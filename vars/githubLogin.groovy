import sami.packages.Github
def call(String repoName,String githubCred){
    return new Github(this).githubLogin(repoName , githubCred)
}