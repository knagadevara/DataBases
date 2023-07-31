#### Database Basics

- List Databases
    
    - Command after logging in

         \l+

    - See what are the existing databases in the PG-System

        SELECT oid,datname,datacl FROM pg_database;

- Create a database 
    
    - From a Template Database.

        CREATE DATABASE <my_database> TEMPLATE template1;
    
    - Direct Command

        CREATE DATABASE <my_database>

- Delete a Database

        DROP DATABASE library;


- To see the collumns of the table inside the database

        \dS+ <database>.<tableName> ; // Syntax       
        \dS+ pg_catalog.pg_stats ; // example