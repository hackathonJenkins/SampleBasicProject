/* Before using this example, check if the below release tag(v0.3.0) is the latest.
 * You can find the latest release tag at https://git.soma.salesforce.com/dci/sfci-pipeline-sharedlib/releases
 */
@Library('sfci-pipeline-sharedlib@v0.9.1') _

/* import is required only if you use one of the classes specified here:
 * https://git.soma.salesforce.com/dci/sfci-pipeline-sharedlib#classes
 */
import net.sfdc.dci.BuildUtils

/* Specify the build image you want to use.
 * Not specifying a build image will use the default build image specified here:
 * https://git.soma.salesforce.com/dci/sfci-pipeline-sharedlib/blob/master/vars/executePipeline.groovy#L4
 */
def envDef = [ buildImage: 'ops0-artifactrepo1-0-prd.data.sfdc.net/dci/centos-sfci-maven:0891c85' ]

// define any release branches here
env.RELEASE_BRANCHES = ['master']

/*
 * Defining Pipline steps 
 */
executePipeline(envDef) {
	/*
	 * first stage to clone the source code and do the inital setup
	 */
    stage('Init') {
		/*
		 * This step will check out the github project defined in the pipeline configuration
		 */
        checkout scm
		
		/*
		 * This will execute https://git.soma.salesforce.com/dci/sfci-pipeline-sharedlib/blob/master/vars/mavenInit.groovy which is included as part of shared lib.
		 * This is suppose to do the basic setup for the maven settings.xml file i.e remote repo , authentication etc.
		 */
        mavenInit()
    }

	/*
	 * second stage is basically to compile, build , test and generate the coverage reports
	 * it will use clover plugin to generate report. Below code snippet should be present in your project pom.xml to generate the reports
	 * <plugin>
	 *		<groupId>com.atlassian.maven.plugins</groupId>
	 *		<artifactId>clover-maven-plugin</artifactId>
	 *			<version>4.1.1</version>
	 *			<configuration>
	 *				<generateHtml>true</generateHtml>
	 *				<generateXml>true</generateXml>
	 *			</configuration>
	 * </plugin>
	 *
	 * more information about the plugin can be found @ https://wiki.jenkins.io/display/JENKINS/Clover+Plugin
	 */
    stage('Build') {
		/*
		 * Below are the different step happening in the below code :
		 * 1. clean the project
		 * 2. Instrumentation of the code to support code coverage
		 * 3. Execute the tests written in the code
		 * 4. Aggregate the reports generate in the previouse step
		 * 5. generate code coverage reports in various format e.t HTML, PDF
		 */
        sh "mvn --settings .m2/settings.xml clean  clover:setup test clover:aggregate clover:clover --batch-mode --fail-at-end --strict-checksums --update-snapshots -Dmaven.repo.local=.m2/repository -DaltDeploymentRepository=zen::default::file://${workspace}/target/deploy"
        //mavenBuild([maven_args: 'clean  clover:setup test clover:aggregate clover:clover'])
    }
    stage('Archive stage'){
        archiveArtifacts 'target/site/clover/*.xml'
    }
    stage('Publish Code Coverage Report') {
       step([
           $class: 'CloverPublisher',
           cloverReportDir: 'target/site/clover',
           cloverReportFileName: 'clover.xml',
           healthyTarget: [methodCoverage: 70, conditionalCoverage: 80, statementCoverage: 80], // optional, default is: method=70, conditional=80, statement=80
           unhealthyTarget: [methodCoverage: 0, conditionalCoverage: 0, statementCoverage: 0], // optional, default is none
           failingTarget: [methodCoverage: 0, conditionalCoverage: 0, statementCoverage: 0]     // optional, default is none
       ])
   }
}
