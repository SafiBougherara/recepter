package bdd;

import java.sql.*;

public class ServiceManager {

    public boolean addService(String name, double amount) {
        BddManager bddManager = new BddManager();
        Connection Connection = bddManager.connection();
        String sql_request = "INSERT INTO services (name, amount) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = Connection.prepareStatement(sql_request);
            pstmt.setString(1, name);
            pstmt.setDouble(2, amount);
            return pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getServices(){
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        ResultSet rs = null;
        String sql_request = "SELECT * FROM services";
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(sql_request);
            //System.out.println(rs);
            return rs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeService(int id) {
        BddManager bdd = new BddManager();
        Connection connection = bdd.connection();
        String sql_request = "DELETE FROM services WHERE id = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql_request);
            pstmt.setInt(1, id);
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
