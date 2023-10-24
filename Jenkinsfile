/*
  Este es un pipeline de tipo declarativo
  https://www.jenkins.io/doc/book/pipeline/

  TODO:scripted pipeline
*/
def getRepoToken
def FOLDER
def ABS_PATH
pipeline {
    agent any
    parameters {
        choice  choices: ["TodoPago", "BIMO", "default"],
                description: 'Ecosistema del proyecto en VMS',
                name: 'PROJECT'
            
        choice  choices: ["TP-Android", "TP-iOS", "TP-Comercio-iOS", "TP-Comercio-Android", "BIMO-iOS", "BIMO-Android","default"],
                description: 'Proyecto asociado en VMS',
                name: 'ECOSYSTEM'

        choice choices: ['TodoPago/TP-IOS-Comercio.git', 'TodoPago/TP-Android-Comercio.git', 'TodoPago/TP-ios.git', 'TodoPago/TP-Android.git', 'TodoPago/clarin-ios.git', 'TodoPago/clarin-android.git'],
                description: 'Github repo',
                name: 'REPO'

        choice choices: ['java', 'ios'],
                description: 'Language del repo',
                name: 'LANGUAGE'

        booleanParam defaultValue: true,
                description: 'VMS Upload',
                name: 'UPLOAD'

        booleanParam defaultValue: true,
                description: 'VMS Export',
                name: 'EXPORT'

        credentials credentialType: "Secret Text",
                defaultValue: 'vms-token',
                description: 'VMS Api key',
                name: 'VMS_API_KEY'

        credentials credentialType: 'Secret Text',
                defaultValue: 'github-token', //ID como se almaceno la cred en jenkins
                description: 'Token de Github',
                name: 'GITHUB_TOKEN',
                required: true
        
        //Deprecated method
        /*credentials credentialType: 'Username with password',
                    defaultValue: 'github-darteaga-TP',
                    description: 'Credential of Github',
                    name: 'CREDENTIAL_GITHUB',
                    required: true*/
    }
    stages {
        stage('Pipe Info') {
                steps {
                    script {
                        echo "<--Parameter Initialization-->"
                        echo """
                        The current parameters are:
                            VMS Ecosystem: ${params.ECOSYSTEM}
                            Ecosystem Project: ${params.PROJECT}
                            Repo to analyze: ${params.REPO}
                            Repo language: ${params.LANGUAGE}
                            VMS Upload: ${params.UPLOAD}
                            VMS Export: ${params.EXPORT}
                        """
                    }                      
                }
        }
        stage('Git clone repo') {
                steps {
                    script {
                        echo "<-- Start clonning git repo: ${params.REPO} -->"
                        getRepoToken = load('vms/JenkinsTemplate/getRepoToken.groovy')
                        getRepoToken.call(params.REPO, params.GITHUB_TOKEN, 'default')
                        //getRepo = load('vms/JenkinsTemplate/getRepo.groovy')
                        //getRepo.call(params.REPO, 'default', params.CREDENTIAL_GITHUB)
                    }
                }
        }
        /*stage('Setting up VMS docker container') {
            steps {
                script {
                        echo "Pulling up last VMS container --> Start"
                        sh 'docker pull vmsys/clivms:latest'
                        echo "Pulling up last VMS container --> End"
                        echo "Starting container --> Start"
                        def CODE = "${params.REPO}".split('/')
                        FOLDER = CODE[1].substring(0,CODE[1].length()-4)
                        ABS_PATH = "${WORKSPACE}/${FOLDER}"
                        echo "CODE: --->${ABS_PATH}"
                        sh """
                            docker run -dt --name vms \
                            -v ${ABS_PATH}:/CODE \
                            --add-host vms.globant.com:200.80.170.91 \
                            vmsys/clivms:latest \
                            /bin/bash
                        """
                }
            }
        }*/
        stage('Scanning code on VMS container') {
            steps {
                script {
                    echo "VMS Scanning --> Start"
                    withCredentials([string(credentialsId: env.VMS_API_KEY, variable: 'SECRET_VMS')]) {
                        sh """
                            docker exec vms \
                            python3 /app/main.py \
                            --path /CODE \
                            --language ${params.LANGUAGE} \
                            --export ${params.EXPORT} \
                            --vms_upload=${params.UPLOAD} \
                            --api_key ${SECRET_VMS} \
                            --ecosystem '${params.ECOSYSTEM}' \
                            --project ${params.PROJECT}
                        """
                    }
                    echo "VMS Scanning --> Stop"
                }
            }
        }
    }
    post {
            always {
                echo "Removing container"
                sh '''
                    docker stop vms
                    docker rm vms
                '''
                echo "Removing repo code"
                sh "rm -rf ${ABS_PATH}"
            }
        }
}
