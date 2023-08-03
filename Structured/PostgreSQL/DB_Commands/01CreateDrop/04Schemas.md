#### Schemas

- Show schemas
        # \dn

- To create a basic schema and assign it to a role.

        # CREATE SCHEMA <schema-name> AUTHORIZATION <role-name>
        # CREATE SCHEMA human_resources AUTHORIZATION hr_managers;

- Grant CREATE privileges on the schema to a group
        
        # GRANT CREATE ON SCHEMA <schema-name> TO <user-group>;
        # GRANT CREATE ON SCHEMA human_resources TO hr_managers;
        # GRANT USAGE, CREATE ON SCHEMA marketing TO marketing_team;

- Rename the "old_schema" to "new_schema"

        # ALTER SCHEMA old_schema RENAME TO new_schema;
        # ALTER SCHEMA human_resources RENAME TO hr_dept;

- Drop the "obsolete_schema" and all its objects(THis deletes all the tables, views, indexed partitions under the schema)
        # DROP SCHEMA <obsolete_schema_name> ; // Does not drop if schema is not empty 
        # DROP SCHEMA <obsolete_schema_name> CASCADE; // Cascade will force and drop all the underlaying objects
