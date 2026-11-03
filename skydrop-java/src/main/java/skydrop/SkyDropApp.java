package skydrop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * SkyDrop Java Edition: the window. All flight rules live in {@link Mission};
 * this class only shows the state and passes user actions to it.
 */
public class SkyDropApp extends JFrame {

    private static final Color NAVY = new Color(0x16324F);
    private static final Color PAGE = new Color(0xEEF1F6);
    private static final Color LINE = new Color(0xD5DBE5);
    private static final Color MUTED = new Color(0x6B7489);
    private static final Color RED = new Color(0xB02A37);
    private static final Color GREEN = new Color(0x1E7B4F);

    private final Mission mission = new Mission();
    private final MapPanel map;

    private final JLabel queueStat = bigNumber();
    private final JLabel deliveredStat = bigNumber();
    private final JLabel rejectedStat = bigNumber();
    private final JLabel stateStat = bigNumber();
    private final BatteryBar battery = new BatteryBar();
    private final JLabel batteryLabel = new JLabel("Battery 100%");
    private final JLabel hoverLabel = new JLabel(" ");

    private final JTextField xField = new JTextField(3);
    private final JTextField yField = new JTextField(3);
    private final JLabel resultLabel = new JLabel(" ");

    private final DefaultListModel<Order> queueModel = new DefaultListModel<>();
    private final DefaultListModel<Mission.LogEntry> logModel = new DefaultListModel<>();
    private List<Order> shownQueue = new ArrayList<>();
    private int shownLogSize = -1;

