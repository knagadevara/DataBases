
- Calculate area of circle

        CREATE OR REPLACE FUNCTION calculate_area(radius NUMERIC)
        RETURNS NUMERIC AS
        $$
        BEGIN
            RETURN 3.14159 * radius * radius;
        END;
        $$
        LANGUAGE plpgsql;

- Grade Student

        CREATE OR REPLACE FUNCTION get_student_grade(score INT)
        RETURNS VARCHAR AS
        $$
        BEGIN
            IF score >= 90 THEN
                RETURN 'A';
            ELSIF score >= 80 THEN
                RETURN 'B';
            ELSIF score >= 70 THEN
                RETURN 'C';
            ELSIF score >= 60 THEN
                RETURN 'D';
            ELSE
                RETURN 'F';
            END IF;
        END;
        $$
        LANGUAGE plpgsql;

- Get total Sales

        CREATE OR REPLACE FUNCTION get_total_sales(product_id INT)
        RETURNS NUMERIC AS
        $$
        DECLARE
            total_sales NUMERIC;
        BEGIN
            SELECT SUM(amount) INTO total_sales FROM sales WHERE product_id = product_id;
            RETURN total_sales;
        END;
        $$
        LANGUAGE plpgsql;

- Calling a Function 

    - Example 1: Call the calculate_area function

            SELECT calculate_area(5); -- Output: 78.53975

    - Example 2: Call the get_student_grade function

            SELECT get_student_grade(85); -- Output: 'B'

    - Example 3: Call the get_total_sales function

            SELECT get_total_sales(101); -- Output: Total sales for product_id 101

