
pipeline {
    agent any

    tools {
        jdk 'JDK25'
        maven 'Maven3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out source code'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building Order Service'
                sh 'mvn -f order-service/pom.xml clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running unit and integration tests'
                sh 'mvn -f order-service/pom.xml test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging Order Service'
                sh 'mvn -f order-service/pom.xml package -DskipTests'
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'order-service/target/surefire-reports/*.xml',
                allowEmptyResults: true
            )
        }

        success {
            echo 'Order Service pipeline completed successfully!'
        }

        failure {
            echo 'Order Service pipeline failed!'
        }
    }
}