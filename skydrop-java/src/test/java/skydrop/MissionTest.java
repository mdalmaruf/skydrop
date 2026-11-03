package skydrop;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the flight rules in {@link Mission}.
 *
 * Every test follows the same three steps:
 *   Arrange  - create the objects and input
 *   Act      - call the one method under test
 *   Assert   - compare the actual result with the expected result
 */
class MissionTest {

    private Mission mission;

    @BeforeEach
    void setUp() {
        mission = new Mission();   // a fresh mission for every test, so tests never affect each other
    }

    // ------------------------------------------------------------------
    //  Worked examples (run these first, all three should pass)
    // ------------------------------------------------------------------

    @Test
    @DisplayName("TC1  FR-3  centre of the no-fly zone (8, 3) is restricted")
    void centreOfNoFlyZoneIsRestricted() {
        // Arrange
        Cell centre = new Cell(8, 3);

        // Act
        boolean restricted = mission.inNoFlyZone(centre);

        // Assert
        assertTrue(restricted, "(8, 3) is in the middle of the red zone");
    }

    @Test
    @DisplayName("TC2  FR-3  cell just left of the zone (6, 3) is allowed")
    void cellJustOutsideNoFlyZoneIsAllowed() {
        Cell outside = new Cell(6, 3);

        boolean restricted = mission.inNoFlyZone(outside);

        assertFalse(restricted, "(6, 3) is one cell left of the zone edge x = 7");
    }

    @Test
    @DisplayName("TC3  FR-4  order at (13, 5) needs 110% battery and is rejected")
    void orderBeyondRangeIsRejected() {
        Cell tooFar = new Cell(13, 5);   // 11 cells from base, so 22 cells there and back

        Decision decision = mission.addOrder(tooFar);

        assertEquals(Decision.REJECTED_OUT_OF_RANGE, decision);
        assertEquals(0, mission.getQueue().size(), "a rejected order must not enter the queue");
        assertEquals(1, mission.getRejectedCount());
    }

    // ------------------------------------------------------------------
    //  TC4  YOUR TURN (Part 5 of the lab)
    //  The bottom-right corner of the no-fly zone is (9, 4).
    //  Write a test that places an order there with addOrder() and checks
    //  that it is rejected with Decision.REJECTED_NO_FLY_ZONE and that the
    //  queue stays empty. Remove the comment marks and fill in the blanks.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("TC4  FR-3  corner of the zone (9, 4) is rejected")
    // void cornerOfNoFlyZoneIsRejected() {
    //     Cell corner = new Cell(__, __);
    //
    //     Decision decision = mission.addOrder(corner);
    //
    //     assertEquals(__________, decision);
    //     assertEquals(__, mission.getQueue().size());
    // }

    // ------------------------------------------------------------------
    //  TC5  NEW FEATURE (Part 6 of the lab)
    //  FR-7: an order outside the 16 x 10 map must be rejected with
    //  Decision.REJECTED_OFF_MAP. Write this test FIRST and watch it fail,
    //  then change Mission.addOrder() until it passes.
    // ------------------------------------------------------------------

    // @Test
    // @DisplayName("TC5  FR-7  order below the map (2, 10) is rejected")
    // void orderOffTheMapIsRejected() {
    //
    // }
}
