package skydrop;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.function.Consumer;
import javax.swing.JPanel;

/** Draws the map, the no-fly zone, the range circle, the orders and the drone. */
public class MapPanel extends JPanel {

    static final int CELL = 40;
    private static final int LEFT = 30;   // room for y-axis labels
    private static final int TOP = 24;    // room for x-axis labels
    private static final int W = Mission.COLS * CELL;
    private static final int H = Mission.ROWS * CELL;

    private static final Color GRID = new Color(0xE3E7EE);
    private static final Color AXIS = new Color(0x6B7489);
    private static final Color ZONE_FILL = new Color(220, 53, 69, 60);
    private static final Color ZONE_EDGE = new Color(0xB02A37);
    private static final Color RANGE = new Color(0x3B7DD8);
    private static final Color NAVY = new Color(0x16324F);
    private static final Color ORDER = new Color(0xE0952B);
    private static final Color HOVER = new Color(0, 0, 0, 22);

    private final Mission mission;
    private Cell hover;

    public MapPanel(Mission mission, Consumer<Cell> onClick, Consumer<Cell> onHover) {
        this.mission = mission;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(LEFT + W + 10, TOP + H + 10));

        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                Cell c = toCell(e);
                if (c != null) onClick.accept(c);
            }
            @Override public void mouseMoved(MouseEvent e) {
                hover = toCell(e);
                onHover.accept(hover);
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                hover = null;
                onHover.accept(null);
                repaint();
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    /** Convert a mouse position to a map cell, or null if outside the map. */
    private Cell toCell(MouseEvent e) {
        int x = Math.floorDiv(e.getX() - LEFT, CELL);
        int y = Math.floorDiv(e.getY() - TOP, CELL);
        if (x < 0 || y < 0 || x >= Mission.COLS || y >= Mission.ROWS) return null;
        return new Cell(x, y);
    }

    private static int px(double v) {
        return (int) Math.round(v * CELL + CELL / 2.0);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawAxes(g);
        g.translate(LEFT, TOP);
        Shape oldClip = g.getClip();
        g.clipRect(0, 0, W + 1, H + 1);   // anything off the map is not drawn

        drawGrid(g);
        drawZone(g);
        drawRange(g);
        drawBase(g);
        drawOrders(g);
        drawDrone(g);

        g.setClip(oldClip);
        g.setColor(AXIS);
        g.drawRect(0, 0, W, H);
        g.dispose();
    }

    private void drawAxes(Graphics2D g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        g.setColor(AXIS);
        FontMetrics fm = g.getFontMetrics();
        for (int x = 0; x < Mission.COLS; x++) {
            String s = String.valueOf(x);
            g.drawString(s, LEFT + x * CELL + (CELL - fm.stringWidth(s)) / 2, TOP - 8);
        }
        for (int y = 0; y < Mission.ROWS; y++) {
            String s = String.valueOf(y);
            g.drawString(s, LEFT - 8 - fm.stringWidth(s), TOP + y * CELL + CELL / 2 + 4);
        }
    }

    private void drawGrid(Graphics2D g) {
        g.setColor(GRID);
        for (int x = 0; x <= Mission.COLS; x++) g.drawLine(x * CELL, 0, x * CELL, H);
        for (int y = 0; y <= Mission.ROWS; y++) g.drawLine(0, y * CELL, W, y * CELL);
        if (hover != null) {
            g.setColor(HOVER);
            g.fillRect(hover.x() * CELL + 1, hover.y() * CELL + 1, CELL - 1, CELL - 1);
        }
    }

    private void drawZone(Graphics2D g) {
        int x = Mission.ZONE_X1 * CELL, y = Mission.ZONE_Y1 * CELL;
        int w = (Mission.ZONE_X2 - Mission.ZONE_X1 + 1) * CELL;
        int h = (Mission.ZONE_Y2 - Mission.ZONE_Y1 + 1) * CELL;
        g.setColor(ZONE_FILL);
        g.fillRect(x, y, w, h);
        g.setColor(ZONE_EDGE);
        g.drawRect(x, y, w, h);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
        g.drawString("NO-FLY ZONE", x + 22, y - 6);
    }

    private void drawRange(Graphics2D g) {
        double r = (100 / Mission.RATE / 2) * CELL;   // safe radius in pixels
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10f, new float[] {6f, 6f}, 0f));
        g.setColor(RANGE);
        g.draw(new Ellipse2D.Double(px(Mission.BASE.x()) - r, px(Mission.BASE.y()) - r, 2 * r, 2 * r));
        g.setStroke(old);
    }

    private void drawBase(Graphics2D g) {
        int x = Mission.BASE.x() * CELL, y = Mission.BASE.y() * CELL;
        g.setColor(NAVY);
        g.fillRect(x + 4, y + 4, CELL - 8, CELL - 8);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        g.drawString("H", x + 14, y + 27);
    }

    private void drawOrders(Graphics2D g) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        for (Order o : mission.getQueue()) {
            int x = o.cell().x() * CELL, y = o.cell().y() * CELL;
            g.setColor(ORDER);
            g.fillRect(x + 8, y + 8, CELL - 16, CELL - 16);
            g.setColor(Color.WHITE);
            String s = String.valueOf(o.id());
            g.drawString(s, x + (CELL - g.getFontMetrics().stringWidth(s)) / 2, y + 25);
        }
        Order active = mission.getActiveOrder();
        if (active != null) {
            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(3f));
            g.setColor(ORDER);
            g.drawRect(active.cell().x() * CELL + 8, active.cell().y() * CELL + 8, CELL - 16, CELL - 16);
            g.setStroke(old);
        }
    }

    private void drawDrone(Graphics2D g) {
        int x = px(mission.getDroneX()), y = px(mission.getDroneY());
        Stroke old = g.getStroke();
        if (mission.getState() == Mission.State.CRASHED) {
            g.setStroke(new BasicStroke(4f));
            g.setColor(new Color(0xC0392B));
            g.drawLine(x - 12, y - 12, x + 12, y + 12);
            g.drawLine(x + 12, y - 12, x - 12, y + 12);
            g.setStroke(old);
            return;
        }
        g.setStroke(new BasicStroke(3f));
        g.setColor(new Color(0x1D2433));
        g.drawLine(x - 11, y - 11, x + 11, y + 11);
        g.drawLine(x + 11, y - 11, x - 11, y + 11);
        g.setStroke(old);
        g.setColor(RANGE);
        int[][] rotors = {{-11, -11}, {11, -11}, {-11, 11}, {11, 11}};
        for (int[] r : rotors) g.fillOval(x + r[0] - 6, y + r[1] - 6, 12, 12);
        g.setColor(new Color(0x1D2433));
        g.fillOval(x - 6, y - 6, 12, 12);
    }
}
