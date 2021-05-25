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
                    dir('song-catalog'){
                        sh "sshpass -p '$DREAMHOST_PW' ssh $DREAMHOST_UN@$DNS -o StrictHostKeyChecking=no '/home/$DREAMHOST_UN/scripts/backup_cfsongs.sh'"
                    }
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
    }
}