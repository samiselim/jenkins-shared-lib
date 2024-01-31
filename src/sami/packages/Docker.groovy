package sami.packages

class Docker implements Serializable{

    def script

    Docker(script){
         this.script = script
    }
def dockerLogin(String dockerHubCred) {
    script.withCredentials([script.usernamePassword(credentialsId: "${dockerHubCred}", passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        script.sh 'git config --global user.email "jenkins@jenkins.com"'
        script.sh 'git config --global user.name "jenkins"'
        script.sh "echo $script.PASS | docker login -u $script.USER --password-stdin"
        // def name = script.sh(script: 'kubectl get secret | grep my-key | awk \'{print $1}\'', returnStdout: true).trim()
        
        // script.sh "echo $name"
        
        // if (name == "my-key") {
        //     script.sh 'echo my-keyalready found .. '
        // } else {
        //     script.sh "echo creating my-keysecret for K8s with dockerHub"
        //     script.sh "kubectl create secret docker-registry my-key\
        //     --docker-server=https://index.docker.io/v1/ \
        //     --docker-username=samiselim \
        //     --docker-password=${script.PASS} \
        //     --docker-email=samiselim75@gmail.com"
        // }
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
