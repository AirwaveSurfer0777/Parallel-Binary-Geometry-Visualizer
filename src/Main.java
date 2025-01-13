import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** AUTHOR AIRWAVESURFER0777 **/
public class Main extends JFrame {
    private static final long serialVersionUID = 4648172894076113183L;

    // Default binary value
    private int binaryValue = 119; // Change this value as needed

    // Booleans to enable/disable connections
    private boolean drawHorizontally = true; // Set to false to disable
    private boolean drawDiagonally = true; // Set to false to disable
    private boolean drawOnes = true; // Set to false to disable connections for 1s
    private boolean drawZeros = true; // Set to false to disable connections for 0s

    private JTextField inputField;
    private JPanel panel;

    // Labels to show the status of toggles
    private JLabel horizontalStatus;
    private JLabel diagonalStatus;
    private JLabel onesStatus;
    private JLabel zerosStatus;

    public Main() {
        initUI();
        setTitle("Parallel Binary Geometry Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600); // Increased height to accommodate status labels

        // Set an icon for the JFrame
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/icon.png")));

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create "About" menu
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Parallel Binary Geometry Visualizer\n" +
                        "Author: AIRWAVESURFER0777\n" +
                        "The inspiration for this program comes from the binary code 1110111, " +
                        "found written in the pages of the book Autobiography of a Yogi.\n" +
                        "Made for the people of India, by an American.",
                "About",
                JOptionPane.INFORMATION_MESSAGE));
        aboutMenu.add(aboutItem);
        menuBar.add(aboutMenu);
        
        // Set the menu bar
        setJMenuBar(menuBar);

