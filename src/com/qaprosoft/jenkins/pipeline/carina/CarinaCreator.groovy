package com.qaprosoft.jenkins.pipeline

import com.qaprosoft.jenkins.pipeline.Creator
import com.qaprosoft.jenkins.pipeline.Scanner
import com.qaprosoft.scm.github.GitHub;

class CarinaCreator extends Creator {

    public CarinaCreator(context) {
        super(context)
        this.context = context

        scmClient = new GitHub(context)
        scanner = new Scanner(context)
    }

    //Events
    @Override
    protected void onUpdate() {
        context.println("CarinaCreator->onUpdate")
        // handle each push/merge operation
        // execute logic inside this method only if $REPO_HOME/Jenkinsfile was updated
        context.println("TODO: implement snapshot build generation and emailing build number...")
    }

    @Override
    protected void onPullRequest() {
        context.println("CarinaCreator->onPullRequest")
        context.node("master") {
            scmClient.clonePR()
            def goals = "-Dcobertura.report.format=xml cobertura:cobertura clean test javadoc:javadoc"
            //executeMavenGoals(goals)
            context.junit '**/target/surefire-reports/junitreports/*.xml'
            context.step([$class: 'CoberturaPublisher',
                  autoUpdateHealth: false,
                  autoUpdateStability: false,
                  coberturaReportFile: '**/target/site/cobertura/coverage.xm',
                  failUnhealthy: false,
                  failUnstable: false,
                  maxNumberOfBuilds: 0,
                  onlyStable: false,
                  sourceEncoding: 'ASCII',
                  zoomCoverageChart: false])
			//TODO: investigate howto use creds functionality in jenkins 

/*			def nicePasswordBro;
			context.withCredentials([context.usernamePassword(credentialsId:'gpg_token', passwordVariable:'PASSWORD', usernameVariable:'USER')]) {
			   nicePasswordBro = context.env.getEnvironment().get("PASSWORD")
			   context.echo "${nicePasswordBro}" // password is masked
			   context.echo context.env.getEnvironment().dump()
			}
			context.echo nicePasswordBro
			
			context.environment {
				GPG_TOKEN = context.credentials("gpg_token")
				context.echo context.env.getEnvironment().dump()
				context.println("GPG: " + context.env.getEnvironment().get("GPG_TOKEN_PSW") )
			}
			context.echo context.env.getEnvironment().dump()
			context.println("GPG2: " + context.env.getEnvironment().get("GPG_TOKEN_PSW") )

			*/

			//TODO: implement below code
			// produce snapshot build if ghprbPullTitle contains 'build-snapshot'
			
            if (Configuration.get("ghprbPullTitle").contains("build-snapshot")) {
				executeMavenGoals("versions:set -DnewVersion=${context.env.getEnvironment().get("CARINA_RELEASE")}.${context.env.getEnvironment().get("BUILD_NUMBER")}-SNAPSHOT")
				executeMavenGoals("-Dcobertura.report.format=xml cobertura:cobertura clean deploy javadoc:javadoc")
            }
            //email notification
        }
        //TODO: publish cobertura report
        //TODO: send email about unit testing results
    }

}