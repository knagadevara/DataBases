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

- Grant SELECT privilege on the "publications" table to all roles (PUBLIC) // Do not use thi in Production!!
        
        # GRANT SELECT ON publications TO PUBLIC;
