
pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'order-service'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-credentials',
                        usernameVariable: 'DOCKERHUB_USERNAME',
                        passwordVariable: 'DOCKERHUB_TOKEN'
                    )
                ]) {
                    sh '''
                        set -eu

                        export DOCKER_CONFIG=$(mktemp -d)

                        trap 'docker logout >/dev/null 2>&1 || true; rm -rf "$DOCKER_CONFIG"' EXIT

                        IMAGE_TAG=$(git rev-parse --short HEAD)

                        echo "Logging into Docker Hub..."

                        echo "$DOCKERHUB_TOKEN" | docker login \
                            -u "$DOCKERHUB_USERNAME" \
                            --password-stdin

                        echo "Building Docker image..."

                        docker build \
                            --pull \
                            -f order-service/Dockerfile \
                            -t "$DOCKERHUB_USERNAME/$DOCKERHUB_REPO:$BUILD_NUMBER" \
                            -t "$DOCKERHUB_USERNAME/$DOCKERHUB_REPO:$IMAGE_TAG" \
                            -t "$DOCKERHUB_USERNAME/$DOCKERHUB_REPO:latest" \
                            order-service

                        echo "Pushing Docker images..."

                        docker push "$DOCKERHUB_USERNAME/$DOCKERHUB_REPO:$BUILD_NUMBER"

                        docker push "$DOCKERHUB_USERNAME/$DOCKERHUB_REPO:$IMAGE_TAG"

                        docker push "$DOCKERHUB_USERNAME/$DOCKERHUB_REPO:latest"

                        echo "Docker images pushed successfully!"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo 'Docker image build and push completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check Jenkins console output.'
        }
    }
}