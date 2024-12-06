package bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FactureManager {

    public void addFacture(int num_facture, boolean status, double amount, int client_id) {
        BddManager bm = new BddManager();
        Connection Connection = bm.connection();
        String sql_request = "INSERT INTO factures (num_facture, status, amount, client_id) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement pstmt = Connection.prepareStatement(sql_request);
            pstmt.setInt(1, num_facture);
            pstmt.setBoolean(2, status);
            pstmt.setDouble(3, amount);
            pstmt.setInt(4, client_id);
            pstmt.execute();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getFacture() {
        System.out.println("update facture");
    }
}
