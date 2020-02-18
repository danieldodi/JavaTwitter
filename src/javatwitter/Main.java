package javatwitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Daniel
 */
public class Main {

    // Keys and tokens necessary to connect to Twitter API
    private static final String CONSUMER_KEY = "HDZbUhcAZUehKDGpsTqmUzi2H";
    private static final String CONSUMER_SECRET = "YVUZtoJ1gg3VQFRtFNZJgTvIc8afLnio7tnM84CMIEX7VcKvOb";
    private static final String ACCESS_TOKEN = "1015941676974706688-R3PN3S5OhspZg4y7WwxoSZVFUiv2Zt";
    private static final String ACCESS_TOKEN_SECRET = "Sr7mrzqS4kC05Sx8xrIPxFgtS7LqEufuWt6R3YKp9ZL29";
    
    public static void main(String[] args) throws TwitterException, IOException {
        // Scanner for username input
        Scanner input = new Scanner(System.in);
        System.out.print("Enter username: ");
        String user = input.nextLine();
        System.out.println("");
        
        // Calls getTwitterInstance() method
        getTwitterInstance();
        Twitter twitter = getTwitterInstance();
        
        // Calls getUserTimeline() method receiving the twitter instance and inputed username
        getUserTimeline(twitter, user);
    }

    // Method to connect to Twitter instance
    public static Twitter getTwitterInstance() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
            .setOAuthConsumerKey(CONSUMER_KEY)
            .setOAuthConsumerSecret(CONSUMER_SECRET)
            .setOAuthAccessToken(ACCESS_TOKEN)
            .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        return twitter;
    }
    
    // Method to retrieve last 20 twits from an user
    public static void getUserTimeline(Twitter twitter, String user) throws TwitterException, IOException {
        List<Status> statuses = twitter.getUserTimeline(user);
        String userName = "@" + statuses.get(0).getUser().getScreenName();
        int twitNumber = 1;
        
        System.out.println("Latest twits: ");
        
        // Recorre all the twits and prints them
        for (Status status : statuses) {
            String twits = "Twit #" + twitNumber++ + "\n\n" + status.getText() + "\n________________________________________________";
            System.out.println(twits + "\n");
        }
        
        // Option to save twits to a .txt file
        System.out.print("Do you want to save these twits? [Y/N]: ");
        Scanner input = new Scanner(System.in);
        String option = input.nextLine().toUpperCase();
        
        if (option.equals("Y")) {
            FileWriter fileWriter = new FileWriter(new File("files" + File.separator + userName + "_twits.txt"));
            BufferedWriter writer = new BufferedWriter(fileWriter);
            
            
            writer.write("Twits from user "  + userName + "\n\n");
            
            for (Status status : statuses) {
                String twits = "Twit #" + twitNumber++ + "\n" + status.getText();
                writer.write(twits + "\n________________________________________________");
                writer.newLine();
                writer.newLine();
            }
            
            writer.close();
        }
    }
}
