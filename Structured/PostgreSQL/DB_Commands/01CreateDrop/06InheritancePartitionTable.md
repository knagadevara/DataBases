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
