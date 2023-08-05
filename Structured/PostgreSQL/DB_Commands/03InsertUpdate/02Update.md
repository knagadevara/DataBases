In PostgreSQL, the UPDATE statement is used to modify existing records in a table. It allows you to change the values of one or more columns in one or multiple rows based on specified conditions. The general syntax of the 

- UPDATE statement is as follows:
    - table_name: The name of the table you want to update.
    - SET: Specifies the columns and their new values that you want to update.
    - WHERE: Specifies the conditions that determine which rows should be updated. If omitted, all rows in the table will be updated.

            UPDATE table_name
            SET column1 = value1, column2 = value2, ...
            WHERE condition;


Here are some variations and conditions that can be used with the UPDATE statement:

- Updating Specific Columns: You can update specific columns by providing their names and new values in the SET clause. 

        UPDATE products
        SET price = 59.99, stock_quantity = 150
        WHERE product_id = 1;
        Updating Multiple Rows:

- Multiple Rows: You can update multiple rows that meet the specified condition by using the WHERE clause.

        UPDATE products
        SET category = 'Electronics'
        WHERE stock_quantity <= 10;

- Using Subqueries in the SET Clause: You can use subqueries in the SET clause to update columns based on the results of a subquery. 

        UPDATE products
        SET price = price * 1.1
        WHERE category = 'Electronics' AND price < (SELECT MAX(price) FROM products);
- Updating Columns Using Mathematical Operations: You can perform mathematical operations while updating columns. 

        UPDATE products
        SET price = price + 5.00
        WHERE category = 'Books';

- Updating Columns Using Conditional Expressions: You can use conditional expressions like CASE to perform different updates based on conditions. 

        UPDATE products
        SET price = CASE
                    WHEN stock_quantity < 50 THEN price * 1.2
                    ELSE price * 1.1
                END;

- Returning Updated Rows: PostgreSQL allows you to use the RETURNING clause to return the rows affected by the UPDATE statement. This can be useful to retrieve the updated data. 

        UPDATE products
        SET stock_quantity = stock_quantity - 1
        WHERE product_id = 5
        RETURNING product_id, product_name, stock_quantity;
