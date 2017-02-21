def cloudFoundry

def setup() {
    cloudFoundry = load("shared-libraries/cloud-foundry.groovy")
}

def execute() {
    node('k8s-gradle') {

        stage('Checkout') {
            checkout scm
        }

        stage('Build and unit test') {
            sh 'gradle clean build'
            stash(name: 'deployable', includes: "build/libs/*")
        }

    }

    node('k8s-cf') {

        stage('Deploy to cloud foundry') {
            unstash(name: 'deployable')
            cloudFoundry.deploy()
        }

    }
}

setup()

return this