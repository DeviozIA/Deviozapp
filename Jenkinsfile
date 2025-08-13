pipeline {
  agent any

  environment {
    DOCKERHUB_USER = 'kevinrojaswb'                 // <- cambia por tu usuario Docker Hub
    FRONTEND_IMAGE = "${DOCKERHUB_USER}/frontend-app"
    BACKEND_IMAGE  = "${DOCKERHUB_USER}/backend-app"
    TAG = "${env.BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps {
        // usa tu credencial SSH si clonas por SSH; o usa HTTPS si prefieres
        git branch: 'Dev_2', credentialsId: 'github-https', url: 'https://github.com/DeviozIA/Deviozapp.git'
      }
    }

    stage('Build images') {
      steps {
        bat "docker build -t ${FRONTEND_IMAGE}:${TAG} ./login-frontend"
        bat "docker build -t ${BACKEND_IMAGE}:${TAG} ./login-backend"
      }
    }

    stage('Login & Push to Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          bat 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
          bat "docker push ${FRONTEND_IMAGE}:${TAG}"
          bat "docker push ${BACKEND_IMAGE}:${TAG}"
          bat 'docker logout'
        }
      }
    }
  }

  post {
    success { echo "Imágenes construidas y subidas: ${FRONTEND_IMAGE}:${TAG} , ${BACKEND_IMAGE}:${TAG}" }
    failure { echo "Pipeline falló" }
  }
}
