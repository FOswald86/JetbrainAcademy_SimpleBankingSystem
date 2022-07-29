package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbRead {

    static String getPinWhere() {

        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String pin = "";
        try {
            String sql = "SELECT pin FROM card WHERE number = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, Main.inputCardNumber);
            rs = ps.executeQuery();
            pin = rs.getString(1);
        } catch(SQLException e) {
            System.out.println(e.toString());
        } finally {
            try{
                rs.close();
                ps.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("DbReadException");
                System.out.println("DbReadException" + e.toString());
            }
        }
        return pin;
    }

    static boolean checkTransferNumberExists(){
        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String pin = "";
        try {
            String sql = "SELECT pin FROM card WHERE number = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, Main.transferNumber);
            rs = ps.executeQuery();
            pin = rs.getString(1);
        } catch(SQLException e) {
            System.out.println(e.toString());
        } finally {
            try{
                rs.close();
                ps.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("DbReadException" + e.toString());
            }
        }
        if (pin.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    static int getBalance(String number) {

        Connection con = DbConnection.connect();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int balance = 0;
        try {
            String sql = "SELECT balance FROM card WHERE number = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, number);
            rs = ps.executeQuery();
            balance = rs.getInt(1);
        } catch(SQLException e) {
            System.out.println(e.toString());
        } finally {
            try{
                rs.close();
                ps.close();
                con.close();
            } catch(SQLException e) {
                System.out.println("DbReadException" + e.toString());
            }
        }
        return balance;
    }
}
