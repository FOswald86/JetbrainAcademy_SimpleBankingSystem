package banking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbDelete {
	static void deleteAccount() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		try {
			String sql = "DELETE FROM card WHERE number = ? ";
			ps = con.prepareStatement(sql);
			ps.setString(1, Main.inputCardNumber);
			System.out.println("\nThe account has been closed!\n");
			ps.execute();
		} catch (SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println("DbDeleteException" + e.toString());
			}

		}
	}
}
