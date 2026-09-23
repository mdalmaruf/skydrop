# SkyDrop Requirements (version 0.1)

Each requirement has an ID so that code, tests, and bug reports can refer to it (traceability).

## Functional requirements

| ID | Requirement |
|---|---|
| FR-1 | The user places a delivery order by clicking a cell on the map. |
| FR-2 | Orders are delivered first come, first served (in the order they were placed). |
| FR-3 | Any order inside the no-fly zone, including its edge cells, is rejected. |
| FR-4 | An order is accepted only if the battery can cover the whole trip, there and back. |
| FR-5 | After each delivery, the drone returns to base and recharges to 100%. |
| FR-6 | The flight log records every order, delivery, rejection, and crash. |

## Constants

| Name | Value | Meaning |
|---|---|---|
| Map | 16 x 10 cells | Flight area |
| Base | (2, 5) | Take-off and recharge point |
| No-fly zone | x 7 to 9, y 2 to 4 | Restricted area |
| Battery use | 5% per cell | Energy model |

## Known gaps (to be addressed in later labs)

These are intentionally left open for the requirements lab:

- Nothing says the drone must avoid flying *through* the no-fly zone.
- There is no safety reserve: the drone may land with 0% battery.
- Input to `addOrder()` is not validated (orders off the map are possible).
- There is only one drone and no order priority.
