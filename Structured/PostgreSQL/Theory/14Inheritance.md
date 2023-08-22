In PostgreSQL, inheritance is a feature that allows you to create a hierarchical relationship between tables, where child tables inherit properties and data from a parent table. This concept is known as table inheritance. Inheritance is a powerful mechanism that enables data partitioning, code reuse, and simplification of complex data models. Let's explore the functionality, use cases, and variations of table inheritance in PostgreSQL in detail:

Functionality of Table Inheritance: Table inheritance allows you to create a parent-child relationship between tables, where child tables inherit the structure (columns) and constraints from the parent table. Child tables can store additional data specific to their context, while shared attributes are stored in the parent table.

Use Cases:
- Data Partitioning: Table inheritance can be used to partition large datasets across multiple child tables, allowing for better data management and performance optimization.
- Code Reuse: By creating a common parent table with shared attributes, you can avoid duplicating column definitions and constraints in multiple tables.
- Table Customization: Child tables can extend the attributes of the parent table and add new columns specific to their use case, while still maintaining a connection to the shared parent attributes.
Variations of Table Inheritance:

1. Single Table Inheritance: In this variation, all child tables directly inherit from the same parent table, forming a flat hierarchy. Each child table corresponds to a specific type or category of data.

        - Example: Let's assume we have a parent table vehicles and two child tables cars and bikes. Both cars and bikes have a relationship with the parent table vehicles, sharing common attributes like make, model, and year, but they may also have specific columns like num_doors for cars and wheel_size for bikes.

2. Multiple Table Inheritance: In this variation, child tables can inherit from different parent tables, forming a multi-level hierarchy.

        - Example: We have a parent table employees with general employee information, and two child tables full_time_employees and contract_employees. The full_time_employees table inherits from employees and adds additional columns like salary, while the contract_employees table also inherits from employees but has different columns like contract_duration.

3. Joined Table Inheritance: In this variation, child tables inherit from multiple parent tables, but data is stored in separate tables. The child tables have a foreign key relationship with each parent table.

        - Example: We have a parent table users with general user information and two child tables customers and employees. Both customers and employees inherit from users and contain specific data relevant to their roles, but the data is stored separately in different tables.

4. Concrete Table Inheritance: In this variation, each child table corresponds to a concrete entity with its own table structure. The parent table is not used to store any data directly.

        - Example: We have a parent table vehicles with shared attributes, but it doesn't store any data. Instead, data is stored directly in the child tables like cars and bikes.

5. Implementing Table Inheritance: To create a table inheritance hierarchy, you need to use the CREATE TABLE statement with the INHERITS keyword to specify the parent table.

        - Create the parent table

                        CREATE TABLE vehicles (
                        vehicle_id SERIAL PRIMARY KEY,
                        make VARCHAR(50) NOT NULL,
                        model VARCHAR(100) NOT NULL,
                        year INTEGER NOT NULL
                        );

        - Create child tables with table inheritance

                        CREATE TABLE cars (
                        num_doors INTEGER NOT NULL,
                        FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
                        ) INHERITS (vehicles);

                        CREATE TABLE bikes (
                        wheel_size INTEGER NOT NULL,
                        FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
                        ) INHERITS (vehicles);

In this example, both the cars and bikes tables inherit from the vehicles table, sharing its attributes while also containing their own specific attributes.

Querying Tables with Table Inheritance: When querying data from a parent table with child tables, the results will include data from all tables in the inheritance hierarchy. To distinguish between parent and child table records, you can use the ONLY keyword.

-- Query vehicles and child tables (cars and bikes)
        
        SELECT * FROM vehicles;

-- Query only records from the parent table (vehicles)
        
        SELECT * FROM ONLY vehicles;

In conclusion, table inheritance in PostgreSQL is a versatile feature that provides various ways to structure and organize data in hierarchical relationships. By leveraging inheritance, you can simplify your data model, improve performance, and manage related data efficiently. However, table inheritance should be used judiciously, considering the complexity and data requirements of your application, as well as the potential impact on query performance and data integrity.