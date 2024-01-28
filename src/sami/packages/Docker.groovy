package sami.packages

class Docker implements Serializable{

    def script

    Docker(script){
         this.script = script
    }
    def extractSecretName(grepOutput) {
        def matcher = (grepOutput =~ /\bsecret-reg\S*/)
        if (matcher.find()) {
            return matcher.group()
        }
        return null
    }
    def dockerLogin(String dockerHubCred){
        script.withCredentials([script.usernamePassword(credentialsId: "${dockerHubCred}", passwordVariable: 'PASS', usernameVariable: 'USER')]){
            script.sh "echo $script.PASS | docker login -u $script.USER --password-stdin"
            def secretName = extractSecretName(sh(script: "kubectl get secret | grep ${SECRET_NAME_PREFIX}", returnStdout: true).trim())
            if (secretName) {
                echo "Found secret: ${secretName}"
            } else {
                echo "No matching secret found with the prefix '${SECRET_NAME_PREFIX}'."
            }
           
            script.sh "kubectl create secret docker-registry secret-reg \
            --docker-server=docker.io \
            --docker-username=samiselim \
            --docker-password=${script.USER}"
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