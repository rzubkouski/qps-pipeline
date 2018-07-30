package com.qaprosoft.jenkins.repository.jobdsl.factory.job

import groovy.transform.InheritConstructors

//TODO: remove this class 

@InheritConstructors
public class BuildJobFactory extends JobFactory {

	def create() {
		def job = super.create()
		job.with {
			parameters {
				booleanParam('parameterIsHere', true, 'First factory parameter')
			}
		}
		return job
	}
}