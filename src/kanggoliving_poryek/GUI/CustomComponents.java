package kanggoliving_poryek.GUI;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomComponents {

    public static class ThemeColor {
        public static final Color PASTEL_BROWN_BG = new Color(237, 227, 215); // Pastel Brown BG
        public static final Color SIDEBAR_BG = new Color(74, 59, 48);          // Dark Pastel Brown
        public static final Color CARD_BG = new Color(252, 250, 247);         // Warm Soft White
        public static final Color ACCENT_GREEN = new Color(46, 125, 50);       // Forest Green
        public static final Color ACCENT_GREEN_HOVER = new Color(56, 142, 60);
        public static final Color TEXT_DARK = new Color(51, 41, 34);           // Deep Espresso
        public static final Color TEXT_LIGHT = new Color(245, 240, 235);
        public static final Color TEXT_MUTED = new Color(130, 115, 105);
        public static final Color DIVIDER = new Color(220, 210, 200);
        public static final Color STATUS_SUCCESS_BG = new Color(232, 245, 233);
        public static final Color STATUS_PENDING_BG = new Color(255, 243, 224);
        public static final Color STATUS_PENDING_FG = new Color(230, 81, 0);
    }

    public static class RoundedPanel extends JPanel {
        private int cornerRadius = 15;
        private Color shadowColor = new Color(0, 0, 0, 15);
        private int shadowSize = 4;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }

        public RoundedPanel() {
            this(15);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            int width = getWidth();
            int height = getHeight();
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw shadow
            graphics.setColor(shadowColor);
            for (int i = 0; i < shadowSize; i++) {
                graphics.drawRoundRect(i, i, width - 1 - i * 2, height - 1 - i * 2, arcs.width, arcs.height);
            }

            // Draw background
            graphics.setColor(getBackground());
            graphics.fillRoundRect(shadowSize, shadowSize, width - 1 - shadowSize * 2, height - 1 - shadowSize * 2, arcs.width, arcs.height);
        }
    }

    public static class RoundedButton extends JButton {
        private Color normalColor = ThemeColor.ACCENT_GREEN;
        private Color hoverColor = ThemeColor.ACCENT_GREEN_HOVER;
        private Color activeColor = ThemeColor.ACCENT_GREEN.darker();
        private int radius = 10;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setBackground(activeColor);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }
            });
            setBackground(normalColor);
        }

        public void setColors(Color normal, Color hover) {
            this.normalColor = normal;
            this.hoverColor = hover;
            this.activeColor = normal.darker();
            setBackground(normal);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static class SidebarButton extends JButton {
        private boolean active = false;

        public SidebarButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setForeground(ThemeColor.TEXT_LIGHT);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!active) {
                        setBackground(new Color(255, 255, 255, 25));
                        setOpaque(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!active) {
                        setOpaque(false);
                    }
                }
            });
        }

        public void setActive(boolean active) {
            this.active = active;
            if (active) {
                setBackground(ThemeColor.ACCENT_GREEN);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setOpaque(true);
            } else {
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
                setOpaque(false);
            }
            repaint();
        }
    }

    public static class CustomTable extends JTable {
        public CustomTable() {
            super();
            setRowHeight(30);
            setFont(new Font("Segoe UI", Font.PLAIN, 12));
            setSelectionBackground(new Color(46, 125, 50, 40));
            setSelectionForeground(ThemeColor.TEXT_DARK);
            setGridColor(ThemeColor.DIVIDER);
            setShowVerticalLines(false);

            // Table Header Styling
            JTableHeader header = getTableHeader();
            header.setFont(new Font("Segoe UI", Font.BOLD, 12));
            header.setBackground(ThemeColor.SIDEBAR_BG);
            header.setForeground(Color.WHITE);
            header.setReorderingAllowed(false);

            // Default Cell Renderer to center text vertically and add padding
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 245, 240));
                    }
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    return c;
                }
            };
            setDefaultRenderer(Object.class, cellRenderer);
        }
    }
}
