import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * PROG209 - Event Driven Programming
 * Assignment: Basic Interactive GUI (Java Swing)
 *
 * Demonstrates: JFrame, JPanel, JLabel, ImageIcon,
 *               inheritance (GradientPanel extends JPanel) and constructors.
 */
public class SwingAssignment {

    // ====== CHANGE THIS TO YOUR NAME ======
    private static final String STUDENT_NAME = "Your Name";

    // ====== Required components (names as per the brief) ======
    private JFrame frame;
    private GradientPanel panel;
    private JLabel label;

    // ====== Data for the extra functionality ======
    private final String[] messages = {
            "Welcome to Java Swing First Individual Assignment by '" + STUDENT_NAME + "'",
            "Event Driven Programming is fun!",
            "Java Swing makes desktop apps easy",
            "PROG209 - Limkokwing University, Sierra Leone"
    };

    // Each theme = {top colour, bottom colour}
    private final Color[][] themes = {
            {new Color(0x6A11CB), new Color(0x2575FC)},   // purple -> blue
            {new Color(0xFF512F), new Color(0xDD2476)},   // orange -> pink
            {new Color(0x11998E), new Color(0x38EF7D)},   // teal -> green
            {new Color(0xF7971E), new Color(0xFFD200)},   // amber -> yellow
            {new Color(0x141E30), new Color(0x243B55)}    // midnight
    };

    private int messageIndex = 0;
    private int themeIndex = 0;

    // ====== Inheritance: our own panel class extends JPanel ======
    static class GradientPanel extends JPanel {
        private Color top;
        private Color bottom;

        // Constructor
        GradientPanel(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
            setLayout(new BorderLayout());
        }

        void setColors(Color top, Color bottom) {
            this.top = top;
            this.bottom = bottom;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), bottom));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    }

    // ====== Constructor: builds the whole GUI ======
    public SwingAssignment() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) { }

        // Frame
        frame = new JFrame();
        frame.setTitle("Java Swing Assignment");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Own application icon (icon.png) loaded with ImageIcon
        ImageIcon appIcon = loadIcon(64);
        frame.setIconImage(appIcon.getImage());

        // Panel
        panel = new GradientPanel(themes[0][0], themes[0][1]);

        // Label
        label = new JLabel(wrap(messages[0]), loadIcon(160), SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setIconTextGap(30);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Tahoma", Font.BOLD, 28));

        // Buttons
        JButton textButton = makeButton("Change Text", new Color(0xFFFFFF), new Color(0x333333));
        JButton colorButton = makeButton("Change Background", new Color(0x222222), Color.WHITE);

        textButton.addActionListener((ActionEvent e) -> {
            messageIndex = (messageIndex + 1) % messages.length;
            label.setText(wrap(messages[messageIndex]));
        });

        colorButton.addActionListener((ActionEvent e) -> {
            themeIndex = (themeIndex + 1) % themes.length;
            panel.setColors(themes[themeIndex][0], themes[themeIndex][1]);
        });

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 25));
        buttonBar.setOpaque(false);
        buttonBar.add(textButton);
        buttonBar.add(colorButton);

        // Assemble: label -> panel -> frame
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonBar, BorderLayout.SOUTH);
        frame.add(panel);

        frame.setVisible(true);
    }

    // ====== Helpers ======
    private String wrap(String text) {
        return "<html><div style='text-align:center; width:560px'>" + text + "</div></html>";
    }

    private JButton makeButton(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Tahoma", Font.BOLD, 16));
        b.setBackground(bg);
        b.setForeground(fg);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(12, 28, 12, 28));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    /** Loads icon.png as an ImageIcon (scaled). Falls back to a drawn icon if the file is missing. */
    private ImageIcon loadIcon(int size) {
        ImageIcon file = new ImageIcon("icon.png");
        if (file.getIconWidth() > 0) {
            return new ImageIcon(file.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
        }
        return new ImageIcon(createIcon(size));
    }

    /** Draws our own icon: a gradient rounded square with a white letter "J". */
    private BufferedImage createIcon(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setPaint(new GradientPaint(0, 0, new Color(0xFF512F), size, size, new Color(0xF7B733)));
        g.fillRoundRect(0, 0, size, size, size / 3, size / 3);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Tahoma", Font.BOLD, (int) (size * 0.65)));
        FontMetrics fm = g.getFontMetrics();
        int x = (size - fm.stringWidth("J")) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString("J", x, y);
        g.dispose();
        return img;
    }

    // ====== Entry point ======
    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingAssignment::new);
    }
}
