# Workflow Model

## Workflow Type

Directed Acyclic Graph (DAG)

Cycles are not allowed.

## Task Types

### MANUAL_TASK

Human approval or action.

Examples:
- Manager Approval
- Finance Approval

### HTTP_TASK

Calls external APIs.

Examples:
- Create Employee
- Create Jira Ticket

### CONDITION_TASK

Branching logic.

Examples:
- Amount > 10000

### DELAY_TASK

Wait before continuing.

Examples:
- Wait 24 Hours

### WEBHOOK_TASK

Send outbound event.

Examples:
- Notify External System

## Workflow States

- CREATED
- READY
- RUNNING
- COMPLETED
- FAILED
- CANCELLED

## Task States

- PENDING
- READY
- RUNNING
- COMPLETED
- FAILED
- RETRYING