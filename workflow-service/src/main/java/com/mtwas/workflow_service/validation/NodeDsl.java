package com.mtwas.workflow_service.validation;

import java.util.Map;

public record NodeDsl(String id, String name, String type, Map<String, Object> config) {

}
