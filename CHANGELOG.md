# Changelog

## 0.3 Java Edition (Week 9)

- Added `skydrop-java/`, a Java Swing port of the simulator with the Lab 1 fixes (first come first served, no-fly zone edges, round-trip range check).
- Flight rules moved into `Mission.java` with no GUI code, so they can be unit tested.
- GUI additions: axis numbers, hover read-out of cell and battery cost, order entry by typed coordinates, result message for each order.
- Added JUnit 5 tests TC1 to TC3 in `MissionTest.java`.
- Added Lab 3 feature development and unit testing activity.
- Known bugs B1 to B5 are listed in Lab 3. B1 (off-map orders accepted) is left for students to fix.

## 0.1 (Week 3)

- First version of the drone delivery simulator: map, orders, battery, no-fly zone, flight log.
- Contains three known bugs used in Lab 1.
- Added Lab 2 requirements discovery and validation activity.
