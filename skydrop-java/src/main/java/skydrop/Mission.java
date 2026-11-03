package skydrop;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Flight logic for SkyDrop. This class has no GUI code, so every rule
 * here can be checked with a unit test (see MissionTest).
 */
public class Mission {

    // ----- Constants (see docs/requirements.md) -----
    public static final int COLS = 16;              // map width in cells
    public static final int ROWS = 10;              // map height in cells
    public static final double RATE = 5.0;          // battery % used per cell flown
    public static final double SPEED = 0.1;         // cells moved per simulation step
    public static final Cell BASE = new Cell(2, 5); // take-off and recharge point

    // No-fly zone corners (inclusive): x 7 to 9, y 2 to 4
    public static final int ZONE_X1 = 7, ZONE_Y1 = 2, ZONE_X2 = 9, ZONE_Y2 = 4;

    public enum State { IDLE, FLYING, CRASHED }

    /** One line in the flight log. {@code warning} lines are shown in red. */
    public record LogEntry(String text, boolean warning) { }

    private final Deque<Order> queue = new ArrayDeque<>();
    private final List<LogEntry> log = new ArrayList<>();
    private int nextId;
    private int delivered;
    private int rejected;

    private double droneX, droneY, battery;
    private State state;
    private Order currentOrder;
    private Cell target;

    public Mission() {
        reset();
    }

    // =====================================================================
    //  Rules (these are the methods you will unit test)
    // =====================================================================

    /** FR-3: true if the cell is inside the no-fly zone, edges included. */
    public boolean inNoFlyZone(Cell c) {
        return c.x() >= ZONE_X1 && c.x() <= ZONE_X2
            && c.y() >= ZONE_Y1 && c.y() <= ZONE_Y2;
    }

    /** Straight-line distance between two cells, in cells. */
    public static double distance(Cell a, Cell b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    /** FR-4: battery % needed to fly from base to the cell and back. */
    public double roundTripCost(Cell c) {
        return 2 * distance(BASE, c) * RATE;
    }

    /**
     * FR-1, FR-3, FR-4: check an order and, if it is safe, add it to the
     * end of the queue.
     */
    public Decision addOrder(Cell c) {
        if (inNoFlyZone(c)) {
            rejected++;
            log("Rejected " + c + ": no-fly zone", true);
            return Decision.REJECTED_NO_FLY_ZONE;
        }
        if (roundTripCost(c) > 100) {
            rejected++;
            log("Rejected " + c + ": out of range", true);
            return Decision.REJECTED_OUT_OF_RANGE;
        }
        Order order = new Order(nextId++, c);
        queue.addLast(order);
        log("Order #" + order.id() + " placed at " + c, false);
        return Decision.ACCEPTED;
    }

    // =====================================================================
    //  Flight simulation
    // =====================================================================

    /** Start the delivery run if the drone is waiting at base. */
    public void launch() {
        if (state == State.IDLE) {
            nextOrder();
        }
    }

    /** FR-2: take the oldest order first (first come, first served). */
    private void nextOrder() {
        if (queue.isEmpty()) {
            state = State.IDLE;
            log("All orders delivered. Drone at base.", false);
            return;
        }
        currentOrder = queue.pollFirst();
        target = currentOrder.cell();
        state = State.FLYING;
        log("Flying to order #" + currentOrder.id(), false);
    }

    /** FR-5: deliver, then return to base and recharge. */
    private void arrive() {
        if (target.equals(BASE)) {
            battery = 100;
            nextOrder();
        } else {
            delivered++;
            log("Delivered order #" + currentOrder.id(), false);
            target = BASE;
        }
    }

    /** Move the drone one small step. The GUI calls this about 60 times a second. */
    public void step() {
        if (state != State.FLYING) {
            return;
        }
        double dx = target.x() - droneX;
        double dy = target.y() - droneY;
        double d = Math.hypot(dx, dy);
        if (d <= SPEED + 1e-9) {
            droneX = target.x();
            droneY = target.y();
            arrive();
            return;
        }
        droneX += dx / d * SPEED;
        droneY += dy / d * SPEED;
        battery -= RATE * SPEED;
        if (battery <= 0) {
            battery = 0;
            state = State.CRASHED;
            log(String.format("CRASH: battery empty at (%.1f, %.1f)", droneX, droneY), true);
        }
    }

    /** Clear all orders and put a fully charged drone back at base. */
    public void reset() {
        queue.clear();
        log.clear();
        nextId = 1;
        delivered = 0;
        rejected = 0;
        droneX = BASE.x();
        droneY = BASE.y();
        battery = 100;
        state = State.IDLE;
        currentOrder = null;
        target = null;
        log("System ready.", false);
    }

    private void log(String text, boolean warning) {
        log.add(new LogEntry(text, warning));
    }

    // =====================================================================
    //  Getters used by the GUI and the tests
    // =====================================================================

    public List<Order> getQueue()     { return List.copyOf(queue); }
    public List<LogEntry> getLog()    { return Collections.unmodifiableList(log); }
    public int getDeliveredCount()    { return delivered; }
    public int getRejectedCount()     { return rejected; }
    public double getBattery()        { return battery; }
    public State getState()           { return state; }
    public double getDroneX()         { return droneX; }
    public double getDroneY()         { return droneY; }

    /** The order the drone is flying to, or null when it is returning or idle. */
    public Order getActiveOrder() {
        return (state == State.FLYING && target != null && !target.equals(BASE))
            ? currentOrder : null;
    }
}
