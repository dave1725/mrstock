import java.sql.*;
import java.util.ArrayList;

public class DBConnector {
    private Connection conn;

    public DBConnector() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:stocks.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String[]> filterStocks(String query) {
        ArrayList<String[]> results = new ArrayList<>();

        try {
            Statement stmt = conn.createStatement();
            // Ensure all required columns are selected
            ResultSet rs = stmt.executeQuery("SELECT name, symbol, price, market_cap, volume, pe_ratio, sector, cash_flow, total_assets, debt, roe FROM stocks");

            while (rs.next()) {
                String name = rs.getString("name");
                String symbol = rs.getString("symbol");

                if (!name.toLowerCase().contains(query) && !symbol.toLowerCase().contains(query)) continue;

                String price = rs.getString("price");
                String marketCap = rs.getString("market_cap");
                String volume = rs.getString("volume");
                String peRatio = rs.getString("pe_ratio");
                String sector = rs.getString("sector");
                String cashFlow = rs.getString("cash_flow");
                String totalAssets = rs.getString("total_assets");
                String debt = rs.getString("debt");
                String roe = rs.getString("roe");

                results.add(new String[]{
                    name, symbol, price, marketCap, volume, peRatio, sector,
                    cashFlow, totalAssets, debt, roe
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public ArrayList<String[]> getTopStocks() {
        ArrayList<String[]> stocks = new ArrayList<>();
        try {
            String query = "SELECT name, symbol, price FROM stocks ORDER BY market_cap DESC LIMIT 10";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String[] row = {
                    rs.getString("name"),
                    rs.getString("symbol"),
                    rs.getString("price")
                };
                stocks.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stocks;
    }
}
