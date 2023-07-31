## Check configuratin and its parameters
- After making a change inside the postgres.conf some of the configurations to get affected they need to be restarted. 

- Check the present running configurating of the system. 

    select name, setting, category, boot_val, min_val, max_val, sourcefile, pending_restart from pg_settings WHERE name=<'configuration_parameter_name'>;

    example:

        postgres=# select name, setting, category, boot_val, min_val, max_val, sourcefile, pending_restart from pg_settings WHERE name='max_connections';
            name       | setting |                       category                       | boot_val | min_val | max_val |                   sourcefile                    | pending_restart

        max_connections | 100     | Connections and Authentication / Connection Settings | 100      | 1       | 262143  | /var/lib/postgresql/data/pgdata/postgresql.conf | f
        (1 row)

- To reload the changes made to configuration file witout restarting from within the admin user session

        select * from pg_reload_conf();

- To check if any parameter to get imposed needs a restart

        select * from pg_file_setting

- To check the existing live config value

        show <configuration_parameter_name>

- The safest way to change a prameter is by using  "ALTER SYSTEM"
        
        - To set 
        
                ALTER SYSTEM SET configuration_parameter='value' ;

        - To reset 

                ALTER SYSTEM RESET configuration_parameter ;

        - To reset all altered values

                ALTER SYSTEM RESET ALL ;

