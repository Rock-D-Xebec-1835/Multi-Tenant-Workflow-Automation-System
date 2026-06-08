package com.mtwas.workflow_service.validation;

import java.util.List;

public record WorkflowDsl(List<NodeDsl> nodes, List<EdgeDsl> edges) {

}
