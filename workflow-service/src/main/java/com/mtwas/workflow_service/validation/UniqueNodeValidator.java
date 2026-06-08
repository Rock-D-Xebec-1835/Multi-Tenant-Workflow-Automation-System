package com.mtwas.workflow_service.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mtwas.workflow_service.validation.rule.ValidationRule;

public class UniqueNodeValidator implements ValidationRule{
	
	@Override
	public void validate(WorkflowDsl workflow, List<String> errors) {
		Set<String> nodeIds = new HashSet<>();
		
		for(NodeDsl node : workflow.nodes()) {
			if(!nodeIds.add(node.id())) {
				errors.add("Duplicate node id: " + node.id());
			}
		}
	}

}
