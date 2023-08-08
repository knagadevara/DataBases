- Custom Format Restore (-Fc): This is the default format used by pg_dump, and you don't need to specify the format explicitly. However, if you want to, you can use the -Fc option with pg_restore.

        pg_restore -d mydatabase backup_file.backup

- Directory Format Restore (-Fd): To perform a directory format restore, use the -Fd option with pg_restore. This will create plain-text SQL files for each table and object in the specified directory (my_directory in this example).

        mkdir my_directory
        pg_restore -Fd my_directory backup_file.backup

- Plain Text Format Restore (-Fp): To perform a plain text format restore, use the -Fp option with pg_restore. This will create a SQL script with SQL commands for each database object.

        pg_restore -Fp -d mydatabase backup_file.backup

- Tar Format Restore (-Ft): To perform a tar format restore, use the -Ft option with pg_restore. This will read the data from the tar archive backup_file.backup.

        pg_restore -Ft -d mydatabase backup_file.backup

- Schema-Only Restore (-s): To perform a schema-only restore, use the -s option with pg_restore. This will restore only the database schema without restoring the actual data.

        pg_restore -s -d mydatabase backup_file.backup

- Data-Only Restore (-a): To perform a data-only restore, use the -a option with pg_restore. This will restore only the data in the database, excluding the schema objects.

        pg_restore -a -d mydatabase backup_file.backup

- Table Selection (-t): To restore specific tables from the backup, use the -t option followed by the table name(s). This will restore only the specified table(s) and their data.

        pg_restore -t table1 -t table2 -d mydatabase backup_file.backup

- Schema Selection (-n): To restore objects from a specific schema, use the -n option followed by the schema name. This will restore only the objects within the specified schema.

        pg_restore -n myschema -d mydatabase backup_file.backup

- Tablespace Mapping (-T): To map the restored tables to a different tablespace, use the -T option followed by a table mapping in the format original_table=tablespace_name. This is useful when moving data to a different location

        pg_restore -T table1=my_tablespace -T table2=my_tablespace -d mydatabase backup_file.backup

- No Owner (-x): To skip setting ownership for restored objects, use the -x option. This can be helpful when restoring into a different database where the original owners might not exist.

        pg_restore -x -d mydatabase backup_file.backup
