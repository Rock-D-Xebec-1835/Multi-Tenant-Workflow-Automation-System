package com.mtwas.workflow_service.validation;

import java.util.List;

public record ValidationResult(boolean valid, List<String> errors) {

}
