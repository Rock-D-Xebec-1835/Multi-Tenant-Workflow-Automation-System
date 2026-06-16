package com.mtwas.workflow_service.validation;

import java.util.List;

public record WorkflowDsl(String workflowId, String name, List<NodeDsl> nodes, List<EdgeDsl> edges) {

}
