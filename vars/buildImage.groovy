def call(String imageName){
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'sami_docker_hub_credentials', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh "docker build -t ${imageName}:${IMAGE_VERSION} ."
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh "docker push ${imageName}:${IMAGE_VERSION}"
    }
}