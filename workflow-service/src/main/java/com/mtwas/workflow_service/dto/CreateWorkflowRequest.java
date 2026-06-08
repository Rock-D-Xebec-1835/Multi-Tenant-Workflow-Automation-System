package com.mtwas.workflow_service.dto;

public record CreateWorkflowRequest(
        String name,
        String description
) {}