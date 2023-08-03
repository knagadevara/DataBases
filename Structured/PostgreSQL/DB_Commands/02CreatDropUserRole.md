## Users/Roles and Groups in PostgreSQL

- list users/roles

    #\du

- with createuser utility

    - Non interactive        
            # createuser -h <host> -p <5432> -U <previlaged-user> -e -S -W <user-name>
            Password: 
            SELECT pg_catalog.set_config('search_path', '', false);
            CREATE ROLE <user-name> NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT LOGIN; 
    
    - Interactive
            # createuser -h <host> -p <5432> -U <previlaged-user> -e --interactive

- creating a user

        # CREATE USER <user-name> LOGIN WITH PASSWORD '<user-password>' ;

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

- To create a No Login ROLE

        # CREATE ROLE sales_group NOSUPERUSER NOCREATEDB NOCREATEROLE INHERIT NOLOGIN;

- Grant SELECT privilege on a table to a role
    
        # GRANT SELECT ON <TABLE-NAME> TO <ROLE-NAME>;

- Revoke privilege on a table to a role

        # REVOKE SELECT ON <TABLE-NAME> TO <ROLE-NAME>;

- Associating "admin" role to "marketing_team"

    - With GRANT
            # GRANT admin TO marketing_team; // groups
            # GRANT developers TO john_doe, jane_smith; //users
            //GRANT role_name TO { user_name | group_name | PUBLIC } [, ...];
    - With ALTER
            # ALTER ROLE admin ADD MEMBER marketing_team; // groups
            # ALTER ROLE developers ADD MEMBER john_doe, jane_smith; //users
            // ALTER ROLE role_name ADD MEMBER { user_name | group_name } [, ...];

- Grant SELECT privilege on a table to all roles (PUBLIC) // Do not use thi in Production!!
        
        # GRANT SELECT ON <table-name> TO PUBLIC;
        # GRANT SELECT ON <table-name> TO <ROLE/USER>;

- TO check the search_path of the current logged in user/role

        # show search_path;
        
- Set multiple search_paths to a team
        
        ALTER ROLE <user-group> SET search_path TO <schema-name1> , <schema-name2> , <schema-name3> , public;
        
        # ALTER ROLE hr_managers SET search_path TO human_resources , public;
        # ALTER ROLE sales_team SET search_path TO sales, inventory, public;

