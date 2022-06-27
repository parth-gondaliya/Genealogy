import java.sql.*;

public class SqlConnection {
	
	private static String url = "jdbc:mysql://127.0.0.1:3306/test_db";
	private static String user_name = "root";
	private static String password = "root";
	

	
	/**
	 * database connection
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection() throws SQLException, ClassNotFoundException
	{	
			Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection connect = DriverManager.getConnection(url,user_name,password);
			return connect;
	}	
	

	
	/**
	 * close DB connection
	 * @param conn
	 * @throws SQLException
	 */
	public static void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}
	
	
}
