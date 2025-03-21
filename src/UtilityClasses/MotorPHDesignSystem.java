package UtilityClasses;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * This class provides utility methods and design constants for creating a consistent
 * user interface across the MotorPH Payroll System.
 */
public class MotorPHDesignSystem {

    // DESIGN CONSTANTS

    // Color scheme
    public static final Color PRIMARY_COLOR = new Color(25, 118, 210);  // Material blue
    public static final Color ACCENT_COLOR = new Color(230, 230, 230);  // Light gray
    public static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Nearly white
    public static final Color TEXT_COLOR = new Color(33, 33, 33);  // Dark gray for text
    public static final Color BUTTON_TEXT_COLOR = Color.WHITE;
    public static final Color ERROR_COLOR = new Color(211, 47, 47);  // Red for errors/deletion
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80);  // Green for success
    public static final Color WARNING_COLOR = new Color(255, 152, 0);  // Orange for warnings

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    // Dimensions
    public static final Dimension BUTTON_SIZE_MEDIUM = new Dimension(120, 35);
    public static final Dimension BUTTON_SIZE_LARGE = new Dimension(200, 35);
    public static final Dimension FIELD_SIZE = new Dimension(200, 35);

    // Insets and padding
    public static final Insets PANEL_PADDING = new Insets(15, 15, 15, 15);
    public static final int COMPONENT_SPACING = 10;

    /**
     * Applies the standard styling to a JButton
     *
     * @param button The button to style
     * @param size The size to set for the button (use constants)
     * @return The styled button
     */
    public static JButton styleButton(JButton button, Dimension size) {
        button.setPreferredSize(size);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(BUTTON_TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    /**
     * Styles a button as a danger button (for delete operations)
     *
     * @param button The button to style
     * @param size The size to set for the button
     * @return The styled button
     */
    public static JButton styleDangerButton(JButton button, Dimension size) {
        styleButton(button, size);
        button.setBackground(ERROR_COLOR);

        // Override hover effect for danger button
        for (MouseListener listener : button.getMouseListeners()) {
            button.removeMouseListener(listener);
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ERROR_COLOR.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(ERROR_COLOR);
            }
        });

        return button;
    }

    /**
     * Creates a styled info panel with title for displaying groups of information
     *
     * @param title The title for the panel
     * @return The styled panel
     */
    public static JPanel createInfoPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create title panel with gradient background
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, getWidth(), 0,
                        new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 200));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setPreferredSize(new Dimension(0, 40));
        titlePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SUBHEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        panel.add(titlePanel, BorderLayout.NORTH);

        return panel;
    }

    /**
     * Styles a text field with standard appearance
     *
     * @param field The field to style
     * @return The styled field
     */
    public static JTextField styleTextField(JTextField field) {
        field.setPreferredSize(FIELD_SIZE);
        field.setFont(FIELD_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    /**
     * Adds a field-value pair to a panel (commonly used in info panels)
     *
     * @param panel The panel to add the field-value pair to
     * @param fieldLabel The label for the field
     * @param valueLabel The label for the value
     */
    public static void addFieldValuePair(JPanel panel, JLabel fieldLabel, JLabel valueLabel) {
        fieldLabel.setFont(LABEL_FONT);
        fieldLabel.setForeground(TEXT_COLOR);

        valueLabel.setFont(FIELD_FONT);
        valueLabel.setForeground(TEXT_COLOR);

        panel.add(fieldLabel);
        panel.add(valueLabel);
    }

    /**
     * Sets up standard bold header renderer for JTable
     *
     * @param table The table to set the header renderer for
     */
    public static void setupTableHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(JLabel.LEFT);
                setFont(SUBHEADER_FONT);
                setBackground(ACCENT_COLOR);
                setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
    }

    /**
     * Makes a panel responsive to window resizing
     *
     * @param panel The panel to make responsive
     */
    public static void makeResponsive(JPanel panel) {
        // Add component listener to handle resize events
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Get the new size
                Dimension newSize = panel.getSize();

                // Calculate new insets based on the panel's size
                int topBottom = Math.max(10, newSize.height / 50);
                int leftRight = Math.max(10, newSize.width / 50);

                // Apply new padding
                panel.setBorder(BorderFactory.createEmptyBorder(topBottom, leftRight, topBottom, leftRight));

                // Revalidate to apply changes
                panel.revalidate();
            }
        });
    }

    /**
     * Creates a standard caption panel for form sections
     *
     * @param title The title for the caption
     * @return The styled caption panel
     */
    public static JPanel createCaptionPanel(String title) {
        JPanel captionPanel = new JPanel();
        captionPanel.setBackground(ACCENT_COLOR);
        captionPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel captionLabel = new JLabel(title);
        captionLabel.setFont(SUBHEADER_FONT);
        captionLabel.setForeground(TEXT_COLOR);

        captionPanel.add(captionLabel);

        return captionPanel;
    }

    /**
     * Applies a standardized look and feel to a JOptionPane dialog
     *
     * @param parent The parent component
     * @param message The message to display
     * @param title The title for the dialog
     * @param messageType The type of message (JOptionPane constants)
     */
    public static void showStyledDialog(Component parent, Object message, String title, int messageType) {
        // Create an option pane with the message
        JOptionPane optionPane = new JOptionPane(message, messageType);

        // Create a dialog that contains the option pane
        JDialog dialog = optionPane.createDialog(parent, title);

        // Style the dialog
        dialog.setFont(FIELD_FONT);

        // Show the dialog
        dialog.setVisible(true);
    }

    /**
     * Creates a styled box for form sections
     *
     * @return The styled box panel
     */
    public static JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        return panel;
    }
}