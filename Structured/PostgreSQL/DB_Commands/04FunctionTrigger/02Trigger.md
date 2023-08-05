1. Automatic Timestamp , Automatically updates the created_at column with the current timestamp whenever a new row is inserted into the products table
 
        CREATE OR REPLACE FUNCTION update_created_at()
        RETURNS TRIGGER AS
        $$
        BEGIN
            NEW.created_at := NOW();
            RETURN NEW;
        END;
        $$
        LANGUAGE plpgsql;

        CREATE TRIGGER trigger_update_created_at
        BEFORE INSERT ON products
        FOR EACH ROW
        EXECUTE FUNCTION update_created_at();

2. Auditing Changes, trigger that logs changes made to the employees table into an audit table called employees_audit

        CREATE OR REPLACE FUNCTION audit_employee_changes()
        RETURNS TRIGGER AS
        $$
        BEGIN
            IF TG_OP = 'DELETE' THEN
                INSERT INTO employees_audit (action, employee_id, changed_at)
                VALUES ('DELETE', OLD.employee_id, NOW());
            ELSE
                INSERT INTO employees_audit (action, employee_id, changed_at)
                VALUES ('UPDATE', NEW.employee_id, NOW());
            END IF;
            RETURN NEW;
        END;
        $$
        LANGUAGE plpgsql;

        CREATE TRIGGER trigger_audit_employee_changes
        AFTER INSERT OR UPDATE OR DELETE ON employees
        FOR EACH ROW
        EXECUTE FUNCTION audit_employee_changes();

3. Data Integrity, prevents inserting a new row into the orders table if the total_amount exceeds the customer's credit limit

        CREATE OR REPLACE FUNCTION check_credit_limit()
        RETURNS TRIGGER AS
        $$
        BEGIN
            IF NEW.total_amount > (SELECT credit_limit FROM customers WHERE customer_id = NEW.customer_id) THEN
                RAISE EXCEPTION 'Credit limit exceeded';
            END IF;
            RETURN NEW;
        END;
        $$
        LANGUAGE plpgsql;

        CREATE TRIGGER trigger_check_credit_limit
        BEFORE INSERT ON orders
        FOR EACH ROW
        EXECUTE FUNCTION check_credit_limit();

- Calling Trigger

    - Example 1: Automatic Timestamp: The trigger will automatically set the `created_at` column with the current timestamp.
    
            INSERT INTO products (product_name) VALUES ('Widget 1');

    - Example 2: Auditing Changes: The trigger will log the update into the `employees_audit` table.
            
            UPDATE employees SET salary = salary + 1000 WHERE department = 'HR';

    - Example 3: Data Integrity:  The trigger will check the credit limit of the customer (customer_id = 101) and raise an exception if it's exceeded.

            INSERT INTO orders (customer_id, total_amount) VALUES (101, 5000);
