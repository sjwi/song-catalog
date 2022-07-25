String rollbackWar
pipeline {
    agent any
    triggers {
        cron('1 3 * * *')
    }
    environment {
        BRANCH = "${CHANGE_BRANCH ?: GIT_BRANCH}"
    }
    stages {
        stage('Build WAR') {
            when{ expression {false}}
            steps {
                dir('song-catalog') {
                    sh 'mvn clean install package'
                }
            }
        }
        stage('Backup Existing WAR') {
            when {
                expression { env.BRANCH == "main"}
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
            when{ expression {false}}
            steps {
                dir('song-catalog'){
                    script {
                        if (env.BRANCH == "main") {
                            withCredentials([
                                usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                                string(credentialsId:'cfsongs_dns', variable: 'DNS')
                            ]) {
                                sh "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no '/home/$DREAMHOST_UN/$DNS/tomcat/bin/shutdown.sh'"
                                sh "sshpass -p '$DREAMHOST_PW' scp target/ROOT.war $DREAMHOST_UN@$DNS:/home/$DREAMHOST_UN/$DNS/tomcat/webapps"
                                sh "sleep 3"
                                sh "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no '/home/$DREAMHOST_UN/$DNS/tomcat/bin/startup.sh'"
                            }
                        } else if (env.BRANCH == "develop") {
                            sh "sudo mv target/ROOT.war /opt/tomcat/webapps/song-catalog.war"
                        } else {
                            def featureContext = "sc-" + env.BRANCH + ".war"
                            sh "sudo mv target/ROOT.war /opt/tomcat/webapps/$featureContext"
                        }
                    }
                }
            }
        }
        stage('Test Availability') {
            when {
                expression { env.BRANCH == "main"}
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
        stage('Deploy Demo') {
            when {
                expression { env.BRANCH == "develop"}
            }
            steps {
                withCredentials([
                    usernamePassword(credentialsId:'github_token', usernameVariable: 'USER', passwordVariable: 'TOKEN')
                ]) {
                    dir('song-catalog') {
                        sh "git remote set-url origin https://$TOKEN@github.com/sjwi/song-catalog.git && git fetch origin demo && git checkout FETCH_HEAD -- src/main/resources/application.properties"
                        sh '''
                            git checkout origin/demo -- src/main/resources/application.properties
                            git checkout origin/demo -- pom.xml
                            git checkout origin/demo -- src/main/java/com/sjwi/catalog/aspect/LandingPageSessionInitializer.java
                            git checkout origin/demo -- src/main/java/com/sjwi/catalog/mail/Mailer.java
                            git checkout origin/demo -- src/main/resources/schema.sql
                            git checkout origin/demo -- src/main/resources/templates/partial/header.html
                            mvn clean install package
                            sudo cp target/ROOT.war /opt/tomcat/webapps/song-demo.war
                        '''
                    }
                }
            }
        }
    }
}