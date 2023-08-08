"Archiving with Point-in-Time Recovery (PITR)" is a robust approach for creating online backups and enabling point-in-time recovery in PostgreSQL. To implement this method with the best configuration, we need to set up continuous archiving and ensure a seamless restoration process. 

#### 1. Set Up Continuous Archiving

1. Configure postgresql.conf: Open the postgresql.conf file, which is typically located in the PostgreSQL data directory. Uncomment or add the following lines to enable continuous archiving.

    - wal_level = replica: Sets the level of information written to the write-ahead log (WAL) files. "replica" enables archiving and is required for PITR. This setting ensures that enough information is written to the WAL files for point-in-time recovery to be possible.
    - archive_mode = on: Enables continuous archiving.
    - archive_command: Specifies the command to copy WAL files to the archive directory. In this example, we use a simple cp command. You may choose to use a more sophisticated command depending on your environment, such as syncing to a remote location or using a dedicated archiving tool. This command is executed every time a new WAL file is filled. In this example, we use a simple cp command to copy the WAL file to the archive directory. Depending on your setup, you might use more advanced archiving tools or copy the files to a remote location.

            # Enable continuous archiving.
            wal_level = replica
            archive_mode = on
            archive_command = 'cp %p /path/to/archive/%f'


2. Create the Archive Directory: Create the directory specified in the archive_command where WAL files will be archived. Make sure PostgreSQL has read and write permissions to this directory.

        mkdir /path/to/archive
        chown postgres:postgres /path/to/archive

3. Restart PostgreSQL: Restart the PostgreSQL server to apply the changes made in postgresql.conf.

        pg_ctl restart -D /path/to/data

#### 2. Perform Base Backup 

Before starting continuous archiving, we need to perform a base backup. This creates a starting point for point-in-time recovery. Detailed [steps](Structured/PostgreSQL/Theory/18BackUp&Restore.md)

1. Stop PostgreSQL: Stop the PostgreSQL server to ensure consistency during the backup.

        pg_ctl stop -D /path/to/data

2. Take the Base Backup: Use the pg_basebackup utility to perform the base backup. The -D option specifies the backup destination directory.

        pg_basebackup -h <hostname> -U <username> -D <backup_directory> -Ft -Xs -P

#### 3. Start Continuous Archiving

Now that the base backup is complete, we can start continuous archiving to keep the archive directory up-to-date with new WAL files.

1. Start PostgreSQL in Archive Mode: Start the PostgreSQL server with continuous archiving enabled.

        pg_ctl start -D /path/to/data

2. Test if archiving is working on the configured path by creating WAL file

        # SELECT pg_switch_wal(); // creates it

#### 4. Perform Point-in-Time Recovery (PITR)

In case of data loss or the need to restore the database to a specific point in time, we can perform point-in-time recovery using the archived WAL files.

1. Stop PostgreSQL: Stop the PostgreSQL server before initiating the PITR process.

        pg_ctl stop -D /path/to/data

2.  Create a Recovery Configuration File: 
    - Version less than PG12: Create a file named recovery.conf in the PostgreSQL data directory with the contents.

                touch recovery.conf

    - Version more than PG12: Create a file named recovery.signal in the PostgreSQL data directory. Add the contents to postgresql.conf/postgresql.auto.conf

                touch recovery.signal

        - restore_command: Specifies the command to copy the necessary WAL files from the archive to the recovery location.
        - recovery_target_inclusive: (Default -> On) Specifies to stop the recovery after target is reached(ON) or
        just before the target is reached(OFF).
        - recovery_target_time: Specifies the time to which the database should be restored.
        - recovery_target_action: Determines what PostgreSQL should do after reaching the recovery target. In this example, we use 'promote' to promote the database to a writable state after recovery

                        restore_command = 'cp /path/to/archive/%f %p'
                        recovery_target_time = 'YYYY-MM-DD HH:MI:SS'
                        recovery_target_action = 'promote'
                        recovery_target_inclusive = on

3. Start PostgreSQL in Recovery Mode: Start the PostgreSQL server in recovery mode with the -o option to specify the recovery configuration file.

                pg_ctl start -D /path/to/data -o "-c config_file=/path/to/data/recovery.conf"
                pg_ctl start -D /path/to/data // above PG-Ver 12


