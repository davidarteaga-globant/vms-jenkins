#!/usr/bin/env groovy
/*
* This one get a REPO based on github user token
* TODO: Agregar bien el uso del branch, aca toma el default que se tenga configurado en el REPO *master por lo general
*/
def call(String REPO, String token, String branch){
        echo "getRepoToken-->call-->Pulling REPO: ${REPO}"
        withCredentials([string(credentialsId: token, variable: 'SECRET')]) {
                sh 'git clone https://$SECRET@github.com/$REPO'
        }
        echo "getRepoToken-->call-->Finishing pulling ${REPO}]"
}
return this