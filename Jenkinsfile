String rollbackWar
pipeline {
    agent any
    stages {
        stage('Build WAR') {
            steps {
                dir('song-catalog') {
                    sh 'mvn clean install package'
                }
            }
        }
        stage('Backup Existing WAR') {
            when {
                expression { env.GIT_BRANCH == "main"}
            }
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                    string(credentialsId:'cfsongs_dns', variable: 'DNS')
                ]) {
                    script {
                        rollbackWar = sh(script: "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no '/home/$DREAMHOST_UN/scripts/backup_cfsongs.sh'", returnStdout: true).trim()
                    }
                }
            }
        }
        stage('Deploy to App Server') {
            steps {
                dir('song-catalog'){
                    script {
                        if (env.GIT_BRANCH == "main") {
                            withCredentials([
                                usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                                string(credentialsId:'cfsongs_dns', variable: 'DNS')
                            ]) {
                                sh "sshpass -p '$DREAMHOST_PW' scp target/ROOT.war $DREAMHOST_UN@$DNS:/home/$DREAMHOST_UN/$DNS/tomcat/webapps"
                            }
                        } else if (env.GIT_BRANCH == "develop") {
                            sh "sudo mv target/ROOT.war /opt/tomcat/webapps/song-catalog.war"
                        } else {
                            def featureContext = "sc-" + env.GIT_BRANCH + ".war"
                            sh "sudo mv target/ROOT.war /opt/tomcat/webapps/$featureContext"
                        }
                    }
                }
            }
        }
        stage('Test Availability') {
            when {
                expression { env.GIT_BRANCH == "main"}
            }
            steps {
                withCredentials([
                    string(credentialsId:'cfsongs_dns', variable: 'DNS')
                ]) {
                    script {
                        int testCounter = 0
                        def appStatus = "0" //init value to wait at least 10 seconds
                        while (appStatus != "200" && testCounter < 12) {
                            sleep(10)
                            testCounter++
                            appStatus = sh(script: "curl -s -o /dev/null -w '%{http_code}' https://$DNS/server-availability", returnStdout: true).trim()
                        }
                        if (appStatus != "200")
                            throw new Exception("Application failed to deploy new .war - status code: $appStatus")
                    }
                }
            }
            post {
                failure {
                    withCredentials([
                        usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                        string(credentialsId:'cfsongs_dns', variable: 'DNS')
                    ]) {
                        echo "Reverting app back to war $rollbackWar ..."
                        sh "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no 'cp $rollbackWar /home/$DREAMHOST_UN/$DNS/tomcat/webapps/ROOT.war'"
                        echo "Rollback finished."
                    }
                }
            }
        }
    }
}