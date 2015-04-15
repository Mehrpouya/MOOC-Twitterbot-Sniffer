import static java.util.concurrent.TimeUnit.SECONDS;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import twitter4j.*;

/*
 * Author: Hadi Mehrpouya
 * This application is developed as part of Edinburgh University, Challenge fund
 * based at Digital Education Department.
 * 
 * It is using twitter4j API.
 * It uses the Twitter streaming capacity and 
 * will listens to changes on provided #tags and specific user updates.
 * All status updates will then be stored in a local mySql database.
 * 
 * Further developments:
 * *	Add capability to store data in other formats
 * 			Thoughts:
 * 				Mongodb
 * 				CSV,TSV
 *
 *  
 * */
/*
 * FIXME:
 * Get the list of #tag and users to follow from database.
 * 
 * */
public class TwitterSave {
	static Twitter twitter;
	static TwitterFactory factory;
	static StatusListener listener;
	static TwitterStream twitterStream;
	static ArrayList<String> track;
	static ArrayList<Long> follow;

	public static void main(String[] args) {
		setup();
	}

	static void setup() {
		setupTwitterListener();

	}

	private static void setupTwitterListener() {
		// Setting up twitter listener.
		listener = new StatusListener() {
			@Override
			public void onStatus(Status status) {
				if (Globals.IS_DEBUG)
					System.out.println("@" + status.getUser().getScreenName()
							+ " - " + status.getText());
				saveStatusUpdate(status);
			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {
				if (Globals.IS_DEBUG)
					System.out.println("Got a status deletion notice id:"
							+ statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				if (Globals.IS_DEBUG)
					System.out.println("Got track limitation notice:"
							+ numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				if (Globals.IS_DEBUG)
					System.out.println("Got scrub_geo event userId:" + userId
							+ " upToStatusId:" + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				if (Globals.IS_DEBUG)
					System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		twitter4j.conf.ConfigurationBuilder cb = new twitter4j.conf.ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey("")
				.setOAuthConsumerSecret(
						"")
				.setOAuthAccessToken(
						"")
				.setOAuthAccessTokenSecret(
						"");
		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(listener);
		follow = new ArrayList<Long>();
		track = new ArrayList<String>();
		track.add("#edcmooc");
		long[] followArray = new long[follow.size()];
		for (int i = 0; i < follow.size(); i++) {
			followArray[i] = follow.get(i);
		}
		String[] trackArray = track.toArray(new String[track.size()]);
		twitterStream.filter(new FilterQuery(0, followArray, trackArray));
	}

	public static void saveStatusUpdate(Status _status) {
		System.out
				.println("I will now inform sql agent to save this tweet in our database "
						+ _status.getSource());
		User twitterUser = _status.getUser();
		String sn = twitterUser.getScreenName();
		if (sn.toLowerCase().equals("edcmooc")) {
			return;
		} else {
			Tweet t = new Tweet();

			t.userName = twitterUser.getName();
			t.userScreenName = twitterUser.getScreenName();
			t.userStatus = "";
			t.userTimezone = twitterUser.getTimeZone();
			t.tweetText = _status.getText();
			t.tweetDate = new java.sql.Date(_status.getCreatedAt().getTime());
			t.userId=_status.getId();
			System.out.println("$$$$$$$$  " + t.userId);
			t.tweetId = _status.getId();

			try {
				dbManager mydb = new dbManager();
				mydb.insertTweet(t);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
