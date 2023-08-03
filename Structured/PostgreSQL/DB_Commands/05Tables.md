### Create/Drop and other operations on the tables

- Check all the tables present in all Databases

        # SELECT * FROM pg_tables
        // To narrow don to known talble name
        # SELECT * FROM pg_tables WHERE tablename='<table-name>' ;

        