##### Basic SELECT

- Get the count of all the Rows in DB

        # SELECT count(*) FROM <DB-Name>
        # SELECT count(*) FROM inventory.products ;

- Get particular columns and WHERE with AND/OR augumentation and wild card 

        # SELECT col1, col2, col3 FROM <DB-Name> WHERE colN LIKE 'Se%' [OR|AND] colM='SearchValue' ;  

- GroupBy

        # SELECT col1, sum(col2) AS "Some-String" , col3 FROM <DB-Name> GROUP BY col3 ;