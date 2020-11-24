/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.*;
import java.util.*;

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
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/DB", "root", "");
        } catch (SQLException e) {
            System.out.println("connection failed:" + e.getMessage());
        }
        if (con != null) {
            System.out.println("connected");
        } else {
            System.out.println("connection error");
        }
    }

    public void printuser() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from register");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<String> getUsers() {
        List<String> l = new ArrayList<String>();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select Name from register");
            while (rs.next()) {
                l.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return l;
    }

    public boolean login(String un, String pw) {
        String tmp = null;
        try {

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select Password from register where name = " + un);
            while (rs.next()) {
                tmp = rs.getString(1);
            }
            if (tmp == null) {
                return false;
            }
            return (tmp.equals(pw));

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean register(String un, String pw) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeQuery("INSERT INTO `register`(`Name`, `Passwort`) VALUES (" + un + ", " + pw + ")");
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
