package com.mtwas.workflow_service.validation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.mtwas.workflow_service.validation.rule.ValidationRule;

public class EdgeReferenceValidator implements ValidationRule{
	
	@Override
	public void validate(WorkflowDsl workflow, List<String> errors) {
		Set<String> nodeIds = workflow.nodes()
									  .stream()
									  .map(node -> node.id())
									  .collect(Collectors.toSet());
		
		for(EdgeDsl edge : workflow.edges()) {
			if(!nodeIds.contains(edge.from())) {
				errors.add("Invalid edge source: " + edge.from());
			}
			if(!nodeIds.contains(edge.to())) {
				errors.add("Invalid edge target: " + edge.to());
			}
		}
	}
}
