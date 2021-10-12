#!groovy
pipeline {

  agent {
    docker {
      image 'openjdk:11'
      label 'docker'
    }
  }

  environment {
    HOME = "${env.WORKSPACE}"
  }

  stages {

    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }

    stage('Deployment') {
      when {
        // branch pattern: 'release/*', comparator: 'GLOB'
        branch 'main'
      }
      steps {
        withPublishEnivronment {
          sh './gradlew publish'
        }
      }
    }

  }

}

void withPublishEnivronment(Closure<Void> closure) {
  withCredentials([
    usernamePassword(credentialsId: 'mavenCentral-acccessToken', usernameVariable: 'ORG_GRADLE_PROJECT_sonatypeUsername', passwordVariable: 'ORG_GRADLE_PROJECT_sonatypePassword'),
    file(credentialsId: 'mavenCentral-secretKey-asc-file', variable: 'GPG_KEY_RING'),
    string(credentialsId: 'mavenCentral-secretKey-Passphrase', variable: 'GPG_KEY_PASSWORD')
  ]) {
      withEnv(['GPG_KEY_ID=0x7BEEB938AB585959']) {
        closure.call()
      }
  }
}
