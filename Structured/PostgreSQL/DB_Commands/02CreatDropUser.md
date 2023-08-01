## Create a User in PostgreSQL

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
