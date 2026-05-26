def runCommand(String command) {
    if (isUnix()) {
        sh command
    } else {
        bat command
    }
}

def runCommandAllowFailure(String unixCommand, String windowsCommand) {
    if (isUnix()) {
        sh unixCommand
    } else {
        bat windowsCommand
    }
}

pipeline {
    agent any
    environment {
        // TODO: Replace with the Jenkins credentials ID for your Docker Hub account.
        DOCKER_HUB_CREDENTIALS = 'docker-hub-credentials'
        // TODO: Replace with your Docker Hub repository, for example: 'your-dockerhub-username/teedy-app'.
        DOCKER_IMAGE = 'ronneywang/teedy2025_manual'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        K8S_DEPLOYMENT = 'teedy'
        K8S_CONTAINER = 'teedy'
    }
    stages {
        // stage('Clean') {
        //     steps {
        //         script {
        //             runCommand('mvn clean')
        //         }
        //     }
        // }
        // stage('Compile') {
        //     steps {
        //         script {
        //             runCommand('mvn compile')
        //         }
        //     }
        // }
        // stage('Test') {
        //     steps {
        //         script {
        //             runCommand('mvn test -Dmaven.test.failure.ignore=true')
        //         }
        //     }
        // }
        // stage('PMD') {
        //     steps {
        //         script {
        //             runCommand('mvn pmd:pmd')
        //         }
        //     }
        // }
        // stage('JaCoCo') {
        //     steps {
        //         script {
        //             runCommand('mvn jacoco:report')
        //         }
        //     }
        // }
        // stage('Javadoc') {
        //     steps {
        //         script {
        //             runCommand('mvn javadoc:javadoc')
        //         }
        //     }
        // }
        // stage('Site') {
        //     steps {
        //         script {
        //             runCommand('mvn site')
        //         }
        //     }
        // }
        stage('Package') {
            steps {
                script {
                    runCommand('mvn package -DskipTests')
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    runCommand("docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} .")
                    runCommand("docker tag ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ${env.DOCKER_IMAGE}:latest")
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: env.DOCKER_HUB_CREDENTIALS,
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        if (isUnix()) {
                            sh 'printf "%s" "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                        } else {
                            bat '@echo off\r\npowershell -NoProfile -Command "$env:DOCKER_PASS | docker login -u $env:DOCKER_USER --password-stdin"'
                        }
                        runCommand("docker push ${env.DOCKER_IMAGE}:latest")
                        runCommand("docker push ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                    }
                }
            }
        }
        stage('Load Image Into Minikube') {
            steps {
                script {
                    runCommand("minikube image load ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    runCommandAllowFailure(
                        "kubectl patch deployment ${env.K8S_DEPLOYMENT} --type=json -p='[{\"op\":\"replace\",\"path\":\"/spec/template/spec/containers/0/imagePullPolicy\",\"value\":\"IfNotPresent\"}]'",
                        "kubectl patch deployment %K8S_DEPLOYMENT% --type=json -p=\"[{\\\"op\\\":\\\"replace\\\",\\\"path\\\":\\\"/spec/template/spec/containers/0/imagePullPolicy\\\",\\\"value\\\":\\\"IfNotPresent\\\"}]\""
                    )
                    runCommand("kubectl set image deployment/${env.K8S_DEPLOYMENT} ${env.K8S_CONTAINER}=${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                    runCommand("kubectl rollout status deployment/${env.K8S_DEPLOYMENT}")
                    runCommand("kubectl get pods")
                    runCommand("kubectl get services")
                }
            }
        }
    }
    // post {
    //     always {
    //         archiveArtifacts artifacts: '**/target/site/**/*.*', fingerprint: true
    //         archiveArtifacts artifacts: '**/target/**/*.jar', fingerprint: true
    //         archiveArtifacts artifacts: '**/target/**/*.war', fingerprint: true
    //         junit '**/target/surefire-reports/*.xml'
    //     }
    // }
}
