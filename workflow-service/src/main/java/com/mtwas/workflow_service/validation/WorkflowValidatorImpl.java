package com.mtwas.workflow_service.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtwas.workflow_service.validation.rule.ValidationRule;


@Service
public class WorkflowValidatorImpl implements WorkflowValidator{
	private final ObjectMapper objectMapper;
	private final List<ValidationRule> validationRules;
	
	public WorkflowValidatorImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.validationRules = List.of(
					new StartNodeValidator(),
					new EndNodeValidator(),
					new UniqueNodeValidator(),
					new EdgeReferenceValidator()
				);
	}
	
	@Override
	public ValidationResult validate(String workflowJson) {
		List<String> errors = new ArrayList<>();
		
		try {
			WorkflowDsl workflow = objectMapper.readValue(workflowJson, WorkflowDsl.class);
			for(ValidationRule rule : validationRules) {
				rule.validate(workflow, errors);
			}
		}catch (Exception ex) {
			errors.add("Invalid workflow JSON" + ex.getMessage());
		}
		
		return new ValidationResult(errors.isEmpty(),errors);
	}
}


