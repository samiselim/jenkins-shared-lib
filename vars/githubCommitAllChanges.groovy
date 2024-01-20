import sami.packages.Github

def call(String CommitMessage){
    return new Github(this).githubCommit(CommitMessage)
}