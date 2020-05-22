pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn compile'''
      }
    }

    stage('Start') {
      parallel {
        stage('Test Register') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_Register test'''
          }
        }

        stage('Test Login') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_Login test'''
          }
        }

        stage('Test Forgot Password') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_ForgotPassword test'''
          }
        }

      }
    }

    stage('Transaction') {
      parallel {
        stage('Test Internet Data List') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_PaketDataList test'''
          }
        }

        stage('Test Internet Data Purchase') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_PaketDataPurchase test'''
          }
        }

      }
    }

    stage('History') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_PaketDataHistory test'''
      }
    }

    stage('Personal Information') {
      parallel {
        stage('Test Edit Personal Information') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_EditUser test'''
          }
        }

        stage('Test Change Password') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_ChangePassword test'''
          }
        }

        stage('Test Logout') {
          steps {
            sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_Logout test'''
          }
        }

      }
    }

    stage('Test Delete Account') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_DeleteAccount test'''
      }
    }

  }
}