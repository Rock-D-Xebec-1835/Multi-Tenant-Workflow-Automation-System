package com.mtwas.workflow_service.validation.rule;

import java.util.List;

import com.mtwas.workflow_service.validation.WorkflowDsl;

public interface ValidationRule {
	void validate(WorkflowDsl workflow, List<String> errors);
}
