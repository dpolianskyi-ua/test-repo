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

node('test-repo') {
        stage('Checkout') {
            checkout scm
        }

        checkpoint 'Checkout Complete'

        stage('Build') {

        }

        checkpoint 'Build Complete'

        stage('Archive') {

        }

        checkpoint 'Archive Complete'
}