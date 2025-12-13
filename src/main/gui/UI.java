package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * SimpleUI Framework (No HashMap version)
 * ---------------------------------------
 * This version stores split-page (dashboard page) information
 * using arrays instead of HashMap.
 *
 * WHAT YOU GET:
 * -------------
 * 1) Normal pages (login, signup, etc.)
 * 2) Dashboard-style split pages:
 * LEFT = menu buttons
 * RIGHT = CardLayout (multiple switchable panels)
 * 3) Easy helper methods:
 * - label, button, textField, passwordField
 * - table and tableFull
 * - scrollablePanel
 * - popup
 * 4) Theme support
 * 5) Full comments for beginners
 *
 * All functionality is same as HashMap version — only storage changed.
 */
public class UI extends JFrame {

    // =====================================================
    // MAIN PAGE MANAGEMENT (CardLayout for whole app)
    // =====================================================
    private final CardLayout mainLayout = new CardLayout();
    private final JPanel mainCardPanel = new JPanel(mainLayout);

    // =====================================================
    // Arrays of components for split pages
    // Each index belongs to one split page:
    // splitNames[i] = name of split page
    // splitLeftPanels[i] = left panel (menu)
    // splitRightPanels[i] = right panel (CardLayout)
    // splitRightLayouts[i] = its CardLayout object
    // =====================================================
    private final String[] splitNames = new String[50];
    private final JPanel[] splitLeftPanels = new JPanel[50];
    private final JPanel[] splitRightPanels = new JPanel[50];
    private final CardLayout[] splitRightLayouts = new CardLayout[50];
    private int splitCount = 0; // how many split pages exist

    // =====================================================
    // THEME VARIABLES
    // =====================================================
    private Color themeBg = null;
    private Color themeFg = null;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public UI(String title, int w, int h) {
        super(title);
        setSize(w, h);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(mainCardPanel, BorderLayout.CENTER);
    }

    // =====================================================
    // BASIC PAGE CREATION
    // =====================================================

    /** Create a simple page (absolute layout by default). */
    public JPanel createPage(String name) {
        JPanel p = new JPanel(null);
        applyTheme(p);
        mainCardPanel.add(p, name);
        return p;
    }

    /** FlowLayout page */
    public JPanel flowPage(String name) {
        JPanel p = new JPanel(new FlowLayout());
        applyTheme(p);
        mainCardPanel.add(p, name);
        return p;
    }

    /** GridLayout page */
    public JPanel gridPage(String name, int rows, int cols, int hgap, int vgap) {
        JPanel p = new JPanel(new GridLayout(rows, cols, hgap, vgap));
        applyTheme(p);
        mainCardPanel.add(p, name);
        return p;
    }

    // =====================================================
    // SPLIT PAGE (DASHBOARD STYLE)
    // =====================================================

    /**
     * Create dashboard split page.
     * Example: split: LEFT = menu, RIGHT = content cards.
     */
    public JPanel createSplitPage(String name, int leftWidth) {

        JPanel left = new JPanel(null); // menu panel
        JPanel right = new JPanel(new CardLayout()); // card container

        applyTheme(left);
        applyTheme(right);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(leftWidth);

        JPanel container = new JPanel(new BorderLayout());
        container.add(split, BorderLayout.CENTER);

        mainCardPanel.add(container, name);

        // store into arrays
        splitNames[splitCount] = name;
        splitLeftPanels[splitCount] = left;
        splitRightPanels[splitCount] = right;
        splitRightLayouts[splitCount] = (CardLayout) right.getLayout();
        splitCount++;

        return container;
    }

    /** Find index of split page by name */
    private int findSplitIndex(String name) {
        for (int i = 0; i < splitCount; i++) {
            if (splitNames[i].equals(name)) {
                return i;
            }
        }
        return -1; // not found
    }

    /** Create right-side card for dashboard page */
    public JPanel createRightCard(String splitPageName, String cardName) {
        int index = findSplitIndex(splitPageName);
        if (index == -1) {
            throw new RuntimeException("Split page not found: " + splitPageName);
        }

        JPanel card = new JPanel(null); // absolute layout
        applyTheme(card);
        splitRightPanels[index].add(card, cardName);
        return card;
    }

