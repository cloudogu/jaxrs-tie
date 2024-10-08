/*
 * MIT License
 *
 * Copyright (c) 2021, Cloudogu GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

pipeline {

  agent {
    docker {
      image 'scmmanager/java-build:17.0.9_9'
      label 'scmm'
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
        // fetch all remotes from origin
        sh "git tag -d ${releaseVersion} || true"
        sh 'git config "remote.origin.fetch" "+refs/heads/*:refs/remotes/origin/*"'
        sh 'git fetch --all'

        sh "git checkout ${env.BRANCH_NAME}"
        sh "git reset --hard origin/${env.BRANCH_NAME}"

        // read version from brach, set it and commit it
        sh "./gradlew setVersion -PnewVersion=${releaseVersion}"
        sh 'git add gradle.properties'
        commit "release version ${releaseVersion}"

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
        branch pattern: 'release/*', comparator: 'GLOB'
        expression { return isBuildSuccess() }
      }
      steps {
        withPublishEnvironment {
          sh './gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository'
        }
      }
    }

    stage('Update Repository') {
      when {
        branch pattern: 'release/*', comparator: 'GLOB'
        expression { return isBuildSuccess() }
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
        authGit 'cesmarvin', 'push origin main --tags'
        authGit 'cesmarvin', 'push origin develop --tags'
        authGit 'cesmarvin', "push origin :${env.BRANCH_NAME}"
      }
    }

  }

}

void withPublishEnvironment(Closure<Void> closure) {
  withCredentials([
    usernamePassword(credentialsId: 'mavenCentral-acccessToken', usernameVariable: 'ORG_GRADLE_PROJECT_sonatypeUsername', passwordVariable: 'ORG_GRADLE_PROJECT_sonatypePassword'),
    file(credentialsId: 'mavenCentral-secretKey-asc-file', variable: 'GPG_KEY_RING'),
    usernamePassword(credentialsId: 'mavenCentral-secretKey-Passphrase-with-keyId', usernameVariable: 'GPG_KEY_ID', passwordVariable: 'GPG_KEY_PASSWORD')
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
