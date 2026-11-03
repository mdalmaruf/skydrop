package skydrop;

/** A delivery order waiting in the queue. */
public record Order(int id, Cell cell) {

    @Override
    public String toString() {
        return "#" + id + "  ->  " + cell;
    }
}
