package com.mtwas.workflow_service.dto;

import java.time.LocalDateTime;

public record ExecutionResponse(Long id, Long workflowId, Integer workflowVersion, String status, LocalDateTime startedAt, LocalDateTime completedAt, String executionContext) {

}
