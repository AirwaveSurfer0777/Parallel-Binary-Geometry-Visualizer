import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import org.json.JSONObject;

public class Main extends JFrame 
{
    private static final long serialVersionUID = 4648172894076113183L;
    private JComboBox<String> fromCurrency;
    private JComboBox<String> toCurrency;
    private JTextField amountField;
    private JLabel resultLabel;
    private JButton convertButton;
    private final String API_KEY = "e7a11fdb17ba4ef12c3db39e"; // Get from exchangerate-api.com
    private final DecimalFormat df = new DecimalFormat("0.00"); // Used for formatting currency values
    public static final int HEIGHT = 300;
    public static final int WIDTH = 270;
    
    public Main() 
    {
        initUI();
        setupComponents();
        setTitle("Currency Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(HEIGHT, WIDTH);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/icon.png")));
    }

    private void setupComponents() 
    {
        // Main panel with GridBagLayout for better control
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Currency arrays
        String[] currencies = {"USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "INR"};
        
        // Initialize components
        fromCurrency = new JComboBox<>(currencies);
        toCurrency = new JComboBox<>(currencies);
        amountField = new JTextField(10);
        convertButton = new JButton("Convert");
        resultLabel = new JLabel("Enter amount and press Convert");
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Amount:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(amountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("From:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(fromCurrency, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("To:"), gbc);
        
        gbc.gridx = 1;
        mainPanel.add(toCurrency, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(convertButton, gbc);
        
        gbc.gridy = 4;
        mainPanel.add(resultLabel, gbc);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add action listener to convert button
        convertButton.addActionListener(e -> performConversion());
    }
    
    private void performConversion()
    {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = fromCurrency.getSelectedItem().toString();
            String to = toCurrency.getSelectedItem().toString();
            
            // Make API call in background thread
            SwingWorker<Double, Void> worker = new SwingWorker<>() 
            {
                @Override
                protected Double doInBackground() throws Exception
                {
                    String urlStr = String.format(
                        "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%f",
                        API_KEY, from, to, amount
                    );
                    
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    
                    BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                    );
                    StringBuilder response = new StringBuilder();
                    String line;
                    
                    while ((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }
                    reader.close();
                    
                    JSONObject json = new JSONObject(response.toString());
                    return json.getDouble("conversion_result");
                }
                
                @Override
                protected void done() 
                {
                    try {
                        double result = get();
                        resultLabel.setText(String.format(
                            "%s %s = %s %s",
                            df.format(amount),
                            from,
                            df.format(result),
                            to
                        ));
                    } catch (Exception ex) {
                        resultLabel.setText("Error: " + ex.getMessage());
                    }
                }
            };
            
            worker.execute();
            resultLabel.setText("Converting...");
            
        } catch (NumberFormatException ex) {
            resultLabel.setText("Please enter a valid number");
        }
    }

    private void initUI()
    {
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

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> 
        {
            Main gui = new Main();
            gui.setVisible(true);
        });
    }
}