import sami.packages.Docker
def call(String dockerHubCred){
    return new Docker(this).dockerLogin(dockerHubCred)
}