# Lab 1 Debug the Delivery Drone

## Goal

Test SkyDrop as a user, locate three defects in the source code, write reproducible bug reports, and correct the defects.

## Scenario

SkyDrop delivers small parcels by drone. Version 0.1 is ready for a field test, but the test pilot has reported unexpected behavior. A real system could lose a parcel, violate a restricted area, or run out of battery.

## Setup

1. Open `app/skydrop.html` in a web browser.
2. Open the same file in a code editor such as VS Code.
3. Click a map cell to place an order.
4. Press **Launch** to start delivery.
5. After changing the code, save the file and refresh the browser.

The top-left cell is `(0, 0)`. The x-coordinate increases to the right and the y-coordinate increases downward. The flight log displays the coordinates of every order.

## Requirements under test

The complete requirements are in [`docs/requirements.md`](../../docs/requirements.md). This activity focuses on:

- **FR-2 First come first served:** Orders must be delivered in the order they were placed.
- **FR-3 No-fly zone:** Every cell in the red zone, including all edge cells, must be rejected.
- **FR-4 Range safety:** The battery must cover the complete trip from base to the order and back to base. The drone uses 5% battery per cell.

## Part A Black-box testing

Do not read the JavaScript yet.

1. Place at least three orders before pressing **Launch**.
2. Test the centre and edges of the red no-fly zone.
3. Test orders near, on, and beyond the dashed range boundary.
4. Record anything that does not satisfy FR-2, FR-3, or FR-4.

## Part B White-box debugging

Read the flight logic in `app/skydrop.html`. For each observed defect:

1. Identify the responsible line, expression, or function.
2. Explain why the code violates the requirement.
3. Make the smallest correction possible.
4. Refresh the browser and repeat the original test.
5. Run the other tests again to check for regressions.

## Part C Bug reports

Create one report for each defect with the repository's GitHub issue template. Each report should include:

- a concise title;
- the requirement ID;
- exact reproduction steps;
- the expected and actual results;
- the relevant line or function;
- the proposed fix;
- severity and justification; and
- the retest result.

## Discussion

1. Which defect would be most dangerous in a real drone?
2. Which defect would a customer notice first?
3. Why are boundary values important test cases?
4. Which defects could have been prevented by clearer requirements, code review, or unit tests?

## Bonus investigation

1. Place an order at `(11, 2)` and observe the flight path. Decide whether the behavior is a coding defect or a missing requirement.
2. In the browser console, run `addOrder({x: -3, y: 5})`. Explain what happens and what validation is missing.

Keep the corrected version. Later activities build on the same codebase.
