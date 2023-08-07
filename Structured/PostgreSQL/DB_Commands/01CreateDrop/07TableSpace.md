0. List all the Table Spaces

        SELECT * FROM pg_Tablespace;
1. This scenario is useful when you have specific storage requirements for certain database objects. For example, you may want to store critical tables or indexes on faster disks or on a separate storage system.

        - Create a new tablespace named "my_tablespace" at the location '/path/to/tablespace_directory'
        # CREATE TABLESPACE my_tablespace LOCATION '/path/to/tablespace_directory';

2. In large databases, you may have tables with varying access patterns. Placing frequently accessed tables in a separate tablespace on high-performance storage can enhance query performance.

        - Create a table called "important_data" and store it in the "my_tablespace" tablespace
        CREATE TABLE important_data (
            id SERIAL PRIMARY KEY,
            name VARCHAR(100),
            email VARCHAR(255)
        ) TABLESPACE my_tablespace;

3. As data usage patterns change over time, you may want to move tables to different tablespaces to optimize storage and performance.

        - Move an existing table named "infrequently_used_data" to the "my_tablespace" tablespace
        ALTER TABLE infrequently_used_data SET TABLESPACE my_tablespace;

4. Placing indexes on separate tablespaces can optimize read/write operations and improve overall query performance.

        - Create an index on the "important_data" table and store it in the "my_tablespace" tablespace
        CREATE INDEX idx_important_data_name ON important_data (name) TABLESPACE my_tablespace;

5. When performing complex queries that require significant temporary space, you can allocate temporary files in a dedicated tablespace to avoid performance bottlenecks.

    - Create a temporary table space
    
        CREATE TABLESPACE temporatyTableSpace01 OWNER <postgres/system-super-user> LOCATION '/path/to/tablespace_directory';

    - Set "temporatyTableSpace01" as the location for temporary files in *_postgresql.conf_*
    
        temp_tablespaces = 'temporatyTableSpace01';

    - Reload the configuration
    
        SELECT * from pg_reload_conf();
    
    - Validate the change effect
    
        SHOW temp_tablespaces;
    
-------------------------------------------------------
## Migrate the existing DB onto a different TableSpace.
-------------------------------------------------------

### It is important to do this activity in a offline - Maintenance window.

0. Prepare for Migration -> Before migrating the tables, it's essential to ensure there are no active connections or ongoing transactions that can interfere with the migration process. You can put the database in maintenance mode to avoid any potential issues.

        - Put the database in maintenance mode to block new connections
        SELECT pg_terminate_backend(pg_stat_activity.pid)
        FROM pg_stat_activity
        WHERE datname = 'your_database_name';

        - You can also restrict connections by revoking connect privileges from the public schema.
        REVOKE CONNECT ON DATABASE your_database_name FROM PUBLIC;

1. Check Current Tablespace  and Perform Backup 
    - Before proceeding, verify the current tablespace of the tables you want to migrate. You can use the following query to get a list of tables and their corresponding tablespaces:

            SELECT table_schema, table_name, tablespace_name
            FROM information_schema.tables
            WHERE table_schema NOT LIKE 'pg_%' AND table_schema != 'information_schema';

    - Now, use the pg_dump utility to create a logical backup of your database. This will create a SQL script that contains all the data and schema definitions needed to recreate the database.

            // Run it on all the databases which will be migrated
            pg_dump -h localhost -U your_username -d your_database_name -f backup_file_name.sql

    - Verify Backup, after the backup is completed, you can verify its contents to ensure that all data and schema information have been successfully captured

            cat backup_file_name.sql

2. Create the New Tablespace -> First, you need to create the new tablespace where you want to move the existing tables. You can do this using the CREATE TABLESPACE command. Make sure the new tablespace is created and available before proceeding with the migration.

        - Create a new tablespace named "new_tablespace" at the location '/path/to/new_table_space_directory'
        # CREATE TABLESPACE new_tablespace LOCATION '/path/to/new_table_space_directory';

3. Migrate Tables -> Now, you can move the existing tables to the new tablespace using the ALTER TABLE command. You will need to specify the SET TABLESPACE clause for each table you want to migrate. Remeber to repeat this _ALTER TABLE_ command for each table you want to move to the new tablespace.

        - Migrate a table named "your_table_name" to the new tablespace "new_tablespace"
        ALTER TABLE your_table_name SET TABLESPACE new_tablespace;

4. Confirm Migration -> Once the migration is complete, you can verify that the tables have been moved to the new tablespace by querying the system catalog.

    - Check the location of the table "your_table_name" to confirm migration

        SELECT table_schema, table_name, tablespace_name
        FROM information_schema.tables
        WHERE table_name = 'your_table_name';

    - Check from the catalog

        SELECT schemaname, tablename, tablespace, tableowner
        FROM pg_tables
        WHERE tablename='your_table_name';

    - Check the OS Path

        SELECT pg_relation_filepath('your_table_name');

5. Exit Maintenance Mode -> After confirming the successful migration, you can exit the maintenance mode by allowing connections to the database.

        - Allow connections to the database again
        GRANT CONNECT ON DATABASE your_database_name TO PUBLIC/AnySpecificUserRole  ;

6. Clean Up -> Once you are confident that the migration was successful, you can remove the old tablespace if it's no longer needed. Note that you should only do this after ensuring all data has been successfully moved to the new tablespace.

        - Drop the old tablespace "old_tablespace" after confirming successful migration
        DROP TABLESPACE old_tablespace;

