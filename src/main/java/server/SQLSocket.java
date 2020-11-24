/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.*;

/**
 *
 * @author blech
 */
public class SQLSocket {

    private Connection con;

    public SQLSocket() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL-Driver not found");
        }
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB", "root", "");
        } catch (SQLException e) {
            System.out.println("connection failed:" + e.getMessage());
        }
        if (con != null) {
            try {
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("select * from register");
                while (rs.next()) {
                    System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
