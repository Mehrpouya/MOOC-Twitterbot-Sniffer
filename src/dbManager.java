import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class dbManager {
	private static final String URL = "";
	private static final String USER = "";
	private static final String PASSWORD = "";
	private Statement statement = null;
	private ResultSet resultSet = null;
	private static Connection connection;
	public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

	public dbManager() throws ClassNotFoundException, SQLException {
		// setup connection with the db
		Connection connection = getConnection();
		// statements allow to issue SQL queries to the database
		statement = (Statement) connection.createStatement();
		resultSet = statement.executeQuery("select * from MOOCEDC.tweets");
	}
	
	public static Connection getConnection()
	{
				
		try {
			Class.forName
			(DRIVER_CLASS);	//Class.forName("myDriver.ClassName"); ?

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			connection = (Connection) DriverManager.getConnection(URL,
					USER, PASSWORD);

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
		
		return connection;
	}

	public String writeResultSet(ResultSet resultSet) throws SQLException {
		
		while (resultSet.next()) {
			String comment = resultSet.getString("tweetText");
			return(comment);
		}
		return null;
	}
	
	public void insertTweet(Tweet _tweet) throws SQLException{
		Connection connection = getConnection();

		String q = "insert into MOOCEDC.tweets("+ _tweet.getAllNames() +")"
				+ " values (?,?,?,?,?,?,?,?)";
		System.out.println("query: " + q);
		PreparedStatement pst = (PreparedStatement) connection.prepareStatement( q );
		pst.setLong( 1, _tweet.userId );
		pst.setString( 2, _tweet.userName );
		pst.setString( 3, _tweet.userScreenName );
		pst.setString( 4, _tweet.userStatus );
		pst.setString( 5, _tweet.tweetText );
		pst.setDate( 6, _tweet.tweetDate );
		pst.setLong( 7, _tweet.tweetId );
		pst.setInt( 8, 1);

		int insertResult = pst.executeUpdate();
		connection.close();
	}
	
	
}
