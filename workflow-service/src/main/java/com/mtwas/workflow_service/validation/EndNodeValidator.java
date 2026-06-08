package com.mtwas.workflow_service.validation;

import java.util.List;

import com.mtwas.workflow_service.validation.rule.ValidationRule;

public class EndNodeValidator implements ValidationRule{
	
	@Override
	public void validate(WorkflowDsl workflow, List<String> errors) {
		long count = workflow.nodes()
							 .stream()
							 .filter(node -> "END".equals(node.type()))
							 .count();
		
		if(count < 1) {
			errors.add("Workflow must contain at least one END node");
		}
	}

}
