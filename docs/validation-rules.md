# Workflow Validation Rules

## Purpose

All workflow versions must pass validation before publication.

---

## Rule 1: Workflow Must Contain Nodes

A workflow must contain at least one node.

---

## Rule 2: Single START Node

A workflow must contain exactly one START node.

Valid:

```text
START -> A -> END
```

Invalid:

```text
START -> A
START -> B
```

---

## Rule 3: At Least One END Node

A workflow must contain at least one END node.

---

## Rule 4: Unique Node IDs

All node identifiers must be unique.

Invalid:

```text
Node A
Node A
```

---

## Rule 5: Edge References Must Be Valid

All edges must reference existing nodes.

Invalid:

```text
A -> B

Node B does not exist
```

---

## Rule 6: No Self-Loops

Invalid:

```text
A -> A
```

---

## Rule 7: No Cycles

Workflows must be Directed Acyclic Graphs.

Invalid:

```text
A -> B -> C -> A
```

---

## Rule 8: All Nodes Must Be Reachable

Every node must be reachable from START.

Invalid:

```text
START -> A

B
```

Node B is unreachable.

---

## Rule 9: Every Path Must Reach END

All execution paths must eventually terminate.

Invalid:

```text
START -> A
```

No END node reachable.

---

## Rule 10: CONDITION_TASK Validation

A CONDITION_TASK must define:

* TRUE path
* FALSE path

Example:

```text
Condition
   |
TRUE -> A
FALSE -> B
```

---

## Rule 11: Publish Validation

Only workflows that pass all validation rules may transition from DRAFT to PUBLISHED.
