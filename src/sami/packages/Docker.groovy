package sami.packages

class Docker implements Serializable{

    def script

    Docker(script){
         this.script = script
    }
def dockerLogin(String dockerHubCred) {
    script.withCredentials([script.usernamePassword(credentialsId: "${dockerHubCred}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        def name = script.sh(script: 'kubectl get secret | grep S | awk \'{print $1}\'', returnStatus: true)
        script.sh "echo $name"

        if (name == 'secret-reg') {
            script.sh 'echo secret-reg already found .. '
        } else {
            script.sh '''echo creating secret-reg secret for K8s with dockerHub
                kubectl create secret docker-registry secret-reg \
                --docker-server=docker.io \
                --docker-username=samiselim \
                --docker-password=${script.USER}'''
        }
    }
}

    def buildDockerImage(String imageName){
        script.echo "building the docker image..."
        script.sh "docker build -t ${imageName}:${script.IMAGE_VERSION} ."
    }

    def dockerPush(String imageName) {
        script.sh "docker push ${imageName}:${script.IMAGE_VERSION}"
    }
}

class Github implements Serializable{

    def script

    Github(script){
        this.script = script
    }

    def githubLogin(String RepoName , String githubCred){
        script.echo "Logging into github repository"
        script.withCredentials([script.usernamePassword(credentialsId: "${githubCred}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.sh "git remote set-url origin https://${script.USER}:${script.PASS}@github.com/${script.USER}/${RepoName}"
        }
    }

    def gitHubAddAllChanges(){
        script.echo "Adding all changes to github repository"
        script.sh 'git add .'
    }

    def githubCommit(String CommitMessage) {
        script.echo "Committing changes to github repository"
        script.sh "git commit -m \"${CommitMessage}\""
    }
    def githubPush(){
        script.sh "git push origin HEAD:${script.BRANCH_NAME}"
    }
}