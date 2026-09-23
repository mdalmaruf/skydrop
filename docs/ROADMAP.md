# SkyDrop Roadmap

SkyDrop grows through a suggested sequence of activities. Each activity adds one software engineering practice to the same codebase. The sequence can be adapted to different classes or training programs.

## Course track

| Week | Topic | Lab activity | Output in the repo |
|---|---|---|---|
| 3 | Project planning | **Lab 1: Debugging.** Find and fix three seeded bugs; write bug reports. | Bug reports as GitHub issues; version 0.2 |
| 4 | Requirements | Write user stories and acceptance criteria for priority orders and a 20% battery reserve. Write the missing "do not fly through the no-fly zone" requirement. | `docs/requirements.md` v2 |
| 5 | UML and design | Class diagram (Drone, Order, Map, Mission) and a state diagram (IDLE, FLYING, CRASHED). Sequence diagram for "place order". | `docs/design/` |
| 7 | Architecture | Split `app/skydrop.html` into model, view, and controller files (MVC). | `app/model.js`, `app/view.js`, `app/controller.js` |
| 8 | Clean code and Git | Feature branches, pull requests, and peer code review using `CONTRIBUTING.md`. | Reviewed pull requests |
| 9 | Testing | Unit tests for `inNoFlyZone()` and `addOrder()` using boundary values. Test plan. | `tests/` |
| 10 | CI/CD | GitHub Actions runs the tests on every push; badge in README. | `.github/workflows/test.yml` |
| 11 | Security | Validate all order input; discuss what could go wrong if orders came from a network API. | Input validation and tests |
| 12 | Maintenance | Path planning around the no-fly zone; add a second drone; refactor. | Version 1.0 |
| 13 | AI-assisted SE | Ask an AI tool to implement a feature, then review, test, and document what it got wrong. | Review report |

## Research track (possible directions)

SkyDrop is small but has the main features of a cyber-physical system: a physical model (battery, position), safety rules (no-fly zone, range), and a control loop. This makes it useful as a research testbed.

1. **Seeded-fault benchmark.** Each version keeps a known set of bugs with their fixes, requirement IDs, and bug reports. This can become a small benchmark for evaluating automated bug detection and repair (including LLM-based tools) on safety-relevant CPS code.
2. **Requirements to runtime monitors.** Turn FR-3 and FR-4 into runtime safety monitors and study how many seeded faults they catch compared with unit tests.
3. **Human vs. AI debugging.** Compare how students and AI tools find and explain the same seeded bugs.
4. **Toward real hardware.** Port the flight logic to a drone simulator or embedded target and study how the software engineering artifacts (requirements, tests, monitors) transfer.

Any study that uses student work, bug reports, or survey data needs research ethics board (REB) approval and student consent before data collection.
