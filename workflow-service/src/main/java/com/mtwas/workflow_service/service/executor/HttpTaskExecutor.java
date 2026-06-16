package com.mtwas.workflow_service.service.executor;

import com.mtwas.workflow_service.entity.TaskInstance;
import com.mtwas.workflow_service.validation.NodeDsl;

public interface HttpTaskExecutor {
	void execute(TaskInstance task, NodeDsl node);
}
