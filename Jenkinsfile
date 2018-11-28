#!groovy
import groovy.json.JsonSlurperClassic

if (env.BRANCH_NAME == 'master') {
    properties([[$class  : 'BuildDiscarderProperty',
                 strategy: [$class               : 'LogRotator',
                            artifactDaysToKeepStr: '',
                            artifactNumToKeepStr : '10',
                            daysToKeepStr        : '',
                            numToKeepStr         : '10']
                ]
    ])
} else {
    properties([[$class  : 'BuildDiscarderProperty',
                 strategy: [$class               : 'LogRotator',
                            artifactDaysToKeepStr: '',
                            artifactNumToKeepStr : '2',
                            daysToKeepStr        : '',
                            numToKeepStr         : '2']
                ]])
}

pipeline {
    agent any

    stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Test') {
                steps('Pre-install') {
                   curl https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/5.1.1/flyway-commandline-5.1.1-linux-x64.tar.gz | tar xvz
                }

                steps {
                    sh './gradlew clean build'
                }
            }
        }
}