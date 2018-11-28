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
                    checkpoint 'Checkout Complete'
                }
            }

            stage('Build') {
                steps {
                    checkpoint 'Build Complete'
                }
            }

            stage('Archive') {
                steps {
                   checkpoint 'Archive Complete'
                }
            }
    }
}


