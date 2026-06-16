package com.mtwas.workflow_service.service.executor;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.mtwas.workflow_service.entity.TaskInstance;
import com.mtwas.workflow_service.service.context.ExecutionContextService;
import com.mtwas.workflow_service.validation.NodeDsl;

@Service
public class HttpTaskExecutorImpl implements HttpTaskExecutor {
	
	private final RestClient restClient;
	private final ExecutionContextService executionContextService;
	
	public HttpTaskExecutorImpl(RestClient restClient, ExecutionContextService executionContextService) {
		this.restClient = restClient;
		this.executionContextService = executionContextService;
	}
	
	@Override
	public void execute(TaskInstance task, NodeDsl node) {
		String method = (String) node.config().get("method");
		String url = (String) node.config().get("url");
		if(!"GET".equalsIgnoreCase(method)) {
			throw new RuntimeException("Only GET is supported currently");
		}
		
		String response = restClient.get().uri(url).retrieve().body(String.class);
		
		Map<String, Object> context = executionContextService.getContext(task.getWorkflowInstance());
		
		context.put(node.id() + "Response", response);
		
		executionContextService.updateContext(task.getWorkflowInstance(), context);
		
		System.out.println(response);
	}
	
}
