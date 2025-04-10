import java.sql.*;

public class setupDB {
    public static void main(String[] args) {
        try {
            // Force load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish the connection to the SQLite database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:stocks.db");
            Statement stmt = conn.createStatement();

            // Create table with additional fields for balance sheet and cash flow
            stmt.execute("CREATE TABLE IF NOT EXISTS stocks (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT, symbol TEXT, price REAL," +
                    "market_cap INTEGER, volume INTEGER, pe_ratio REAL, sector TEXT," +
                    "cash_flow REAL, total_assets REAL, debt REAL, roe REAL)");

            // Sample data with cash flow, total assets, debt, and ROE included
            stmt.execute("INSERT INTO stocks (name, symbol, price, market_cap, volume, pe_ratio, sector, cash_flow, total_assets, debt, roe) VALUES " +
                    "('Apple Inc.', 'AAPL', 175.2, 2500000000000, 50000000, 29.5, 'Technology', 10000000000, 380000000000, 50000000000, 35.5), " +
                    "('Tesla Inc.', 'TSLA', 210.3, 800000000000, 30000000, 60.1, 'Automotive', 5000000000, 52000000000, 2000000000, 20.1), " +
                    "('Amazon.com Inc.', 'AMZN', 125.9, 1600000000000, 45000000, 39.8, 'E-Commerce', 7500000000, 400000000000, 45000000000, 28.3), " +
                    "('Alphabet Inc.', 'GOOGL', 150.7, 1800000000000, 42000000, 28.9, 'Technology', 5000000000, 250000000000, 30000000000, 30.0), " +
                    "('Meta Platforms Inc.', 'META', 310.5, 950000000000, 37000000, 24.6, 'Social Media', 2000000000, 150000000000, 20000000000, 22.7), " +
                    "('Microsoft Corp.', 'MSFT', 320.8, 2700000000000, 53000000, 35.2, 'Technology', 12000000000, 400000000000, 45000000000, 33.0), " +
                    "('NVIDIA Corp.', 'NVDA', 620.4, 1400000000000, 47000000, 85.3, 'Semiconductors', 6000000000, 130000000000, 10000000000, 40.2), " +
                    "('Netflix Inc.', 'NFLX', 380.2, 180000000000, 25000000, 33.9, 'Streaming', 2500000000, 50000000000, 5000000000, 18.9), " +
                    "('Adobe Inc.', 'ADBE', 520.6, 240000000000, 22000000, 44.1, 'Software', 1500000000, 80000000000, 3000000000, 26.7), " +
                    "('Intel Corp.', 'INTC', 34.9, 140000000000, 60000000, 15.7, 'Semiconductors', 7000000000, 80000000000, 12000000000, 11.2), " +
                    "('Coca-Cola Co.', 'KO', 60.3, 270000000000, 43000000, 25.1, 'Beverages', 5000000000, 60000000000, 10000000000, 19.6), " +
                    "('PepsiCo Inc.', 'PEP', 185.4, 250000000000, 41000000, 26.8, 'Beverages', 4000000000, 70000000000, 12000000000, 22.4), " +
                    "('Johnson & Johnson', 'JNJ', 160.8, 420000000000, 39000000, 22.4, 'Healthcare', 6000000000, 80000000000, 13000000000, 24.1), " +
                    "('Pfizer Inc.', 'PFE', 42.1, 210000000000, 38000000, 11.6, 'Pharmaceuticals', 1500000000, 50000000000, 5000000000, 15.4), " +
                    "('Bank of America', 'BAC', 30.2, 280000000000, 55000000, 10.3, 'Finance', 3000000000, 200000000000, 25000000000, 8.5), " +
                    "('JPMorgan Chase', 'JPM', 145.3, 440000000000, 47000000, 12.7, 'Finance', 4000000000, 450000000000, 30000000000, 15.2), " +
                    "('Walmart Inc.', 'WMT', 145.1, 400000000000, 46000000, 23.5, 'Retail', 5000000000, 60000000000, 12000000000, 19.8), " +
                    "('Home Depot', 'HD', 340.9, 360000000000, 29000000, 21.6, 'Retail', 2000000000, 70000000000, 10000000000, 16.0), " +
                    "('Chevron Corp.', 'CVX', 160.7, 300000000000, 41000000, 13.9, 'Energy', 6000000000, 250000000000, 5000000000, 20.3), " +
                    "('Exxon Mobil', 'XOM', 115.2, 310000000000, 42000000, 14.2, 'Energy', 7000000000, 270000000000, 20000000000, 17.6)");

            // Print confirmation message
            System.out.println("Database setup complete.");
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
