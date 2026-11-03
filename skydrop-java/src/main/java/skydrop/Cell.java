package skydrop;

/**
 * One square on the 16 x 10 map. The top-left cell is (0, 0);
 * x grows to the right and y grows downward.
 */
public record Cell(int x, int y) {

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
