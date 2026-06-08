package com.mtwas.workflow_service.dto;

public record WorkflowResponse(
	Long id,
	String name,
	String description,
	Integer activeVersion
) {}
