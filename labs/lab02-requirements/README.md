# Lab 02: Requirements Discovery and Validation

## Goal

Run SkyDrop, identify five important functional requirements and five important non-functional requirements, and use evidence to decide whether the current system satisfies each requirement.

## Before you begin

Do not read `docs/requirements.md` or inspect the JavaScript until you complete your first black-box investigation. Begin as a user and infer the requirements from the system's purpose and behaviour.

## Run and investigate SkyDrop

1. Open `app/skydrop.html` in Chrome, Edge, or Firefox.
2. Place several delivery orders in different map locations.
3. Try the **Launch** control and observe the order queue, drone status, battery level, no-fly zone, range boundary, and flight log.
4. Test normal cases, boundary cases, and invalid or unsafe destinations.
5. Record what you expected, what happened, and any evidence shown by the interface.

## Requirement types

- A **functional requirement** states what the system must do.
- A **non-functional requirement** states how well the system must operate or a quality it must have, such as usability, reliability, performance, portability, or maintainability.

Write each requirement as a clear, testable statement beginning with **The system shall...**. Mark its status as **Yes**, **No**, **Partly**, or **Not testable**, and support the decision with evidence.

## Sample—not one of your ten requirements

| Type | Requirement | Test | Expected result | Evidence and status | Possible improvement |
|---|---|---|---|---|---|
| Functional | The system shall allow a user to place a delivery order by selecting an unrestricted map cell. | Select a normal cell outside the red zone. | An order marker and queue entry appear. | Record what you observe. | State a correction if the requirement is not fully met. |

## Five functional requirements

| ID | Testable requirement | Test performed | Expected result | Evidence and status | Possible solution |
|---|---|---|---|---|---|
| F1 |  |  |  |  |  |
| F2 |  |  |  |  |  |
| F3 |  |  |  |  |  |
| F4 |  |  |  |  |  |
| F5 |  |  |  |  |  |

## Five non-functional requirements

| ID | Quality category and testable requirement | Test or inspection | Acceptance criterion | Evidence and status | Possible solution |
|---|---|---|---|---|---|
| N1 |  |  |  |  |  |
| N2 |  |  |  |  |  |
| N3 |  |  |  |  |  |
| N4 |  |  |  |  |  |
| N5 |  |  |  |  |  |

## Evaluation questions

1. Which unmet or partly met requirement creates the greatest risk? Explain why.
2. Which improvement should be implemented first? Justify the priority.
3. Which non-functional requirement could not be evaluated from the interface alone? What additional evidence would you need?
4. After completing your own analysis, compare it with `docs/requirements.md`. Which requirements were missing, ambiguous, or difficult to test?

## Submission

Submit one completed requirements table containing exactly five functional and five non-functional requirements, followed by concise answers to the four evaluation questions. Your conclusions must refer to observed evidence rather than guesses.