    public SkyDropApp() {
        super("SkyDrop v0.3 (Java Edition)");
        map = new MapPanel(mission, this::placeOrder, this::showHover);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(PAGE);
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(14, 0));
        body.setOpaque(false);
        body.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        body.add(buildMapCard(), BorderLayout.CENTER);
        body.add(buildSidePanel(), BorderLayout.EAST);
        root.add(body, BorderLayout.CENTER);

        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);

        refresh();
        new Timer(16, e -> {      // about 60 frames per second
            mission.step();
            refresh();
        }).start();
    }

    // ----------------------------------------------------------------- actions

    private void placeOrder(Cell c) {
        Decision d = mission.addOrder(c);
        showResult(c, d);
        refresh();
    }

    private void placeTypedOrder() {
        int x, y;
        try {
            x = Integer.parseInt(xField.getText().trim());
            y = Integer.parseInt(yField.getText().trim());
        } catch (NumberFormatException ex) {
            resultLabel.setForeground(RED);
            resultLabel.setText("X and Y must be whole numbers.");
            return;
        }
        placeOrder(new Cell(x, y));
        xField.requestFocusInWindow();
        xField.selectAll();
    }

    private void showResult(Cell c, Decision d) {
        boolean ok = d == Decision.ACCEPTED;
        resultLabel.setForeground(ok ? GREEN : RED);
        resultLabel.setText(c + ": " + d);
    }

    private void showHover(Cell c) {
        if (c == null) {
            hoverLabel.setText("Move the mouse over the map to see a cell's coordinates.");
            return;
        }
        String zone = mission.inNoFlyZone(c) ? "   |   inside no-fly zone" : "";
        hoverLabel.setText(String.format("Cell %s   |   round trip %.0f%% battery%s",
                c, mission.roundTripCost(c), zone));
    }

    // ----------------------------------------------------------------- refresh

    private void refresh() {
        queueStat.setText(String.valueOf(mission.getQueue().size()));
        deliveredStat.setText(String.valueOf(mission.getDeliveredCount()));
        rejectedStat.setText(String.valueOf(mission.getRejectedCount()));
        stateStat.setText(mission.getState().name());
        stateStat.setForeground(mission.getState() == Mission.State.CRASHED ? RED : NAVY);

        battery.setLevel(mission.getBattery());
        batteryLabel.setText("Battery " + Math.round(Math.max(0, mission.getBattery())) + "%");

        List<Order> q = mission.getQueue();
        if (!q.equals(shownQueue)) {
            queueModel.clear();
            q.forEach(queueModel::addElement);
            shownQueue = q;
        }
        List<Mission.LogEntry> log = mission.getLog();
        if (log.size() != shownLogSize) {
            logModel.clear();
            for (int i = log.size() - 1; i >= 0; i--) logModel.addElement(log.get(i)); // newest first
            shownLogSize = log.size();
        }
        map.repaint();
    }

    // ----------------------------------------------------------------- layout

    private JComponent buildHeader() {
        JPanel band = new JPanel();
        band.setLayout(new BoxLayout(band, BoxLayout.Y_AXIS));
        band.setBackground(NAVY);
        band.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel title = new JLabel("SkyDrop v0.3  (Java Edition)");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Click a map cell or type coordinates to place an order, then press Launch.");
        sub.setForeground(new Color(0xD6DEEA));
        band.add(title);
        band.add(Box.createVerticalStrut(2));
        band.add(sub);

        JPanel stats = new JPanel(new GridLayout(1, 5, 10, 0));
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(12, 18, 0, 18));
        stats.add(stat(queueStat, "Orders waiting"));
        stats.add(stat(deliveredStat, "Delivered"));
        stats.add(stat(rejectedStat, "Rejected"));
        stats.add(stat(stateStat, "Drone status"));

        JPanel batt = card(new BorderLayout(0, 4));
        batt.add(battery, BorderLayout.CENTER);
        batteryLabel.setForeground(MUTED);
        batt.add(batteryLabel, BorderLayout.SOUTH);
        stats.add(batt);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(band, BorderLayout.NORTH);
        top.add(stats, BorderLayout.CENTER);
        return top;
    }

    private JComponent buildMapCard() {
        JPanel c = card(new BorderLayout(0, 6));
        c.add(map, BorderLayout.CENTER);
        hoverLabel.setForeground(MUTED);
        showHover(null);
        JLabel legend = new JLabel("H = base     red = no-fly zone (x 7-9, y 2-4)     dashed circle = safe range");
        legend.setForeground(MUTED);
        JPanel south = new JPanel(new GridLayout(2, 1));
        south.setOpaque(false);
        south.add(hoverLabel);
        south.add(legend);
        c.add(south, BorderLayout.SOUTH);
        JPanel wrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrap.setOpaque(false);
        wrap.add(c);
        return wrap;
    }

    private JComponent buildSidePanel() {
        JPanel side = new JPanel(new GridBagLayout());
        side.setOpaque(false);
        side.setPreferredSize(new Dimension(290, 10));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.fill = GridBagConstraints.BOTH;
        g.weightx = 1;
        g.insets = new Insets(0, 0, 10, 0);

        // Order entry by coordinates
        JPanel entry = card(new BorderLayout(0, 6));
        entry.add(heading("Add order by coordinates"), BorderLayout.NORTH);
        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        fields.setOpaque(false);
        JButton add = new JButton("Add order");
        add.addActionListener(e -> placeTypedOrder());
        yField.addActionListener(e -> placeTypedOrder());   // Enter key
        xField.addActionListener(e -> yField.requestFocusInWindow());
        fields.add(new JLabel("X"));
        fields.add(xField);
        fields.add(new JLabel("Y"));
        fields.add(yField);
        fields.add(add);
        entry.add(fields, BorderLayout.CENTER);
        resultLabel.setFont(resultLabel.getFont().deriveFont(Font.BOLD));
        entry.add(resultLabel, BorderLayout.SOUTH);
        side.add(entry, g);

        // Launch and reset
        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 0));
        buttons.setOpaque(false);
        JButton launch = new JButton("Launch");
        launch.addActionListener(e -> { mission.launch(); refresh(); });
        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> {
            mission.reset();
            resultLabel.setText(" ");
            refresh();
        });
        buttons.add(launch);
        buttons.add(reset);
        side.add(buttons, g);

        // Queue
        JList<Order> queueList = new JList<>(queueModel);
        queueList.setVisibleRowCount(4);
        side.add(listCard("Order queue", queueList, 92), g);

        // Log
        JList<Mission.LogEntry> logList = new JList<>(logModel);
        logList.setCellRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> l, Object v, int i,
                                                                    boolean sel, boolean focus) {
                Mission.LogEntry e = (Mission.LogEntry) v;
                JLabel lab = (JLabel) super.getListCellRendererComponent(l, e.text(), i, sel, focus);
                if (e.warning() && !sel) {
                    lab.setForeground(RED);
                    lab.setFont(lab.getFont().deriveFont(Font.BOLD));
                }
                return lab;
            }
        });
        g.weighty = 1;
        g.insets = new Insets(0, 0, 0, 0);
        side.add(listCard("Flight log", logList, 110), g);
        return side;
    }

    // ----------------------------------------------------------------- helpers

    private static JLabel bigNumber() {
        JLabel l = new JLabel("0");
        l.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        l.setForeground(NAVY);
        return l;
    }

    private static JPanel stat(JLabel value, String caption) {
        JPanel p = card(new BorderLayout());
        p.add(value, BorderLayout.CENTER);
        JLabel c = new JLabel(caption);
        c.setForeground(MUTED);
        p.add(c, BorderLayout.SOUTH);
        return p;
    }

    private static JLabel heading(String text) {
        JLabel h = new JLabel(text);
        h.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        h.setForeground(NAVY);
        return h;
    }

    private static JPanel listCard(String title, JList<?> list, int height) {
        JPanel p = card(new BorderLayout(0, 6));
        p.add(heading(title), BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(list);
        sp.setPreferredSize(new Dimension(10, height));
        sp.setMinimumSize(new Dimension(10, height));
        sp.setBorder(BorderFactory.createLineBorder(LINE));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private static JPanel card(java.awt.LayoutManager layout) {
        JPanel p = new JPanel(layout);
        p.setBackground(Color.WHITE);
        Border line = BorderFactory.createLineBorder(LINE);
        p.setBorder(BorderFactory.createCompoundBorder(line, BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return p;
    }

    /** A simple battery gauge: green above 50%, orange above 20%, red below. */
    private static class BatteryBar extends JComponent {
        private double level = 100;

        BatteryBar() {
            setPreferredSize(new Dimension(150, 20));
        }

        void setLevel(double level) {
            this.level = Math.max(0, Math.min(100, level));
            repaint();
        }

        @Override
        protected void paintComponent(java.awt.Graphics g0) {
            java.awt.Graphics2D g = (java.awt.Graphics2D) g0.create();
            g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth() - 1, h = getHeight() - 1;
            g.setColor(PAGE);
            g.fillRect(0, 0, w, h);
            g.setColor(level > 50 ? new Color(0x2E9E6B) : level > 20 ? new Color(0xE0952B) : RED);
            g.fillRect(0, 0, (int) Math.round(w * level / 100), h);
            g.setColor(LINE);
            g.drawRect(0, 0, w, h);
            g.dispose();
        }
    }

    // ----------------------------------------------------------------- main

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // fall back to the default look and feel
        }
        SwingUtilities.invokeLater(() -> new SkyDropApp().setVisible(true));
    }
}
