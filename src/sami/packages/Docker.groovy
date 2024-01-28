package sami.packages

class Docker implements Serializable{

    def script

    Docker(script){
         this.script = script
    }

    def dockerLogin(String dockerHubCred){
        def xcred = [:]
        script.withCredentials([script.usernamePassword(credentialsId: "${dockerHubCred}", passwordVariable: 'PASS', usernameVariable: 'USER')]){
            script.sh "echo $script.PASS | docker login -u $script.USER --password-stdin"
            xcred['PASS'] = script.PASS
            xcred['USER'] = script.USER
        }
        return xcred
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