import sami.packages.Github
def call(){
    return new Github(this).gitHubAddAllChanges()
}