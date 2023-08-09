The ideal method to calculate the cost of queries in PostgreSQL is to use the Query Execution Plan, also known as the "EXPLAIN" plan. The EXPLAIN plan provides insight into how PostgreSQL will execute a query and estimates the cost associated with each step. This helps database administrators and developers understand the query execution process, identify potential bottlenecks, and optimize queries for better performance.

Here's how you can calculate the cost of queries using the EXPLAIN plan in PostgreSQL:

EXPLAIN Command: Use the EXPLAIN command before your query to see the execution plan. You can prefix your query with EXPLAIN to generate the plan without actually executing the query.

        EXPLAIN SELECT * FROM your_table WHERE your_condition;

ANALYZE Option: To see actual execution times and row counts, you can add the ANALYZE option to the EXPLAIN command:

        EXPLAIN ANALYZE SELECT * FROM your_table WHERE your_condition;

- Query Plans and Cost: The query plan will provide details about the order of operations, types of scans, joins, and other relevant information. The "cost" column indicates the estimated cost of each step. The lower the cost, the more efficient the step is expected to be.

- Consider Indexes and Filters: Pay attention to how indexes are used and whether filters are pushed down efficiently. PostgreSQL will consider different access paths and join methods, and the choice made by the planner impacts the overall query performance.

- Index Usage and Join Methods: Look for index scans, sequential scans, and join methods (hash join, nested loop join, etc.). These choices influence query performance. The planner's goal is to minimize the total cost of the query, which includes both CPU and I/O costs.

- Adjusting Configuration: The planner's cost estimates are influenced by various configuration settings, like random_page_cost, seq_page_cost, etc. These can be adjusted to match your system's characteristics more accurately, but be cautious and test thoroughly.

By analyzing the EXPLAIN plan, you can identify potential areas for optimization, like missing indexes, unnecessary sequential scans, or suboptimal join methods. Experiment with different query formulations and indexes to see how the estimated costs change and choose the option that yields the best execution plan.

Cost formula

        (number_of_pages * seq_page_cost) + (number_of_rows * cpu_tuple_cost)

To calculate the cost

        SHOW seq_page_cost();
        
        SELECT name, setting
        FROM pg_settings
        WHERE name = 'seq_page_cost';


        SHOW cpu_tuple_cost();

        SELECT name, setting
        FROM pg_settings
        WHERE name = 'cpu_tuple_cost';


Create Indexes on the tables on required rows to decrease the cost, to check if the table is indexed

        SELECT oid, relname , relowner , reltablespace , relam , reltuples , relpages,  relhasindex , relacl from pg_class WHERE relname=<db.object.name>;
