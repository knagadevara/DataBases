#### PG System schema which hods the details of all the System meta data

- Queries to get details from various PG System Tables

        # SELECT * FROM pg_database; // Elaborative info about all the databases in DBMS
        # SELECT * FROM pg_stat_database; // Events and activities per Database
        # SELECT * FROM pg_tablespace; // Gets info about Table Spaces
        # SELECT * FROM pg_operator;  // Gets supported Operators
        # SELECT * FROM pg_available_extensions; // Get available extensions
        # SELECT * FROM pg_shadow; // Get details about user/roles
        # SELECT * FROM pg_time_zone_names; // Get Time Zones
        # SELECT * FROM pg_locks; // Check if any locks on tables with queries
        # SELECT * FROM pg_tables; // Details of the table, tablespace and its schema 
        # SELECT * FROM pg_indexes; // Details of the index and its creation
        # SELECT * FROM pg_views; // Details on how view is created

- Query to get details of current session

        # SELECT current_user;
        # SELECT current_schema();
        # SELECT current_database();
        # SELECT current_settings('<parameter>')
        # SELECT pg_postmaster_start_time() // gives the info on the DBMS Startup time
        # SELECT now() as CurrentTime ; // gives current time
        # SELECT (now() + interval '1 day'); as TomorrowSameTime ; // Go forward by one day
        # SELECT (now() - interval '1 day 2 hours 30 minuits'); as Yesterday ; // Go backward by given interval