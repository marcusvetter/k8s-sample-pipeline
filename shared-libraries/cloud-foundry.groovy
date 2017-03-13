// ---------------------------------------------------------------------------------------------------------------------
// SHARED PIPELINE LIBRARY: CLOUD FOUNDRY
// ---------------------------------------------------------------------------------------------------------------------

def deploy() {
    login()
    pushApp()
    logout()
}

// ---------------------------------------------------------------------------------------------------------------------
// PRIVATE METHODS
// ---------------------------------------------------------------------------------------------------------------------

private login() {
    withCredentials([[$class          : 'UsernamePasswordMultiBinding',
                      credentialsId   : 'cf-user-tmp',
                      usernameVariable: 'CLOUD_FOUNDRY_USERNAME',
                      passwordVariable: 'CLOUD_FOUNDRY_PASSWORD']]) {
        sh "cf login " +
                "-a \"https://api.eu-west-1.apps.msi.audi.com\" " +
                "-o \"ABI-3 INNO\" " +
                "-s \"pipeline-showcase\" " +
                "-u \"${env.CLOUD_FOUNDRY_USERNAME}\" " +
                "-p \"${env.CLOUD_FOUNDRY_PASSWORD}\" "
    }
}

private pushApp() {
    String buildpack = 'https://github.com/cloudfoundry/java-buildpack.git#v3.10'
    String pathToDeployable = 'build/libs/carservice-1.0.0.jar'
    String appName = 'carservice'
    String hostName = 'showcase-carservice'

    sh "cf push $appName -b $buildpack -p $pathToDeployable -n $hostName"
}

private logout() {
    sh 'cf logout'
}

return this
