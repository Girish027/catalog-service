buildSuccess = true
Master_Build_ID = "1.1.0-${env.BRANCH_NAME}-${env.BUILD_NUMBER}"
grpId = "com.tfs.dp"
artId = "catalog-service"
packageType = "jar"
artifactName = "${artId}-${Master_Build_ID}.${packageType}"
archiveLocation = "./build/libs/dp2-Catalog_sanity-0.0.1-SNAPSHOT.jar"

mavenVersion="apache-maven-3.3.3"
nodejsVersion="node-v4.4.6-linux-x64"
grailsVersion="grails-2.5.0"
gradleVersion="gradle-2.3"

node
{
	env.JAVA_HOME = "${env.jdk8_home}"
	sh "${env.JAVA_HOME}/bin/java -version"
	echo "Current branch <${env.BRANCH_NAME}>"
	def workspace = env.WORKSPACE
	
	stage('Preparation') 
	{
		executeCheckout() 
	}
	if(env.CHANGE_ID)
	{
		stage('commit')
		{
			echo "pull request detected"
			buildSuccess = executeBuild()
			echo "buildSuccess = ${buildSuccess}"
			validateBuild(buildSuccess)
		}

	}
	if(!env.CHANGE_ID)
	{
		stage('sanity')
		{
			echo "push detected"
			buildSuccess = executeBuild()
			echo "buildSuccess = ${buildSuccess}"
			validateBuild(buildSuccess)
		}
		if(currentBuild.result != 'FAILURE')
		{
			stage('publish Test report to Jenkins')
			{

			}

		}
		if(currentBuild.result != 'FAILURE')
		{
			stage("Artifacts upload to nexus")
			{
			    sh  '''
				    mavenVersion='''+mavenVersion+'''
				    cd ${WORKSPACE}/build/libs
						REPO_URL=${NEXUS_REPO_URL_DEFAULT}
						REPO_ID=${NEXUS_REPO_ID_DEFAULT}
						GRP_ID='''+grpId+'''
						ART_ID='''+artId+'''
						PACKAGE_TYPE='''+packageType+'''
						ARTIFACT_NAME='''+artifactName+'''
						Master_Build_ID='''+Master_Build_ID+'''
						ZIP_FILE=$(ls *.$PACKAGE_TYPE)
						#ZIP_FILE='''+archiveLocation+'''
						/opt/${mavenVersion}/bin/mvn -B deploy:deploy-file -Durl=$REPO_URL -DrepositoryId=$REPO_ID -DgroupId=$GRP_ID -DartifactId=$ART_ID -Dversion=$Master_Build_ID -Dfile=$ZIP_FILE -Dpackaging=$PACKAGE_TYPE -DgeneratePom=true -e
						/opt/${mavenVersion}/bin/mvn -B deploy:deploy-file -Durl=$REPO_URL -DrepositoryId=$REPO_ID -DgroupId=$GRP_ID -DartifactId=$ART_ID -Dversion=latest -Dfile=$ZIP_FILE -Dpackaging=$PACKAGE_TYPE -DgeneratePom=true -e
				'''
			}
		}
	}
}

def boolean executeBuild()
{
	def result = true
	def branchName = env.BRANCH_NAME
	echo "branch = ${branchName} Master_Build_ID=${Master_Build_ID}"
			try 
			{
				sh '''	export JAVA_HOME=${jdk8_home}
				export PATH=${jdk8_home}/bin:$PATH
				mavenVersion='''+mavenVersion+'''
				nodejsVersion='''+nodejsVersion+'''
				grailsVersion='''+grailsVersion+'''
				gradleVersion='''+gradleVersion+'''
				
				export PATH=$PATH:/opt/${mavenVersion}/bin
				export PATH=/opt/${nodejsVersion}/bin:$PATH
				export PATH=/var/tellme/jenkins/tools/sbt/bin:$PATH
				export PATH=/opt/${grailsVersion}/bin:$PATH
				export PATH=/opt/${gradleVersion}/bin:$PATH
				BRANCH='''+branchName+'''
				#ADD YOUR BUILD STEPS HERE----------------------------------
				#/opt/${gradleVersion}/bin/gradle build -x test
				chmod +x ./gradlew
				./gradlew build -x test
					
					#-----------------------------------------------------------
				'''
				echo "Build Success...."
				result = true
			} 
			catch(Exception ex) 
			{
				 echo "Build Failed...."
				 echo "ex.toString() - ${ex.toString()}"
				 echo "ex.getMessage() - ${ex.getMessage()}"
				 echo "ex.getStackTrace() - ${ex.getStackTrace()}"
				 result = false
			} 
		
	
	echo "result - ${result}"
	result
}

def executeCheckout()
{
  //Get some code from a GitHub repository
  checkout scm
}

def validateBuild(def buildStatus)
{
	if (buildStatus) 
	{
		  currentBuild.result = 'SUCCESS'
	}
	else
	{
		currentBuild.result = 'FAILURE'
		 error "build failed!"
	}
	
}
