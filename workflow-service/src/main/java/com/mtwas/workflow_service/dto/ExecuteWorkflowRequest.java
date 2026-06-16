package com.mtwas.workflow_service.dto;

import java.util.Map;

public record ExecuteWorkflowRequest(Map<String, Object> context) {

}
