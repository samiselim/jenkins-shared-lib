
import sami.packages.Docker
def call(String imageName){
    return new Docker(this).buildDockerImage(imageName)
}