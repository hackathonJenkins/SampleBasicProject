@Library('sfci-pipeline-sharedlib@master') _

import net.sfdc.dci.LogUtil
def envDef = [
        emailTo: 'arun.kaushik@salesforce.com',
        stopSuccessEmail: true
]

env.SFCI_LOG_LEVEL = 'INFO' 
env.RELEASE_BRANCHES = ['master']
env.GUS_TEAM_NAME = 'SFCI [CI Apps and Workflow]'
executePipeline(envDef) {

    def maven_args = env.maven_args ?: ''
    def settings_xml = env.settings_xml != null ? config.settings_xml : '.m2/settings.xml'

	stage('Init') {
		LogUtil.info(this,"About to checkout the code from GIT")
		checkout scm
		LogUtil.info(this,"Code checked out the code from GIT")
		mavenInit()
	}

    stage('BuildWithCodeCoverage-Unit'){
        LogUtil.info(this,"Starting MavenBuildwithCodeCoverage")
        mavenBuildWithCodeCoverage(maven_args: '-DskipITs', test_suite:'unit', dev_gus_upload: true)
    }

    stage('BuildWithCodeCoverage-Functional'){
        LogUtil.info(this,"Starting MavenBuildwithCodeCoverage")
        mavenBuildWithCodeCoverage(maven_args: '-DskipSurefire=true', test_suite:'functional', dev_gus_upload: true)
    }
}
