package javatwitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author Daniel
 */
public class Main {
    
    // Get Twitter instance to allow connection and assigns it to a new Twitter object
    private static final Twitter twitter = new TwitterFactory().getInstance();
    
    public static void main(String[] args) throws TwitterException, IOException {
        // If files path doesn't exist, create that path
        Path filesPath = Paths.get("files");
        if(Files.notExists(filesPath)) {
            new File("files").mkdir();
        }
        
        // Menu for all the functions of the app
        String user, search;
        boolean exit = false;
        while(exit != true) {
            int option = menu();
            
            switch(option) {
                case 0:
                    exit = true;
                    break;
                
                case 1:
                    System.out.print("Enter username: ");
                    user = enterString();
                    System.out.println("");
                    // Calls getUserTimeline() method receiving the twitter instance and inputed username
                    getUserTimeline(twitter, user);
                    break;
                    
                case 2:
                    System.out.print("Enter username: ");
                    user = enterString();
                    System.out.println("");
                    getUserFavTweets();
                    break;
                    
                case 3:
                    System.out.print("Enter search keyword: ");
                    search = enterString();
                    System.out.println("");
                    searchTweets(search);
                    break;
            }
        }
    }

    private static int menu() {
        System.out.println("TWITTER4J MENU");
        System.out.println("1. Get user's last 20 tweets");
        System.out.println("2. Get user's favourited tweet");
        System.out.println("3. Search tweets");
        System.out.println("0. Exit");
        System.out.print("Option: ");
        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        System.out.println("");
        
        return option;
    } 
    
    private static String enterString() {
        // Scanner for string variables
        Scanner input = new Scanner(System.in);
        String string = input.nextLine();
        
        return string;
    }

    // Method to retrieve last 20 twits from an user
    private static void getUserTimeline(Twitter twitter, String user) throws TwitterException, IOException {
        List<Status> statuses = twitter.getUserTimeline(user);
        String userName = "@" + statuses.get(0).getUser().getScreenName();
        int tweetNumber = 1;
        
        // Get the current date and time and format it to desired layout
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        
        System.out.println("Latest tweets: ");
        
        // Scan all the tweets and prints them
        for (Status status : statuses) {
            String tweets = "Tweet #" + tweetNumber++ + "\n\n" + status.getText() + "\n________________________________________________";
            System.out.println(tweets + "\n");
        } tweetNumber = 1;
        
        // Option to save tweets to a .txt file
        System.out.print("Do you want to save these tweets? [Y/N]: ");
        String option = enterString().toUpperCase();
        
        // Save twits to a file if user inputs yes
        if (option.equals("Y")) {
            FileWriter fileWriter = new FileWriter(new File("files" + File.separator + userName + "_tweets.txt"));
            try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                // Write current date and time
                writer.write("Tweets from user "  + userName + " at " + dtf.format(ldt) +"\n\n");
                
                // Scan all the twits and writes them to the file
                for (Status status : statuses) {
                    String tweets = "Tweet #" + tweetNumber++ + "\n" + status.getText();
                    writer.write(tweets + "\n________________________________________________");
                    writer.newLine();
                    writer.newLine();
                }
            }
        }
        System.out.println("");
    }
    
    private static void getUserFavTweets() {
        
    }
    
    private static void searchTweets(String search) throws TwitterException {
        Query query = new Query("source:" + search).count(20);
        QueryResult result = twitter.search(query);

        for (Status status : result.getTweets()) {
            System.out.println("@" + status.getUser().getScreenName() + ": " + status.getText());
        }
    }
}
