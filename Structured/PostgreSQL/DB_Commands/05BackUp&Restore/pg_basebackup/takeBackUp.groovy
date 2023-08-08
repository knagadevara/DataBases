pipeline {
    agent any
    parameters {
        string(name: 'DB_HOST', description: 'PostgreSQL Hostname or IP Address', defaultValue: 'localhost')
    }
    triggers {
        cron('H 0 */3 * *')
    }
    
    stages {
        stage('Start Backup') {
            steps {
                script {
                    def backupDirectory = "/path/to/backup/directory"
                    def dbHost = params.DB_HOST
                    
                    withCredentials([
                        usernamePassword(credentialsId: 'postgres_credentials', usernameVariable: 'DB_USERNAME', passwordVariable: 'DB_PASSWORD')
                    ]) {
                        // Execute pg_basebackup command
                        sh "pg_basebackup -h ${dbHost} -U ${DB_USERNAME} -D ${backupDirectory} -Ft -Xs -P"
                    }
                }
            }
        }
        
        stage('Send Email Notification') {
            steps {
                script {
                    def recipientEmail = "sai.karthik.n@gmail.com"
                    
                    emailext (
                        subject: "PostgreSQL Backup Successful",
                        body: "The PostgreSQL backup job has been completed successfully.",
                        to: recipientEmail
                    )
                }
            }
        }
    }
}
