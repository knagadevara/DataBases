#### Database Basics

- List Databases
    
    - Command after logging in

            # \l+

    - See what are the existing databases in the PG-System

            # SELECT oid,datname,datacl FROM pg_database;

- Create a database 
    
    - From a Template Database.

            # CREATE DATABASE <my_database> TEMPLATE template1 ;
    
    - psql way

            # CREATE DATABASE <my_database> OWNER <db_owner> ;

    - With createdb utility

            # createdb -h <host> -p <5432> -U <previlaged-user> -e -T <db-template-if-any> -W <db-name>
            Password: 
            SELECT pg_catalog.set_config('search_path', '', false);
            CREATE DATABASE <db-name> TEMPLATE <db-template-if-any>

- Delete a Database (Cannot drop open databases)
    - psql way
    
                # DROP DATABASE <db-name>;
    
    - With dropdb utility

                # dropdb -h <host> -p <5432> -U <previlaged-user> -e -W <db-name>
                Password: 
                SELECT pg_catalog.set_config('search_path', '', false);
                DROP DATABASE <db-name>;


- To see the collumns of the table inside the database

        # \dS+ <database>.<tableName> ; // Syntax       
        # \dS+ pg_catalog.pg_stats ; // example

- To restrict all connections on the DB (This only applies for normal users and not super-user)

        # REVOKE CONNECT ON DATABASE <DB-NAME> FROM PUBLIC;