## Check configuratin and its parameters
- After making a change inside the postgres.conf some of the configurations to get affected they need to be restarted. 

- Check the present running configurating of the system. 

        SELECT name, setting, category, boot_val, min_val, max_val, sourcefile, pending_restart from pg_settings WHERE name=<'configuration_parameter_name'>;
        // example
        SELECT name, setting, category, boot_val, min_val, max_val, sourcefile, pending_restart from pg_settings WHERE name='max_connections';

- To reload the changes made to configuration file witout restarting from within the admin user session

        SELECT * from pg_reload_conf();

- To check if any parameter to get imposed needs a restart

        SELECT * from pg_file_setting;

- To check the existing live config value

        SHOW <configuration_parameter_name>

- The safest way to change a prameter is by using  "ALTER SYSTEM"
        
        - To set 
        
                ALTER SYSTEM SET configuration_parameter='value' ;

        - To reset 

                ALTER SYSTEM RESET configuration_parameter ;

        - To reset all altered values

                ALTER SYSTEM RESET ALL ;

