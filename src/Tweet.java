


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Tweet {
	public long userId=0;
	public String userURL="a";
	public String userName="a";
	public String userScreenName="a";
	public String userStatus="a";
	public String userTimezone="CURRENT_TIMESTAMP";
	public String tweetText="q";
	public java.sql.Date tweetDate;
	public double tweetLongitude=0;
	public double tweetLatitude=0;
	public long tweetId=0;
	public String tweetCountry="sdfsf";
	public String tweetPlaceFullName="sdfsdf";

	public String getAllValues() {
		
		String allVals = "'"+userId + "','" + userName + "','"
				+ userScreenName + "','" + userStatus + "'," + "'"
				+ tweetText + "','" + tweetDate + "','"
				+ tweetId + "',1";
		return allVals;
	}

	public String getAllNames() {
/*		String allNames = "'userId','userName',"
				+ "'userScreenName','userStatus',"
				+ "'userTimezone','tweetText','tweetDate',"
				+ "'tweetLongitude','tweetLatitude','tweetId',"
				+ "'tweetCountry','tweetPlaceFullName'";*/
		String allNames = "userId,userName,"
				+ "userScreenName,userStatus,"
				+ "tweetText,tweetDate,"
				+ "tweetId,"
				+ "action";

		return allNames;
	}

}
