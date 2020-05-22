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

    stage('Register') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_Register test'''
      }
    }

    stage('Forgot Password') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_ForgotPassword test'''
      }
    }

    stage('Login') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_Login test'''
      }
    }

    stage('List Internet Data') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_InternetDataList test'''
      }
    }

    stage('Purchase Internet Data') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_InternetDataPurchase test'''
      }
    }

    stage('History') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_TransactionHistory test'''
      }
    }

    stage('Change Password') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_ChangePassword test'''
      }
    }

    stage('Edit Personal Information') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_EditUser test'''
      }
    }

    stage('Logout') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_Logout test'''
      }
    }

    stage('Delete Account') {
      steps {
        sh '''export M2_HOME=/usr/local/Cellar/maven/3.6.3_1/libexec
export PATH=$PATH:$M2_HOME/bin
mvn -Dtest=TC_DeleteAccount test'''
      }
    }

  }
}