4. Monitor progress and check logs:
        - After starting the server in recovery mode, PostgreSQL will automatically apply the necessary archived WAL files to reach the specified recovery target time. Once the recovery is complete, the database will be in a consistent state, allowing read-write operations.
        - To accept connections give the below command

                # SELECT pg_wal_replay_resume();

### Variations in PITR:

- Restore to a Specific Time: Instead of restoring to a specific time, you can restore to a specific transaction log file or log sequence number (LSN) using recovery_target_xid or recovery_target_lsn in the recovery.conf file.
        - To check the current WAL **L**ast **S**equence **N**umber.

                # SELECT pg_current_wal_lsn() as "XID", pg_walfile_name(pg_current_wal_lsn()) as "Name" 

- Restore to a Named Restore Point: You can set recovery_target_name in the recovery.conf file to restore to a named restore point created using pg_create_restore_point during the base backup.
        - To create a restore point manually

                # SELECT pg_create_restore_point('restore_point_name');

        - To restore to the last restore point
                
                // For PostgreSQL 15 and earlier (recovery.conf):
                recovery_target_name = 'restore_point_name'
                restore_command = 'cp /path/to/archive/%f %p'

                // For PostgreSQL 16 and later (postgresql.auto.conf):
                restore_command = 'cp /path/to/archive/%f %p'
                recovery_target = 'immediate'
                recovery_target_action = 'promote'

- Restore to End of Logs: If you don't specify a target time or target LSN, PostgreSQL will automatically restore to the end of the available WAL logs, effectively undoing any uncommitted transactions.

        restore_command = 'cp /path/to/archive/%f %p'
        recovery_target = 'immediate'