        // Create the main panel
        panel = new JPanel() {
            private static final long serialVersionUID = 77091376395953152L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Set up the text properties
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 16));

                FontMetrics metrics = g.getFontMetrics();
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                // Convert the binary value to binary string with spaces
                String binaryString = Integer.toBinaryString(binaryValue);
                String spacedBinary = String.join(" ", binaryString.split("")); // Add spaces between characters

                // Create the lines based on the binary value
                String[] texts = new String[6];
                String reversedBinary = new StringBuilder(spacedBinary).reverse().toString(); // Reverse the binary string

                // Set the texts for each line
                for (int i = 0; i < texts.length; i++) {
                    if (i % 2 == 0) {
                        texts[i] = spacedBinary; // Normal binary for 1st, 3rd, 5th lines
                    } else {
                        texts[i] = reversedBinary; // Reversed binary for 2nd, 4th, 6th lines
                    }
                }

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

                // Draw horizontal connections if enabled
                if (drawHorizontally) {
                    for (int i = 0; i < texts.length; i++) {
                        drawHorizontalConnections(g2d, texts[i], textXPositions[i], textYPositions[i], charWidth, spaceWidth);
                    }
                }

                // Draw diagonal connections if enabled
                if (drawDiagonally) {
                    for (int i = 0; i < texts.length - 1; i++) {
                        drawDiagonalConnections(g2d, texts[i], texts[i + 1], textXPositions[i], textYPositions[i], textXPositions[i + 1], textYPositions[i + 1], charWidth, spaceWidth);
                    }
                }
            }

            private void drawHorizontalConnections(Graphics2D g2d, String text, int x, int y, int charWidth, int spaceWidth) {
                String[] parts = text.split(" ");
                for (int j = 0; j < parts.length - 1; j++) {
                    if (parts[j].equals(parts[j + 1])) {
                        int startX = x + (j * (charWidth + spaceWidth));
                        int endX = x + ((j + 1) * (charWidth + spaceWidth) + charWidth);
                        if (parts[j].equals("1") && drawOnes) {
                            g2d.drawLine(startX, y - 5, endX, y - 5);
                        } else if (parts[j].equals("0") && drawZeros) {
                            g2d.drawLine(startX, y - 5, endX, y - 5);
                        }
                    }
                }
            }

            private void drawDiagonalConnections(Graphics2D g2d, String topText, String bottomText, int topX, int topY, int bottomX, int bottomY, int charWidth, int spaceWidth) {
                String[] topParts = topText.split(" ");
                String[] bottomParts = bottomText.split(" ");

                for (int j = 0; j < topParts.length; j++) {
                    for (int k = 0; k < bottomParts.length; k++) {
                        if (topParts[j].equals(bottomParts[k]) && (topParts[j].equals("0") || topParts[j].equals("1"))) {
                            if (topParts[j].equals("1") && drawOnes) {
                                int startX = topX + (j * (charWidth + spaceWidth)) + charWidth / 2;
                                int endX = bottomX + (k * (charWidth + spaceWidth)) + charWidth / 2;
                                g2d.drawLine(startX, topY - 5, endX, bottomY - 5);
                            } else if (topParts[j].equals("0") && drawZeros) {
                                int startX = topX + (j * (charWidth + spaceWidth)) + charWidth / 2;
                                int endX = bottomX + (k * (charWidth + spaceWidth)) + charWidth / 2;
                                g2d.drawLine(startX, topY - 5, endX, bottomY - 5);
                            }
                        }
                    }
                }
            }
        };

        // Create input panel
        JPanel inputPanel = new JPanel();
        inputField = new JTextField(10);
        JButton selectButton = new JButton("Select Number");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    binaryValue = Integer.parseInt(inputField.getText());
                    panel.repaint(); // Repaint the panel with the new binary value
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        inputPanel.add(selectButton);
        inputPanel.add(inputField);
        inputPanel.setBackground(Color.BLACK);
        inputPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create toggle buttons panel
        JPanel togglePanel = new JPanel();
        togglePanel.setLayout(new GridLayout(2, 4)); // Use GridLayout for horizontal arrangement

        JButton toggleHorizontalButton = new JButton("Toggle Horizontal");
        horizontalStatus = new JLabel("OFF");
        horizontalStatus.setForeground(Color.RED);
        toggleHorizontalButton.addActionListener(e -> {
            drawHorizontally = !drawHorizontally;
            horizontalStatus.setText(drawHorizontally ? "ON" : "OFF");
            horizontalStatus.setForeground(drawHorizontally ? Color.GREEN : Color .RED);
            panel.repaint();
        });

        JButton toggleDiagonalButton = new JButton("Toggle Diagonal");
        diagonalStatus = new JLabel("ON");
        diagonalStatus.setForeground(Color.GREEN);
        toggleDiagonalButton.addActionListener(e -> {
            drawDiagonally = !drawDiagonally;
            diagonalStatus.setText(drawDiagonally ? "ON" : "OFF");
            diagonalStatus.setForeground(drawDiagonally ? Color.GREEN : Color.RED);
            panel.repaint();
        });

        JButton toggleOnesButton = new JButton("Toggle Ones");
        onesStatus = new JLabel("ON");
        onesStatus.setForeground(Color.GREEN);
        toggleOnesButton.addActionListener(e -> {
            drawOnes = !drawOnes;
            onesStatus.setText(drawOnes ? "ON" : "OFF");
            onesStatus.setForeground(drawOnes ? Color.GREEN : Color.RED);
            panel.repaint();
        });

        JButton toggleZerosButton = new JButton("Toggle Zeros");
        zerosStatus = new JLabel("ON");
        zerosStatus.setForeground(Color.GREEN);
        toggleZerosButton.addActionListener(e -> {
            drawZeros = !drawZeros;
            zerosStatus.setText(drawZeros ? "ON" : "OFF");
            zerosStatus.setForeground(drawZeros ? Color.GREEN : Color.RED);
            panel.repaint();
        });

        // Add buttons and labels to the toggle panel
        togglePanel.add(toggleHorizontalButton);
        togglePanel.add(horizontalStatus);
        togglePanel.add(toggleDiagonalButton);
        togglePanel.add(diagonalStatus);
        togglePanel.add(toggleOnesButton);
        togglePanel.add(onesStatus);
        togglePanel.add(toggleZerosButton);
        togglePanel.add(zerosStatus);
        togglePanel.setBackground(Color.BLACK);

        // Add components to the frame
        add(inputPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(togglePanel, BorderLayout.SOUTH);
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