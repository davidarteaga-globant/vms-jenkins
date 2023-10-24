#!/usr/bin/env groovy
/*
* This one get a repo based on user password auth
* Warning: This auth method is gonna be deprecated on future
* 
*/
def call(String repo, String branch,pass){
        echo "getRepo-->call-->Pulling repo: ${repo}"
        git branch: branch, credentialsId: pass, url: repo
        echo "getRepo-->call-->Finishing pulling ${repo}"
}
return this