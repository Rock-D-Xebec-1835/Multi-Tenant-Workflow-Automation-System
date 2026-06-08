package com.mtwas.workflow_service.validation;

import java.util.List;

import com.mtwas.workflow_service.validation.rule.ValidationRule;


public class StartNodeValidator implements ValidationRule{
	@Override
	public void validate(WorkflowDsl workflow, List<String> errors) {
		long count = workflow.nodes()
					.stream()
					.filter(
						node -> "START".equals(node.type())
					)
					.count();
		
		if(count != 1) {
			errors.add("Workflow must contain exactly one START node");
		}
	}
}
