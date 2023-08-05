- Insert data into the "products" table

        INSERT INTO inventory.products (product_name, category, price, stock_quantity)
        VALUES
            ('Product A', 'Category 1', 10.50, 100),
            ('Product B', 'Category 2', 15.75, 150),
            ('Product C', 'Category 1', 8.99, 200);

- Insert data into the "suppliers" table

        INSERT INTO inventory.suppliers (supplier_name, contact_email, phone_number)
        VALUES
            ('Supplier X', 'supplierx@example.com', '+1234567890'),
            ('Supplier Y', 'suppliery@example.com', '+9876543210'),
            ('Supplier Z', 'supplierz@example.com', '+1122334455');

- Inserting bulk data from a CSV file

    - Example CSV File saved on OS disk

            product_name,category,price,stock_quantity
            Product D,Category 2,12.99,80
            Product E,Category 1,9.95,120
            Product F,Category 2,18.50,50

    - Using *COPY* command to run the Insert from CSV file.

            COPY inventory.products (product_name, category, price, stock_quantity) FROM '/path/to/products_data.csv' DELIMITER ',' CSV HEADER;
