String rollbackWar
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                dir('song-catalog') {
                    sh 'mvn clean install package'
                }
            }
        }
        stage('Backup') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                    string(credentialsId:'cfsongs_dns', variable: 'DNS')
                ]) {
                    rollbackWar = sh(script: "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no '/home/$DREAMHOST_UN/scripts/backup_cfsongs.sh'", returnStdout: true).trim()
                }
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                    string(credentialsId:'cfsongs_dns', variable: 'DNS')
                ]) {
                    dir('song-catalog'){
                        sh "sshpass -p '$DREAMHOST_PW' scp target/ROOT.war $DREAMHOST_UN@$DNS:/home/$DREAMHOST_UN/$DNS/tomcat/webapps"
                    }
                }
            }
        }
        stage('Test availability') {
            steps {
                withCredentials([
                    string(credentialsId:'cfsongs_dns', variable: 'DNS')
                ]) {
                    script {
                        int testCounter = 0
                        def appStatus = getAppServerStatusCode()
                        while (appStatus != "200" && testCounter < 12) {
                            sleep(10)
                            testCounter++
                            sh "echo $appStatus"
                            appStatus = getAppServerStatusCode()
                        }
                        if (appStatus != "200")
                            throw new Exception("Application failed to deploy new .war")
                    }
                }
            }
            post {
                failure {
                    withCredentials([
                        usernamePassword(credentialsId: 'dreamhost_cfsongs', usernameVariable: 'DREAMHOST_UN', passwordVariable: 'DREAMHOST_PW'),
                        string(credentialsId:'cfsongs_dns', variable: 'DNS')
                    ]) {
                        echo "Reverting app back to war $rollbackWar"
                        sh "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no 'cp $rollbackWar /home/$DREAMHOST_UN/$DNS/tomcat/webapps/ROOT.war'"
                    }
                }
            }
        }
    }
}
def getAppServerStatusCode(){
    return sh(script: "curl -s -o /dev/null -w '%{http_code}' https://$DNS/server-availability", returnStdout: true).trim()
}