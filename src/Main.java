import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class Main extends JFrame {
    private static final long serialVersionUID = 4648172894076113183L;

    public Main() {
        initUI();
        setTitle("Parallel Binary Geometry Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);

        JPanel panel = new JPanel() {
            private static final long serialVersionUID = 77091376395953152L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                setBackground(new Color(50, 50, 50));

                // Set up the text properties
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));

                FontMetrics metrics = g.getFontMetrics();
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // Six lines of text
                String[] texts = {
                    "1 1 1 0 1 1 1",
                    "0 0 0 1 0 0 0",
                    "1 1 1 0 1 1 1",
                    "0 0 0 1 0 0 0",
                    "1 1 1 0 1 1 1",
                    "0 0 0 1 0 0 0"
                };

                // Calculate vertical spacing
                int verticalSpacing = panelHeight / (texts.length + 1);
                int[] textXPositions = new int[texts.length];
                int[] textYPositions = new int[texts.length];

                // Calculate text positions
                for (int i = 0; i < texts.length; i++) {
                    int textWidth = metrics.stringWidth(texts[i]);
                    textXPositions[i] = (panelWidth - textWidth) / 2;
                    textYPositions[i] = verticalSpacing * (i + 1);
                    g.drawString(texts[i], textXPositions[i], textYPositions[i]);
                }

                // Calculate character and space widths
                int charWidth = metrics.charWidth('1');
                int spaceWidth = metrics.charWidth(' ');

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1.0f));

                // Draw horizontal connections
                for (int i = 0; i < texts.length; i++) {
                    drawHorizontalConnections(g2d, texts[i], textXPositions[i], textYPositions[i], charWidth, spaceWidth);
                }

                // Draw diagonal connections
                for (int i = 0; i < texts.length - 1; i++) {
                    drawDiagonalConnections(g2d, texts[i], texts[i + 1], textXPositions[i], textYPositions[i], textXPositions[i + 1], textYPositions[i + 1], charWidth, spaceWidth);
                }
            }

            private void drawHorizontalConnections(Graphics2D g2d, String text, int x, int y, int charWidth, int spaceWidth) {
                String[] parts = text.split(" ");
                for (int j = 0; j < parts.length - 1; j++) {
                    if (parts[j].equals(parts[j + 1])) {
                        // Start at the left edge of the first character
                        int startX = x + (j * (charWidth + spaceWidth));
                        // End at the right edge of the last character
                        int endX = x + ((j + 1) * (charWidth + spaceWidth) + charWidth);
                        g2d.drawLine(startX, y - 5, endX, y - 5);
                    }
                }
            }

            private void drawDiagonalConnections(Graphics2D g2d, String topText, String bottomText, int topX, int topY, int bottomX, int bottomY, int charWidth, int spaceWidth) {
                String[] topParts = topText.split(" ");
                String[] bottomParts = bottomText.split(" ");

                for (int j = 0; j < topParts.length; j++) {
                    for (int k = 0; k < bottomParts.length; k++) {
                        // Only connect 0s to 0s and 1s to 1s
                        if (topParts[j].equals(bottomParts[k]) && (topParts[j].equals("0") || topParts[j].equals("1"))) {
                            int startX = topX + (j * (charWidth + spaceWidth)) + charWidth / 2;
                            int endX = bottomX + (k * (charWidth + spaceWidth)) + charWidth / 2;
                            g2d.drawLine(startX, topY - 5, endX, bottomY - 5);
                        }
                    }
                }
            }
        };

        add(panel);
        setLocationRelativeTo(null);
    }

    private void initUI() {
        try {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMagmaLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException e) {
            try {
                System.out.println("Substance theme not detected, reverting to OS Default.");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}