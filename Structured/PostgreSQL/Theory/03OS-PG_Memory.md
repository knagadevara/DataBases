###### OperatingSystem and DB Memory

### Pages

- In PostgreSQL, "pages" refer to fixed-size blocks of data on the disk used to store the actual data of database tables and indexes. These pages are the fundamental units of storage within PostgreSQL's data files.

- When you create a table or index in a PostgreSQL database, the data associated with them is stored on disk in these fixed-size pages. Each page has a specific size, typically 8KB (kilobytes) by default, although this size can be configured during database creation. It means that the data in PostgreSQL is organized into these 8KB chunks.

- When you insert or update data in a table, PostgreSQL will manage the storage of that data by allocating space in these pages. If the data size exceeds the available space in a page, PostgreSQL will split the data across multiple pages as needed.

- This page-based storage system is efficient because it allows PostgreSQL to read and write data in predictable chunks, reducing the need for random access to different parts of the data files. It also helps in maintaining data integrity and providing concurrency control by locking and managing access to individual pages when multiple transactions are accessing the database simultaneously.

- The process of managing pages, including reading and writing data to and from disk, is taken care of by the PostgreSQL storage engine. It ensures that data is persisted properly and efficiently while optimizing database performance.

- In summary, pages in PostgreSQL are fixed-size blocks of data on the disk that store the actual data of database tables and indexes, and they play a crucial role in organizing, managing, and accessing data in the database efficiently.

### Shared Buffer

- In PostgreSQL, the "Shared Buffer" refers to a portion of the computer's memory (RAM) that is used as a cache to store frequently accessed data pages from the database. This cache helps improve the performance of PostgreSQL by reducing the need to read data directly from the slower disk storage.

- When PostgreSQL reads data from the disk, it does so in larger chunks called "blocks" (commonly 8KB in size). Once the data is read from the disk, it is stored in the Shared Buffer so that subsequent reads of the same data can be served directly from memory, which is much faster than accessing the disk.

- The Shared Buffer is shared among all active database connections (i.e., sessions or queries) in PostgreSQL. When multiple users or applications query the same data, they can take advantage of the data already present in the buffer, leading to faster response times.

- The way it works is as follows:

    - Data Read: When a query is executed in PostgreSQL, and it requires data from a table or index, the system checks if the required data is already present in the Shared Buffer.

    - Cache Hit: If the data is found in the Shared Buffer (a "cache hit"), PostgreSQL retrieves the data directly from memory, avoiding the need to read it from the disk. This results in faster data access and improved query performance.

    - Cache Miss: If the data is not present in the Shared Buffer (a "cache miss"), PostgreSQL needs to fetch the data from the disk and load it into the buffer before serving the query result. While this can cause some initial delay, subsequent queries that require the same data will benefit from the cached copy in the buffer.

- The size of the Shared Buffer is configurable during PostgreSQL installation or can be modified later in the configuration settings. The optimal size of the Shared Buffer depends on the available system memory, the nature of the workload, and the size of the frequently accessed data.

- A larger Shared Buffer can lead to better performance, as more data can be cached in memory. However, an excessively large buffer may lead to competition for memory resources and negatively impact other applications running on the same server.

- In summary, the Shared Buffer in PostgreSQL is a cache in memory used to store frequently accessed data pages from the database, providing faster data access and improved query performance by reducing the need to read data directly from the disk.

### Dirty Buffer

- In the case of the PostgreSQL database (and many other databases as well), a "dirty buffer" refers to a data page that has been modified in memory (in the shared buffer), but the changes have not yet been written to the data files on the disk.

- When data is modified or inserted into a database, it is first written to the shared buffer, which is a portion of memory used for caching data pages. This helps improve database performance by reducing the frequency of disk reads and writes. However, to ensure durability and consistency of data, the changes must eventually be written to the physical data files on disk.

- PostgreSQL uses a process called "write-ahead logging" (WAL) to handle the writing of dirty buffers to disk. In WAL, changes are first recorded in a transaction log (the "write-ahead log") before they are applied to the actual data files. This log allows for crash recovery and replication, as well as ensures that changes are written to disk in a controlled and consistent manner.

- So, in summary, data committed to the shared buffer but not yet written to the data files on disk is indeed referred to as a "dirty buffer" in the context of PostgreSQL and many other database systems.
