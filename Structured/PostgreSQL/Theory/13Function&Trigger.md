## FUNCTION
------------
In PostgreSQL, a FUNCTION is a named block of code that can accept input parameters, perform specific actions or calculations, and return a result. Functions are stored and executed within the database and can be called from SQL queries or other functions, providing a way to encapsulate logic and reuse code.

Functionality:
--------------
- Encapsulate complex logic: Functions allow you to encapsulate complex business logic or calculations into a single, reusable unit.
- Modularity and reusability: Functions can be called from multiple queries or other functions, promoting code reusability and modular design.
- Improve query performance: Functions can help improve query performance by reducing redundant code and simplifying complex expressions.

Use Cases:
----------
- Data transformations: Functions can be used to convert data between different formats or apply custom transformations.
- Business logic: Functions can encapsulate business rules and calculations, making code more maintainable and easier to understand.
- Aggregations: Functions can be used to create custom aggregate functions to perform specialized calculations.
- Dependencies: Functions can be dependent on other functions, tables, or types within the database. They may use input parameters to perform their tasks and return results based on these inputs.

Syntax:

- OR REPLACE (optional): If you want to modify an existing function, you can use OR REPLACE to replace the existing function with the new definition. If the function doesn't exist, the OR REPLACE keyword has no effect.
- function_name: The name of the function you want to create.
- parameter_name data_type: The input parameters of the function, if any. Each parameter is defined with a name and its corresponding data type. You can have multiple parameters separated by commas.
- RETURNS return_type: The data type of the value that the function will return. If the function doesn't return anything, you can use RETURNS VOID.
- LANGUAGE language_name: The programming language used to write the function's body. In PostgreSQL, common languages are plpgsql (procedural SQL using the PL/pgSQL language) and sql (pure SQL).
- AS \$$ ... \$$: The function body, which contains the SQL and/or procedural code that implements the function's logic. You can use SQL statements and control structures (IF, LOOP, etc.) if you are using a procedural language like plpgsql.
- DECLARE: (Optional) Used in plpgsql functions to declare local variables.
- BEGIN ... END: (Optional) Used in plpgsql functions to enclose the function body.
- RETURN: (Optional) Specifies the value to be returned by the function. It is required if the function has a non-VOID return type.

        CREATE [ OR REPLACE ] FUNCTION function_name ( [ parameter_name data_type [, ...] ] )
        RETURNS return_type
        LANGUAGE language_name
        AS $$
        DECLARE
            -- Declaration of local variables (optional)
        BEGIN
            -- Function body with SQL and/or procedural code
            -- The function must end with a RETURN statement to specify the result value
        END;
        $$;


## TRIGGER
-----------
In PostgreSQL, a TRIGGER is a named database object that is associated with a table and automatically executes a specified action when certain events occur on the table. Triggers allow you to define custom actions that respond to data changes, providing a way to enforce data integrity, perform automatic updates, and execute complex business logic.

Functionality:
--------------
- Automatic actions: Triggers enable automatic execution of predefined actions when specific events (INSERT, UPDATE, DELETE) occur on a table.
- Data integrity: Triggers can be used to enforce data integrity by validating or modifying data before it is inserted, updated, or deleted.
- Auditing: Triggers can be employed to track changes to the data, providing an audit trail of modifications.

Use Cases:
----------
- Enforcing business rules: Triggers can enforce complex business rules and ensure data consistency.
- Auditing changes: Triggers can log changes made to a table, capturing before and after data for auditing purposes.
- Automatic updates: Triggers can automatically update related data or maintain denormalized data.
- Dependencies: Triggers are dependent on the table to which they are attached and may use other database objects, such as functions or views, to implement their actions.
- BEFORE Trigger: Fired before the triggering event (INSERT, UPDATE, or DELETE) occurs.
- AFTER Trigger: Fired after the triggering event (INSERT, UPDATE, or DELETE) has occurred.
- INSTEAD OF Trigger: Fired instead of the triggering event, typically used for views to handle INSERT, UPDATE, or DELETE operations on the view.

Syntax:

- CONSTRAINT (optional): If you want to associate a trigger with a specific constraint, you can use the CONSTRAINT keyword followed by the constraint name.
- trigger_name: The name of the trigger you want to create.
- BEFORE, AFTER, or INSTEAD OF: The timing of the trigger execution.
- { INSERT | UPDATE | DELETE }: The event that triggers the execution of the trigger.
- ON table_name or ON view_name: The name of the table or view to which the trigger is associated.
- [ FOR EACH ROW | FOR EACH STATEMENT ]: (optional) Specifies whether the trigger function is executed for each row affected by the triggering event (FOR EACH ROW) or once for each statement (FOR EACH STATEMENT). Default is FOR EACH STATEMENT.
- [ WHEN (condition) ]: (optional) Specifies a condition that determines whether the trigger function will be executed or not. If the condition evaluates to false (or null), the trigger won't fire.
- EXECUTE FUNCTION function_name(): The name of the function to be executed when the trigger is fired.

        CREATE [ CONSTRAINT ] TRIGGER trigger_name
        [ INSTEAD | BEFORE | AFTER ] { INSERT | UPDATE | DELETE }
        ON table_name/view_name
        [ FOR EACH ROW | FOR EACH STATEMENT ]
        [ WHEN (condition) ]
        EXECUTE FUNCTION function_name();

- Enable

    ALTER TABLE table_name ENABLE TRIGGER trigger_name;

- Disable

    ALTER TABLE table_name DISABLE TRIGGER trigger_name;