package com.mtwas.workflow_service.dto;

import java.util.Map;


public record CreateWorkflowVersionRequest(Map<String, Object> definitionJson) {

}
