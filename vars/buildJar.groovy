def call(){
    echo "building the application for ${BRANCH_NAME}"
    sh 'mvn clean package' // to build just one jar file and deleting any jar files before building
}