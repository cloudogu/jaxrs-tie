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

    stage('Set Version') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
      }
      steps {
        // read version from brach, set it and commit it
        sh "./gradlew setVersion -PnewVersion=${releaseVersion}"
        sh 'git add gradle.properties'
        commit "release version ${releaseVersion}"

        // fetch all remotes from origin
        sh 'git config "remote.origin.fetch" "+refs/heads/*:refs/remotes/origin/*"'
        sh 'git fetch --all'

        // checkout, reset and merge
        sh "git checkout main"
        sh "git reset --hard origin/main"
        sh "git merge --ff-only ${env.BRANCH_NAME}"

        // set tag
        tag releaseVersion
      }
    }

    stage('Check') {
      steps {
        sh './gradlew check'
        // update timestamp to avoid rerun tests again and fix junit-plugin:
        // ERROR: Test reports were found but none of them are new
        sh 'touch build/test-results/*/*.xml'
        junit 'build/test-results/*/*.xml'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build'
        archiveArtifacts artifacts: 'build/libs/*.jar'
      }
    }

    stage('Deployment') {
      when {
        anyOf(
          branch pattern: 'release/*', comparator: 'GLOB'
          branch 'develop'
        )
      }
      steps {
        withPublishEnvironment {
          sh './gradlew publish'
        }
      }
    }

    stage('Update Repository') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
      }
      steps {
        // merge main in to develop
        sh 'git checkout develop'
        sh 'git merge main'

        // set version to next development iteration
        sh './gradlew setVersionToNextSnapshot'
        sh 'git add gradle.properties'
        commit 'prepare for next development iteration'

        // push changes back to remote repository
        authGit 'push origin main --tags'
        authGit 'push origin develop --tags'
        authGit "push origin :${env.BRANCH_NAME}"
      }
    }

  }

}

void withPublishEnvironment(Closure<Void> closure) {
  withCredentials([
    usernamePassword(credentialsId: 'mavenCentral-acccessToken', usernameVariable: 'ORG_GRADLE_PROJECT_sonatypeUsername', passwordVariable: 'ORG_GRADLE_PROJECT_sonatypePassword'),
    file(credentialsId: 'oss-gpg-secring', variable: 'GPG_KEY_RING'),
    usernamePassword(credentialsId: 'oss-keyid-and-passphrase', usernameVariable: 'GPG_KEY_ID', passwordVariable: 'GPG_KEY_PASSWORD')
  ]) {
    closure.call()
  }
}

void commit(String message) {
  sh "git -c user.name='CES Marvin' -c user.email='cesmarvin@cloudogu.com' commit -m '${message}'"
}

void tag(String version) {
  String message = "Release version ${version}"
  sh "git -c user.name='CES Marvin' -c user.email='cesmarvin@cloudogu.com' tag -m '${message}' ${version}"
}

String getReleaseVersion() {
  return env.BRANCH_NAME.substring("release/".length());
}

void isBuildSuccess() {
  return currentBuild.result == null || currentBuild.result == 'SUCCESS'
}

void authGit(String credentials, String command) {
  withCredentials([
    usernamePassword(credentialsId: credentials, usernameVariable: 'AUTH_USR', passwordVariable: 'AUTH_PSW')
  ]) {
    sh "git -c credential.helper=\"!f() { echo username='\$AUTH_USR'; echo password='\$AUTH_PSW'; }; f\" ${command}"
  }
}
