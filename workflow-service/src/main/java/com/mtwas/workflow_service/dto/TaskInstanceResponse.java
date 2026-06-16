package com.mtwas.workflow_service.dto;

public record TaskInstanceResponse(Long id, String nodeId, String nodeType, String status) {

}
