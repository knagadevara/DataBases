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
