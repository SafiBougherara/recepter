package bdd;

import java.sql.*;

public class ClientManager {

    public void addClient(String brand, String firstname, String name, String siret, String adresse, String email) {
        BddManager bddManager = new BddManager();
        Connection Connection = bddManager.connection();
        String sql_request = "INSERT INTO clients (brand, firstname, name, siret, adresse, mail) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = Connection.prepareStatement(sql_request);
            pstmt.setString(1, brand);
            pstmt.setString(2, firstname);
            pstmt.setString(3, name);
            pstmt.setString(4, siret);
            pstmt.setString(5, adresse);
            pstmt.setString(6, email);

            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public ResultSet getClients(){
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        ResultSet rs = null;
        String sql_request = "SELECT * FROM clients";
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sql_request);
            //System.out.println(rs);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeClient(int id) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "DELETE FROM clients WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setInt(1, id);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
