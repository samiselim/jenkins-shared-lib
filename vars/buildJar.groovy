def call(){
    echo "building the application..."
    sh 'mvn clean package' // to build just one jar file and deleting any jar files before building
}