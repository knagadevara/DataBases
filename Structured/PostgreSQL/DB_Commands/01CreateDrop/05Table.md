### Create/Drop and other operations on the tables

- Check all the tables present in all Databases

        # SELECT * FROM pg_tables
        // To narrow don to known talble name
        # SELECT * FROM pg_tables WHERE tablename='<table-name>' ;

    --  1: Create the "inventory" schema

        # CREATE SCHEMA inventory;

    --  2: Create the "products", suppliers table within the "inventory" schema
    
        # CREATE TABLE inventory.products (
            product_id SERIAL,
            product_name VARCHAR(100) NOT NULL,
            category VARCHAR(50) NOT NULL,
            price NUMERIC(10, 2) NOT NULL,
            stock_quantity INTEGER NOT NULL,
            CONSTRAINT PRDKT_ID PRIMARY KEY (product_id),
            CONSTRAINT PRDKT_NM UNIQUE (product_name),
            CONSTRAINT PRDKT_PRC_GT0 CHECK (price > 1.0),
            CONSTRAINT PRDKT_STK CHECK (stock_quantity >= 0)
        );


        # CREATE TABLE inventory.suppliers (
            supplier_id SERIAL PRIMARY KEY,
            supplier_name VARCHAR(100) NOT NULL,
            contact_email VARCHAR(100),
            phone_number VARCHAR(20)
        );


    --  3: Add descriptions to the columns using the COMMENT command

        COMMENT ON COLUMN inventory.products.product_id IS 'A unique identifier for each product';
        COMMENT ON COLUMN inventory.products.product_name IS 'The name of the product';
        COMMENT ON COLUMN inventory.products.category IS 'The category to which the product belongs (e.g., electronics, clothing, etc.)';
        COMMENT ON COLUMN inventory.products.price IS 'The price of the product';
        COMMENT ON COLUMN inventory.products.stock_quantity IS 'The quantity of the product available in stock';

    --  4: Add the "on_offer" column to the "inventory.products" table
        
        ALTER TABLE inventory.products ADD COLUMN on_offer BOOLEAN;

    --  5: Add a comment to the "on_offer" column
        
        COMMENT ON COLUMN inventory.products.on_offer IS 'Tells if Product is on Offer';

    --  6: Add a constraint to ensure the "on_offer" column is not null

        ALTER TABLE inventory.products ALTER COLUMN on_offer SET NOT NULL;

    --  7: Add the "supplier_id" column to the "inventory.products" table

        ALTER TABLE inventory.products ADD COLUMN supplier_id INTEGER;

    --  8: Add a foreign key constraint to link the "supplier_id" column to the "suppliers" table

        ALTER TABLE inventory.products ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES inventory.suppliers (supplier_id);

    --  9: Add CHECK constraint for "contact_email" column

        ALTER TABLE suppliers
        ADD CONSTRAINT chk_contact_email_format
        CHECK (contact_email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');

    --  10: Add CHECK constraint for "phone_number" column

        ALTER TABLE suppliers
        ADD CONSTRAINT chk_phone_number_format
        CHECK (phone_number ~ '^\+?\d{1,4}[-.\s]?\(?\d{1,4}\)?[-.\s]?\d{1,4}[-.\s]?\d{1,9}$');

- Create Inherited Tables.

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

- Create Table Partitions

    - RANGE

            CREATE TABLE sales (
                id SERIAL PRIMARY KEY,
                sale_date DATE,
                amount NUMERIC
            )
            PARTITION BY RANGE (sale_date);

            CREATE TABLE sales_q1 PARTITION OF sales
                FOR VALUES FROM ('2023-01-01') TO ('2023-04-01');

            CREATE TABLE sales_q2 PARTITION OF sales
                FOR VALUES FROM ('2023-04-01') TO ('2023-07-01');

    - LIST

            CREATE TABLE employees (
                emp_id SERIAL PRIMARY KEY,
                department TEXT
            )
            PARTITION BY LIST (department);

            CREATE TABLE hr_employees PARTITION OF employees
                FOR VALUES IN ('HR', 'Human Resources');

            CREATE TABLE it_employees PARTITION OF employees
                FOR VALUES IN ('IT', 'Information Technology');
    
    - HASH

            CREATE TABLE sensor_data (
                sensor_id SERIAL PRIMARY KEY,
                timestamp TIMESTAMP,
                value FLOAT
            )
            PARTITION BY HASH (sensor_id);

            CREATE TABLE sensor_data_0 PARTITION OF sensor_data
                FOR VALUES WITH (MODULUS 4, REMAINDER 0);

            CREATE TABLE sensor_data_1 PARTITION OF sensor_data
                FOR VALUES WITH (MODULUS 4, REMAINDER 1);


    - COMPOSITE

            CREATE TABLE sales (
                id SERIAL PRIMARY KEY,
                sale_date DATE,
                region TEXT
            )
            PARTITION BY RANGE (sale_date), LIST (region);

            CREATE TABLE sales_q1_usa PARTITION OF sales
                FOR VALUES FROM ('2023-01-01') TO ('2023-04-01')
                PARTITION BY LIST (region)
                FOR VALUES IN ('USA');

            CREATE TABLE sales_q1_europe PARTITION OF sales
                FOR VALUES FROM ('2023-01-01') TO ('2023-04-01')
                PARTITION BY LIST (region)
                FOR VALUES IN ('Europe');


    - OnInsert Trigger.

            CREATE OR REPLACE FUNCTION insert_sensor_data()
            RETURNS TRIGGER AS
            $$
            BEGIN
                IF NEW.timestamp >= '2023-01-01' AND NEW.timestamp < '2023-04-01' THEN
                    INSERT INTO sensor_data_q1 VALUES (NEW.*);
                ELSEIF NEW.timestamp >= '2023-04-01' AND NEW.timestamp < '2023-07-01' THEN
                    INSERT INTO sensor_data_q2 VALUES (NEW.*);
                ELSE
                    RAISE EXCEPTION 'Invalid timestamp for partitioning';
                END IF;
                RETURN NULL;
            END;
            $$
            LANGUAGE plpgsql;

            CREATE TRIGGER insert_sensor_trigger
            BEFORE INSERT ON sensor_data
            FOR EACH ROW
            EXECUTE FUNCTION insert_sensor_data();
