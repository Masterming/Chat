package server;

import java.sql.*;
import java.util.*;

/**
 * @author blechner
 */
public class SQLSocket {

    private Connection con;

    public SQLSocket() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL-Driver not found");
            return;
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB", "root", "");
            //System.out.println("connected to sqlsocket");
        } catch (SQLException e) {
            System.out.println("connection to sqlsocket failed:" + e.getMessage());
            return;
        }
    }

    public void printAllUser() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from register");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
        }
    }

    public List<String> getAllUser() {
        List<String> l = new ArrayList<>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select Name from register");
            while (rs.next()) {
                l.add(rs.getString(1));
            }
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
        }
        return l;
    }

    public boolean login(String un, String pw) {
        String res = null;

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select Password from register where name = \"" + un + "\"");

            while (rs.next()) {
                res = rs.getString(1);
            }
            if (res == null) {
                return false;
            }
            return (res.equals(pw));
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean register(String un, String pw) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO `register`(`Name`, `Password`) VALUES (\"" + un + "\", \"" + pw + "\")");
            return true;
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
            return false;
        }
    }
}