- restore_command: Instead of using a simple cp command in the restore_command, you can use more advanced methods like syncing to a remote location or using third-party archiving tools.

    1. rsync: The rsync command is a popular utility for efficiently synchronizing files and directories between different locations. It can be used to copy WAL files to the archive directory in a more optimized manner.

            archive_command = 'rsync %p /path/to/archive/%f'
            restore_command = 'rsync /path/to/archive/%f %p'

    2. scp: The scp command (secure copy) is used to securely transfer files between hosts over a network. It can be used for archiving WAL files to a remote server.

            archive_command = 'scp %p user@remote_server:/path/to/archive/%f'
            restore_command = 'scp user@remote_server:/path/to/archive/%f %p'

    3. Amazon S3 Commands: If you are using Amazon S3 as the archive storage, you can use AWS CLI commands to upload and download files to/from S3.

            archive_command = 'aws s3 cp %p s3://bucket_name/archive/%f'
            restore_command = 'aws s3 cp s3://bucket_name/archive/%f %p'

    4. Using Compression: You can use compression utilities like gzip or bzip2 to compress the archived WAL files, saving storage space.

            archive_command = 'gzip -c %p > /path/to/archive/%f.gz'
            restore_command = 'gzip -dc /path/to/archive/%f.gz > %p'

    5. Using Encryption: For added security during transfer or storage, you can use encryption utilities like gpg to encrypt the archived WAL files.

            archive_command = 'gpg --encrypt --recipient recipient_email %p > /path/to/archive/%f.gpg'
            restore_command = 'gpg --decrypt /path/to/archive/%f.gpg > %p'

    6. Cloud Storage Commands: For cloud-based archive storage, you can use commands specific to the cloud provider, such as Azure Blob Storage or Google Cloud Storage.

            archive_command = 'az storage blob upload --account-name myaccount --account-key mykey --container-name mycontainer --name %f --type block --source %p'
            restore_command = 'az storage blob download --account-name myaccount --account-key mykey --container-name mycontainer --name %f --destination %p'

    7. Invoking a script: In PostgreSQL, you can invoke a script in place of a command for configuration parameters like archive_command and restore_command. Invoking a script allows you to execute more complex logic and operations as part of archiving or restoration processes. To achieve this, you need to specify the script path and any necessary arguments in the configuration file (postgresql.conf).

    - Source(archive_command): In the configuration, %p and %f are placeholders representing the full path of the WAL file to be archived and the filename of the WAL file, respectively. The script (your_script.sh) will be executed with these arguments when a new WAL file needs to be archived.
    Here's how you can invoke a script for archive_command and restore_command:

            archive_command = 'path/to/your_script.sh %p %f'

            ----------------------------------------------------------------------------
            #!/bin/bash

            # Set your AWS credentials and S3 bucket name
            AWS_ACCESS_KEY="your-access-key"
            AWS_SECRET_KEY="your-secret-key"
            S3_BUCKET="your-s3-bucket-name"

            # Set remote server details
            REMOTE_USER="remote_user"
            REMOTE_SERVER="remote.server.example.com"
            REMOTE_PATH="/remote/path/"

            # Set the archive directory
            ARCHIVE_DIR="/path/to/archive"

            # Get the path to the WAL segment
            WAL_SEGMENT=$1

            # Get the filename of the WAL segment
            WAL_FILENAME=$(basename "$WAL_SEGMENT")

            # Archive the WAL segment to the remote server
            rsync -avz "$WAL_SEGMENT" "$REMOTE_USER"@"$REMOTE_SERVER":"$REMOTE_PATH"

            # Compress the backup
            gzip "$ARCHIVE_DIR/$WAL_FILENAME"

            # Copy the compressed backup to AWS S3
            aws s3 cp "$ARCHIVE_DIR/$WAL_FILENAME.gz" "s3://$S3_BUCKET/"
            --------------------------------------------------------------------------------

    - Destination(restore_command): Similarly %p and %f represent the full path of the destination file where the WAL segment needs to be restored and the filename of the WAL segment to be restored, respectively. The script will be executed with these arguments during the restoration process.

            restore_command = 'path/to/your_script.sh %f %p'
            --------------------------------------------------------------------------------
            #!/bin/bash

            # Set your AWS credentials and S3 bucket name
            AWS_ACCESS_KEY="your-access-key"
            AWS_SECRET_KEY="your-secret-key"
            S3_BUCKET="your-s3-bucket-name"

            # Set remote server details
            REMOTE_USER="remote_user"
            REMOTE_SERVER="remote.server.example.com"
            REMOTE_PATH="/remote/path/"

            # Set the restore directory
            RESTORE_DIR="/path/to/restore"

            # Get the path to the WAL segment
            WAL_SEGMENT=$1

            # Get the filename of the WAL segment
            WAL_FILENAME=$(basename "$WAL_SEGMENT")

            # Restore the compressed backup from AWS S3
            aws s3 cp "s3://$S3_BUCKET/$WAL_FILENAME.gz" "$RESTORE_DIR/"
            gzip -d "$RESTORE_DIR/$WAL_FILENAME.gz"

            # Rsync the backup files from the remote server
            rsync -avz "$REMOTE_USER"@"$REMOTE_SERVER":"$REMOTE_PATH/$WAL_FILENAME" "$WAL_SEGMENT"

            # Send a notification to AWS SNS
            aws sns publish --topic-arn arn:aws:sns:us-east-1:123456789012:your-topic \
                --subject "Restore Complete" --message "Database restore of $WAL_FILENAME has been completed."

            # Clean up the downloaded backup file
            rm "$WAL_SEGMENT"

            --------------------------------------------------------------------------------

Note: To enable continuous archiving with PITR successfully, it's crucial to regularly monitor the archive directory and the available disk space to ensure sufficient storage for archived WAL files.

        // Only one of the target is allowed.
        restore_command
        recovery_target_inclusive
                AND
        recovery_target // Once the consistency is reached recovery is stopped 'immediately'
                OR
        recovery_target_name // The name of the previous successfull checkpoint
                OR
        recovery_target_xid // The LSN id details
                OR
        recovery_target_lsn // LSN name
                OR
        recovery_target_time // PITR, the time till which the recovery shall be done.

In conclusion, Continuous Archiving with Point-in-Time Recovery (PITR) provides a robust method for online backups and the ability to recover the database to a specific point in time. By following the above configuration and variations, administrators can ensure a reliable backup and restoration process, allowing for data integrity and business continuity.