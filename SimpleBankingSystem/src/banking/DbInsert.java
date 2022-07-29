package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static banking.DbRead.getBalance;

public class DbInsert {

    static void insertCard(String number, String pin, int balance) {

        Connection conn = DbConnection.connect();
        PreparedStatement ps = null;

        try {
            String sql = "INSERT INTO card(number, pin, balance) VALUES(?,?,?) ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, number);
            ps.setString(2, pin);
            ps.setInt(3, balance);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try{
                ps.close();
                conn.close();
            } catch(SQLException e) {
                System.out.println("DbInsert.insertCardException" + e.toString());
            }
        }
    }

    static void addIncome(int incomeToAdd) {

        int balanceAccount = DbRead.getBalance(Main.inputCardNumber);

        Connection conn = DbConnection.connect();
        PreparedStatement ps = null;

        try {
            String sql = "UPDATE card SET balance = ? WHERE number = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, balanceAccount += incomeToAdd);
            ps.setString(2, Main.inputCardNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DbInsert.addIncomeException" + e.getMessage());
        } finally {
            try{
                ps.close();
                conn.close();
            } catch(SQLException e) {
                System.out.println("DbInsertException" + e.toString());
            }
        }
    }

    static void transfer(int transferAmount) {

        int balanceSender = DbRead.getBalance(Main.inputCardNumber);
        int balanceReceiver = DbRead.getBalance(Main.transferNumber);

        Connection conn = DbConnection.connect();
        PreparedStatement ps = null;

        try {
            String sqlSender = "UPDATE card SET balance = ? WHERE number = ?";
            ps = conn.prepareStatement(sqlSender);
            ps.setInt(1, balanceSender -= transferAmount);
            ps.setString(2, Main.inputCardNumber);
            ps.executeUpdate();

            String sqlReciver = "UPDATE card SET balance = ? WHERE number = ?";
            ps = conn.prepareStatement(sqlReciver);
            ps.setInt(1, balanceReceiver += transferAmount);
            ps.setString(2, Main.transferNumber);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("DbInsert.addIncomeException" + e.getMessage());
        } finally {
            try{
                ps.close();
                conn.close();
            } catch(SQLException e) {
                System.out.println("DbInsertException" + e.toString());
            }
        }
    }
}
