def call(){
    echo "Committing changes to github repository"
    withCredentials([usernamePassword(credentialsId: 'sami_githubAcess', passwordVariable: 'PASS', usernameVariable: 'USER')]) {

        sh 'whoami'

        sh "git remote set-url origin https://${USER}:${PASS}@github.com/samiselim/java-maven-app.git"
        sh 'git add .'
        sh 'git commit -m "this commit from jenkins "'
        sh 'git push origin HEAD:jenkins-update'

    }
}