import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StockScreenerUI {
    private JFrame frame;
    private JTextField searchField;
    private JTable resultTable;
    private JTable topStocksTable;
    private JTable balanceSheetTable;
    private JTable cashFlowTable;
    private JLabel avgPriceLabel, totalVolumeLabel, avgPERatioLabel, marketCapRangeLabel, countLabel;
    private DBConnector db;
    private JTabbedPane bottomTabs;

    public StockScreenerUI() {
        db = new DBConnector();
        frame = new JFrame("Stock Screener");
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top Panel with Search Field
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");

        inputPanel.add(new JLabel("Search Stock:"));
        inputPanel.add(searchField);
        inputPanel.add(searchButton);
        frame.add(inputPanel, BorderLayout.NORTH);

        // Left Panel with Top Stocks
        topStocksTable = new JTable();
        JScrollPane topScrollPane = new JScrollPane(topStocksTable);
        topScrollPane.setPreferredSize(new Dimension(400, 400));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Top 10 Stocks:"), BorderLayout.NORTH);
        leftPanel.add(topScrollPane, BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);

        // Center Panel with Results
        resultTable = new JTable();
        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        resultScrollPane.setPreferredSize(new Dimension(500, 400));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Search Results:"), BorderLayout.NORTH);
        centerPanel.add(resultScrollPane, BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Right Panel with Stats
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));

        avgPriceLabel = new JLabel("Average Price: -");
        totalVolumeLabel = new JLabel("Total Volume: -");
        avgPERatioLabel = new JLabel("Average P/E Ratio: -");
        marketCapRangeLabel = new JLabel("Market Cap Range: -");
        countLabel = new JLabel("Stocks Matched: -");

        statsPanel.add(avgPriceLabel);
        statsPanel.add(totalVolumeLabel);
        statsPanel.add(avgPERatioLabel);
        statsPanel.add(marketCapRangeLabel);
        statsPanel.add(countLabel);

        frame.add(statsPanel, BorderLayout.EAST);

        // Bottom Panel with Tabs
        balanceSheetTable = new JTable();
        cashFlowTable = new JTable();

        JScrollPane balanceScroll = new JScrollPane(balanceSheetTable);
        JScrollPane cashFlowScroll = new JScrollPane(cashFlowTable);

        bottomTabs = new JTabbedPane();
        bottomTabs.addTab("Balance Sheet", balanceScroll);
        bottomTabs.addTab("Cash Flow", cashFlowScroll);
        bottomTabs.setPreferredSize(new Dimension(frame.getWidth(), 150));
        frame.add(bottomTabs, BorderLayout.SOUTH);

        // Load Top 10 Stocks
        loadTopStocks();

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText().trim().toLowerCase();
                loadSearchResults(query);
            }
        });

        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void loadTopStocks() {
        try {
            ArrayList<String[]> topStocks = db.getTopStocks();
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Symbol");
            model.addColumn("Price");

            for (String[] stock : topStocks) {
                model.addRow(stock);
            }

            topStocksTable.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to load top stocks: " + e.getMessage());
        }
    }

    private void loadSearchResults(String query) {
        try {
            ArrayList<String[]> matches = db.filterStocks(query);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Name");
            model.addColumn("Symbol");
            model.addColumn("Price");
            model.addColumn("Market Cap");
            model.addColumn("Volume");
            model.addColumn("P/E Ratio");
            model.addColumn("Sector");

            float totalPrice = 0;
            long totalVolume = 0;
            float totalPERatio = 0;
            long maxMarketCap = Long.MIN_VALUE;
            long minMarketCap = Long.MAX_VALUE;
            int validCount = 0;

            for (String[] stock : matches) {
                if (stock.length < 7) {
                    System.err.println("Skipped invalid row (length " + stock.length + "): " + java.util.Arrays.toString(stock));
                    continue;
                }

                model.addRow(stock);

                try {
                    float price = Float.parseFloat(stock[2]);
                    long volume = Long.parseLong(stock[4]);
                    float pe = Float.parseFloat(stock[5]);
                    long marketCap = Long.parseLong(stock[3]);

                    totalPrice += price;
                    totalVolume += volume;
                    totalPERatio += pe;
                    maxMarketCap = Math.max(maxMarketCap, marketCap);
                    minMarketCap = Math.min(minMarketCap, marketCap);
                    validCount++;

                } catch (NumberFormatException nfe) {
                    System.err.println("Skipping row with invalid number format: " + java.util.Arrays.toString(stock));
                }
            }

            resultTable.setModel(model);

            avgPriceLabel.setText("Average Price: " + (validCount > 0 ? String.format("%.2f", totalPrice / validCount) : "-"));
            totalVolumeLabel.setText("Total Volume: " + totalVolume);
            avgPERatioLabel.setText("Average P/E Ratio: " + (validCount > 0 ? String.format("%.2f", totalPERatio / validCount) : "-"));
            marketCapRangeLabel.setText("Market Cap Range: " + (validCount > 0 ? minMarketCap + " - " + maxMarketCap : "-"));
            countLabel.setText("Stocks Matched: " + validCount);

            loadBalanceSheetTable(matches);
            loadCashFlowTable(matches);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private void loadBalanceSheetTable(ArrayList<String[]> stocks) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Symbol");
        model.addColumn("Total Assets");
        model.addColumn("Debt");
        model.addColumn("ROE");

        for (String[] stock : stocks) {
            if (stock.length >= 11) {
                model.addRow(new String[] {
                    stock[0],  // Name
                    stock[1],  // Symbol
                    stock[8],  // Total Assets
                    stock[9],  // Debt
                    stock[10]  // ROE
                });
            }
        }

        balanceSheetTable.setModel(model);
    }

    private void loadCashFlowTable(ArrayList<String[]> stocks) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Symbol");
        model.addColumn("Cash Flow");

        for (String[] stock : stocks) {
            if (stock.length >= 11) {
                model.addRow(new String[] {
                    stock[0],  // Name
                    stock[1],  // Symbol
                    stock[7]   // Cash Flow
                });
            }
        }

        cashFlowTable.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockScreenerUI());
    }
}
