#### prompt commands

- Connect to a specific DB on a specific host
    - Using psql

            # psql --host <hostname> --port <port> --database <database-name> --log-file=<FILENAME> --username=<USERNAME> --password

    - Using SSL

            # psql  --host <hostname> --port <port> --username=<USERNAME> --log-file=<FILENAME> "dbname=<databaseName> sslmode=require"

- To Run an external sql file
    
        # psql --host <hostname> --port <port> --database <database-name> --log-file=<FILENAME> --username=<USERNAME> --password --file <path-to-file>

- Internal DB commands.

    \conninfo -> Gives connection details
    \l+ -> List the number of databases in DB-System
    \dn+ -> Lists all the Schemas in Database
    \df+ -> Lists all Functions in Database
    \dv+ -> Lists all Views in Database
    \du+ -> Lists all Users/Roles in Database
    \ds+ -> Lists all Sequences in Database
    \g -> Run previous successfull command
    \timing -> set start/stop time before executing a query
    \e -> edit your previously failed query in a editor
    \ef <function-name> -> edit your function in a editor
    \c <db-name> -> to connect to a database
    \! <command> -> to run environment specific commands
    \! clear -> clears screen in Linux
    \dt+ <schema-name> -> Lists details of objects inside the schema  
    \d+ <schema-name>.<table-name> -> to check colums and their datatypes in table
    \o outputFileName.txt -> Result of the query will be exported into file.
    \set AUTOCOMMIT off -> On a session level you are allowed to commit/rollback;