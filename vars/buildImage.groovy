def call(){
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'sami_docker_hub_credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t samiselim/demo-java-maven-app:${IMAGE_VERSION} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push samiselim/demo-java-maven-app:${IMAGE_VERSION}"
    }
}