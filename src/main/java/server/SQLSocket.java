package server;

import java.sql.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author blechner
 */
public class SQLSocket {

    private final static Logger LOGGER = Logger.getLogger(SQLSocket.class.getName());
    private Connection con;

    public SQLSocket() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MYSQL-Driver not found");
            return;
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DB", "root", "");
            // System.out.println("connected to sqlsocket");
        } catch (SQLException e) {
            System.out.println("connection to sqlsocket failed:" + e.getMessage());
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

    public int login(String un, String pw) {
        String pw_rs = null;
        int banned_rs = 0;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select Password, banned from register where name = \"" + un + "\"");

            while (rs.next()) {
                pw_rs = rs.getString(1);
                banned_rs = rs.getInt(2);
            }
            if (banned_rs == 1) {
                LOGGER.log(Level.INFO, "User is Banned");
                return 1; // 1: user banned
            }
            if (pw_rs == null) {
                return -1;// -1: error
            } else if (!pw_rs.equals(pw)) {
                return 2;// 2: pw falsch
            } else {
                return 0;// : passt alles
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
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

    public boolean ban(String name) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("UPDATE `register` SET \"banned=1\" WHERE Name=\"" + name + "\"");
            return true;
        } catch (SQLException e) {
            // System.out.println(e.getMessage());
            return false;
        }
    }
}
