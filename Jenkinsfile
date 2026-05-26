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
        DOCKER_HUB_CREDENTIALS = '0d7b5b67-aeef-4e51-b1a4-f1b32fb38ae2'
        // TODO: Replace with your Docker Hub repository, for example: 'your-dockerhub-username/teedy-app'.
        DOCKER_IMAGE = 'ronneywang/teedy2025_manual'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        CONTAINER_NAME = 'teedy-container-8081'
        HOST_PORT = '8081'
    }
    stages {
        stage('Clean') {
            steps {
                script {
                    runCommand('mvn clean')
                }
            }
        }
        stage('Compile') {
            steps {
                script {
                    runCommand('mvn compile')
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    runCommand('mvn test -Dmaven.test.failure.ignore=true')
                }
            }
        }
        stage('PMD') {
            steps {
                script {
                    runCommand('mvn pmd:pmd')
                }
            }
        }
        stage('JaCoCo') {
            steps {
                script {
                    runCommand('mvn jacoco:report')
                }
            }
        }
        stage('Javadoc') {
            steps {
                script {
                    runCommand('mvn javadoc:javadoc')
                }
            }
        }
        stage('Site') {
            steps {
                script {
                    runCommand('mvn site')
                }
            }
        }
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
                    docker.build("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}", '.')
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', env.DOCKER_HUB_CREDENTIALS) {
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push()
                        docker.image("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}").push('latest')
                    }
                }
            }
        }
        stage('Run Docker Container') {
            steps {
                script {
                    runCommandAllowFailure(
                        "docker stop ${env.CONTAINER_NAME} || true",
                        "docker stop %CONTAINER_NAME% || exit /b 0"
                    )
                    runCommandAllowFailure(
                        "docker rm ${env.CONTAINER_NAME} || true",
                        "docker rm %CONTAINER_NAME% || exit /b 0"
                    )
                    runCommand("docker run -d -p ${env.HOST_PORT}:8080 --name ${env.CONTAINER_NAME} ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/target/site/**/*.*', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.jar', fingerprint: true
            archiveArtifacts artifacts: '**/target/**/*.war', fingerprint: true
            junit '**/target/surefire-reports/*.xml'
        }
    }
}
