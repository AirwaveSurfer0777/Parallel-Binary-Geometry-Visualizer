import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;
/** AUTHOR: AIRWAVESURFER0777 **/
public class Main extends JFrame
{
    private static final long serialVersionUID = 4648172894076113183L;

    public Main()
    {
        initUI();  // Call initUI only once here
        setTitle("Parallel Binary Geometry Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);  // Increased size to accommodate more lines

        JPanel panel = new JPanel() 
        {
            private static final long serialVersionUID = 77091376395953152L;

            @Override
            protected void paintComponent(Graphics g) 
            {
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

                // Method to draw horizontal connections for a specific row
                BiConsumer<Integer, boolean[]> drawHorizontalConnections = (rowIndex, connectFirst) -> {
                    int x = textXPositions[rowIndex];
                    int y = textYPositions[rowIndex];
                    
                    if (connectFirst[0])
                    {
                        int startX1 = x + charWidth/2;
                        int endX1 = x + (2 * charWidth) + (2 * spaceWidth) + charWidth/2;
                        g2d.drawLine(startX1, y - 5, endX1, y - 5);
                    }
                    
                    if (connectFirst[1]) 
                    {
                        int startX2 = x + (4 * charWidth) + (4 * spaceWidth) + charWidth/2;
                        int endX2 = x + (6 * charWidth) + (6 * spaceWidth) + charWidth/2;
                        g2d.drawLine(startX2, y - 5, endX2, y - 5);
                    }
                };

                // Draw horizontal connections
                drawHorizontalConnections.accept(0, new boolean[]{true, true});
                drawHorizontalConnections.accept(2, new boolean[]{true, true});
                drawHorizontalConnections.accept(4, new boolean[]{true, true});
                
                // New horizontal connections for 0s in lines 2, 4, and 6
                BiConsumer<Integer, Void> drawZeroHorizontalConnections = (rowIndex, v) -> {
                    int x = textXPositions[rowIndex];
                    int y = textYPositions[rowIndex];
                    
                    // Connect first two 0s
                    int startX1 = x + charWidth/2;
                    int endX1 = x + charWidth + spaceWidth + charWidth/2;
                    g2d.drawLine(startX1, y - 5, endX1, y - 5);
                    
                    // Connect second and third 0s
                    int startX1b = x + charWidth + spaceWidth + charWidth/2;
                    int endX1b = x + (2 * charWidth) + (2 * spaceWidth) + charWidth/2;
                    g2d.drawLine(startX1b, y - 5, endX1b, y - 5);
                    
                    // Connect last three 0s
                    int startX2 = x + (4 * charWidth) + (4 * spaceWidth) + charWidth/2;
                    int endX2 = x + (6 * charWidth) + (6 * spaceWidth) + charWidth/2;
                    g2d.drawLine(startX2, y - 5, endX2, y - 5);
                };

                // Draw new horizontal connections for 0s
                drawZeroHorizontalConnections.accept(1, null);
                drawZeroHorizontalConnections.accept(3, null);
                drawZeroHorizontalConnections.accept(5, null);

                // Diagonal connections
                BiConsumer<Integer, Integer> drawDiagonalConnections = (topRowIndex, bottomRowIndex) -> {
                    int topRowX1 = textXPositions[topRowIndex] + (0 * charWidth) + (0 * spaceWidth) + charWidth/2;
                    int topRowX2 = textXPositions[topRowIndex] + (1 * charWidth) + (1 * spaceWidth) + charWidth/2;
                    int topRowX3 = textXPositions[topRowIndex] + (5 * charWidth) + (5 * spaceWidth) + charWidth/2;
                    int topRowX4 = textXPositions[topRowIndex] + (6 * charWidth) + (6 * spaceWidth) + charWidth/2;
                    
                    int middleRowX = textXPositions[topRowIndex + 1] + (3 * charWidth) + (3 * spaceWidth) + charWidth/2;
                    
                    int bottomRowX1 = textXPositions[bottomRowIndex] + (0 * charWidth) + (0 * spaceWidth) + charWidth/2;
                    int bottomRowX2 = textXPositions[bottomRowIndex] + (1 * charWidth) + (1 * spaceWidth) + charWidth/2;
                    int bottomRowX3 = textXPositions[bottomRowIndex] + (5 * charWidth) + (5 * spaceWidth) + charWidth/2;
                    int bottomRowX4 = textXPositions[bottomRowIndex] + (6 * charWidth) + (6 * spaceWidth) + charWidth/2;

                    // Previous 1s connections
                    int prevTopRowX1 = textXPositions[topRowIndex] + (2 * charWidth) + (2 * spaceWidth) + charWidth/2;
                    int prevTopRowX2 = textXPositions[topRowIndex] + (4 * charWidth) + (4 * spaceWidth) + charWidth/2;
                    int prevBottomRowX1 = textXPositions[bottomRowIndex] + (2 * charWidth) + (2 * spaceWidth) + charWidth/2;
                    int prevBottomRowX2 = textXPositions[bottomRowIndex] + (4 * charWidth) + (4 * spaceWidth) + charWidth/2;

                    // Diagonal connections for 1s (previous connections)
                    g2d.drawLine(prevTopRowX1, textYPositions[topRowIndex] - 5, middleRowX, textYPositions[topRowIndex + 1] - 5);
                    g2d.drawLine(prevTopRowX2, textYPositions[topRowIndex] - 5, middleRowX, textYPositions[topRowIndex + 1] - 5);
                    g2d.drawLine(middleRowX, textYPositions[topRowIndex + 1] - 5, prevBottomRowX1, textYPositions[bottomRowIndex] - 5);
                    g2d.drawLine(middleRowX, textYPositions[topRowIndex + 1] - 5, prevBottomRowX2, textYPositions[bottomRowIndex] - 5);

                    // New connections for 0s
                    g2d.drawLine(topRowX1, textYPositions[topRowIndex] - 5, middleRowX, textYPositions[topRowIndex + 1] - 5);
                    g2d.drawLine(topRowX2, textYPositions[topRowIndex] - 5, middleRowX, textYPositions[topRowIndex + 1] - 5);
                    g2d.drawLine(topRowX3, textYPositions[topRowIndex] - 5, middleRowX, textYPositions[topRowIndex + 1] - 5);
                    g2d.drawLine(topRowX4, textYPositions[topRowIndex] - 5, middleRowX, textYPositions[topRowIndex + 1] - 5);
                    
                    g2d.drawLine(middleRowX, textYPositions[topRowIndex + 1] - 5, bottomRowX1, textYPositions[bottomRowIndex] - 5);
                    g2d.drawLine(middleRowX, textYPositions[topRowIndex + 1] - 5, bottomRowX2, textYPositions[bottomRowIndex] - 5);
                    g2d.drawLine(middleRowX, textYPositions[topRowIndex + 1] - 5, bottomRowX3, textYPositions[bottomRowIndex] - 5);
                    g2d.drawLine(middleRowX, textYPositions[topRowIndex + 1] - 5, bottomRowX4, textYPositions[bottomRowIndex] - 5);
                };

                // Draw diagonal connections
                drawDiagonalConnections.accept(0, 2);
                drawDiagonalConnections.accept(2, 4);
                drawDiagonalConnections.accept(1, 3);
                drawDiagonalConnections.accept(3, 5);

                // New diagonal connections for middle 0 in first line to 0s in second line
                int zeroX = textXPositions[0] + (3 * charWidth) + (3 * spaceWidth) + charWidth/2;
                int targetY = textYPositions[1] - 5;
                for (int i = 0; i < 7; i++) 
                {
                    if (i != 3)
                    { // Skip the middle 1
                        int targetX = textXPositions[1] + (i * charWidth) + (i * spaceWidth) + charWidth/2;
                        g2d.drawLine(zeroX, textYPositions[0] - 5, targetX, targetY);
                    }
                }

                // New diagonal connections from 1s in line 5 to middle 1 in line 6
                int bottomMiddleX = textXPositions[5] + (3 * charWidth) + (3 * spaceWidth) + charWidth/2;
                for (int i = 0; i < 7; i++) 
                {
                    if (texts[4].charAt(i * 2) == '1') 
                    {
                        int sourceX = textXPositions[4] + (i * charWidth) + (i * spaceWidth) + charWidth/2;
                        g2d.drawLine(sourceX, textYPositions[4] - 5, bottomMiddleX, textYPositions[5] - 5);
                    }
                }
            }
        };

        add(panel);
        setLocationRelativeTo(null);
    }
    
    private void initUI() 
    {
        try {
            // Ensure decorations are set
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            
            // Set the look and feel before creating any Swing components
            UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceMagmaLookAndFeel");
            
            // Update the UI of this frame after setting look and feel
            SwingUtilities.updateComponentTreeUI(this);
        } 
        catch (ClassNotFoundException e)
        {
            try {
                System.out.println("Substance theme not detected, reverting to OS Default.");
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}