    /** Create right-side card with custom layout manager */
    public JPanel createRightCard(String splitPageName, String cardName, LayoutManager layout) {
        int index = findSplitIndex(splitPageName);
        if (index == -1) {
            throw new RuntimeException("Split page not found: " + splitPageName);
        }

        JPanel card = new JPanel(layout);
        applyTheme(card);
        splitRightPanels[index].add(card, cardName);
        return card;
    }

    /** Switch entire page (login → dashboard) */
    public void showPage(String name) {
        mainLayout.show(mainCardPanel, name);
    }

    /** Switch right panel card inside dashboard */
    public void showRightCard(String splitPageName, String cardName) {
        int index = findSplitIndex(splitPageName);
        if (index == -1) {
            throw new RuntimeException("Split page not found: " + splitPageName);
        }

        splitRightLayouts[index].show(splitRightPanels[index], cardName);
    }

    // =====================================================
    // LEFT MENU BUTTON HELPERS
    // =====================================================

    /** Add simple left menu button */
    public JButton addLeftMenuButton(String splitPageName, String text, int x, int y, int w, int h, Runnable action) {
        int index = findSplitIndex(splitPageName);
        if (index == -1)
            throw new RuntimeException("Split page not found: " + splitPageName);

        JButton b = new JButton(text);
        b.setBounds(x, y, w, h);
        applyTheme(b);
        if (action != null)
            b.addActionListener(e -> action.run());
        splitLeftPanels[index].add(b);

        return b;
    }

    /** Add left menu button that switches right card */
    public JButton addLeftMenuButtonLinked(String splitPageName, String text, int x, int y, int w, int h,
            String rightCard) {
        return addLeftMenuButton(splitPageName, text, x, y, w, h,
                () -> showRightCard(splitPageName, rightCard));
    }

    // =====================================================
    // BASIC COMPONENT HELPER METHODS
    // =====================================================

    public JLabel label(JPanel p, String text, int x, int y, int w, int h) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, w, h);
        applyTheme(l);
        p.add(l);
        return l;
    }

    public JButton button(JPanel p, String text, int x, int y, int w, int h, Runnable action) {
        JButton b = new JButton(text);
        b.setBounds(x, y, w, h);
        applyTheme(b);
        if (action != null)
            b.addActionListener(e -> action.run());
        p.add(b);
        return b;
    }

    public JTextField textField(JPanel p, int x, int y, int w, int h) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, w, h);
        applyTheme(tf);
        p.add(tf);
        return tf;
    }

    public JPasswordField passwordField(JPanel p, int x, int y, int w, int h) {
        JPasswordField pf = new JPasswordField();
        pf.setBounds(x, y, w, h);
        applyTheme(pf);
        p.add(pf);
        return pf;
    }

    // =====================================================
    // TABLE HELPERS
    // =====================================================

    /** Table with absolute position */
    public JTable table(JPanel p, String[] columns, Object[][] data, int x, int y, int w, int h) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable t = new JTable(model);
        JScrollPane sp = new JScrollPane(t);
        sp.setBounds(x, y, w, h);
        applyTheme(sp);
        p.add(sp);
        return t;
    }

    /** Table for layout-managed pages */
    public JScrollPane tableFull(JPanel p, String[] columns, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable t = new JTable(model);
        JScrollPane sp = new JScrollPane(t);
        applyTheme(sp);
        p.add(sp);
        return sp;
    }

    // =====================================================
    // SCROLLABLE PANEL
    // =====================================================

    public JScrollPane scrollablePanel(JPanel p, int w, int h) {
        JScrollPane sp = new JScrollPane(p);
        sp.setPreferredSize(new Dimension(w, h));
        applyTheme(sp);
        return sp;
    }

    // =====================================================
    // POPUP (& MESSAGEBOX)
    // =====================================================

    public void popup(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    // =====================================================
    // THEME SUPPORT
    // =====================================================

    public void setTheme(Color bg, Color fg) {
        themeBg = bg;
        themeFg = fg;
    }

    protected void applyTheme(Component c) {
        if (themeBg != null)
            c.setBackground(themeBg);
        if (themeFg != null)
            c.setForeground(themeFg);

        if (c instanceof JPanel) {
            ((JPanel) c).setOpaque(true);
        }
    }
}
