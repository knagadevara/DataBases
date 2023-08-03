## Users/Roles and Groups in PostgreSQL

- list users/roles

    #\du

- with createuser utility

    - Non interactive        
            # createuser -h <host> -p <5432> -U <previlaged-user> -e -S -W <user-name>
    
    - Interactive
            # createuser -h <host> -p <5432> -U <previlaged-user> -e --interactive

- creating a user

        # CREATE USER <user-name> [NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN] WITH PASSWORD '<user-password>' ;

- dropping user, if the user has no object attached to it.

    - with dropuser utility

            # dropuser -h <host> -p <5432> -U <previlaged-user> -e --interactive <username>

    - with psql

            # drop user <username>

- Creting group

        # CREATE GROUP  <name_of_grp> ;

- Adding user to group

        # ALTER GROUP <name_of_grp> ADD USER <user1> , <user2> , <user3> ... ;

- Alter on user // Applies more on a DB-System/Cluster level. If logged in as SuperUser 

        # ALTER USER <uname> WITH [ NOSUPERUSER , NOCREATEDB , NOCREATEROLE ] 
        # ALTER USER <uname> WITH CREATEDB

- TO check the search_path of the current logged in user/role

        # show search_path;
        
- Set multiple search_paths to a team
        
        ALTER ROLE <user-group> SET search_path TO <schema-name1> , <schema-name2> , <schema-name3> , public;     
        # ALTER ROLE hr_managers SET search_path TO human_resources , public;
        # ALTER ROLE sales_team SET search_path TO sales, inventory, public;

- To create a No Login ROLE

        # CREATE ROLE sales_group NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT NOLOGIN;

- Associating "admin" role to "marketing_team"

    - With GRANT
            # GRANT admin TO marketing_team; // groups
            # GRANT developers TO john_doe, jane_smith; //users
            //GRANT role_name TO { user_name | group_name | PUBLIC } [, ...];
    - With ALTER
            # ALTER ROLE admin ADD MEMBER marketing_team; // groups
            # ALTER ROLE developers ADD MEMBER john_doe, jane_smith; //users
            // ALTER ROLE role_name ADD MEMBER { user_name | group_name } [, ...];

- Grant [SELECT, UPDATE, ALTER, DROP, DELETE] privilege on a [ DATABASE, SCHEMA, TABLE, VIEW ]

        // grant connect to DB
        # GRANT CONNECT ON DATABASE <DB-Name> TO <ROLE/USER> ;
        // grant usage on schema
        # GRANT USAGE ON SCHEMA <SCHEMA-Name> TO <ROLE/USER> ;
        // To a specific column in a table in Schema
        # GRANT SELECT (col1), UPDATE (col2) ON TABLE IN SCHEMA <SCHEMA-NAME>.<table-name> TO <ROLE/USER>; 
        // Grant SELECT privilege on a table to a role
        # GRANT SELECT ON TABLE IN SCHEMA <SCHEMA-NAME>.<TABLE-NAME> TO <ROLE/USER>;
        // To a specific table in a schema
        # GRANT SELECT, UPDATE, ALTER ON TABLE IN SCHEMA <SCHEMA-NAME>.<table-name> TO <ROLE/USER>; 
        // To a table residing DB-System-Cluster wide.
        # GRANT SELECT ON TABLE <table-name> TO <ROLE/USER>;
        // grant DML on all tables in schema
        # GRANT SELECT,INSERT,UPDATE,DELETE,DROP PRIVILEGES ON ALL TABLES IN SCHEMA <SCHEMA-NAME> TO <ROLE/USER>;
        // grant all privileges on all tables in schema
        # GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA <SCHEMA-NAME> TO <ROLE/USER>;
        // PUBLIC includes all the users/roles. Do try in Production!!
        # GRANT SELECT ON TABLE <table-name> TO PUBLIC; 

- By replacing GRANT->REVOKE and TO->FROM all the above operations can be made on DB objects. 

        # REVOKE SELECT ON TABLE IN SCHEMA <SCHEMA>.<TABLE-NAME> FROM <ROLE/USER>;
        # REVOKE ALL ON TABLES IN SCHEMA <SCHEMA>.<TABLE-NAME> FROM <ROLE/USER>;