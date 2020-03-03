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
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

/**
 *
 * @author Daniel
 */
public class Main {
    
    // Get Twitter instance to allow connection and assigns it to a new Twitter object
    // Tokens and keys are set in the twitter4j.properties file
    private static final Twitter TWITTER = new TwitterFactory().getInstance();
    
    public static void main(String[] args) throws TwitterException, IOException, Exception {
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
                    getTimeline(user);
                    break;
                    
                case 2:
                    System.out.print("Enter username: ");
                    user = enterString();
                    System.out.println("");
                    getUserFavTweets(user);
                    break;
                    
                case 3:
                    System.out.print("Enter search keyword: ");
                    search = enterString();
                    System.out.println("");
                    searchTweets(search);
                    break;
                    
                case 4:
                    getReplies();
                    
                case 5:
                // Gets the API user and shows the rate limit status
                User rlsuser = TWITTER.showUser(TWITTER.getId());
                System.out.println("Seconds until reset: " + rlsuser.getRateLimitStatus().getSecondsUntilReset() + " seconds\n");
                break;
                    
                default:
                    System.out.println("Wrong input.\n");
                    break;
            }
        }
    }

    // Menu method
    private static int menu() {
        System.out.println("TWITTER4J MENU");
        System.out.println("1. Get user's last 100 tweets");
        System.out.println("2. Get user's last 100 favourited tweets");
        System.out.println("3. Search tweets (100 max)");
        System.out.println("4. Get replies from specific tweet");
        System.out.println("5. Get API rate limit status");
        System.out.println("0. Exit");
        System.out.print("Option: ");
        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        System.out.println("");
        
        return option;
    } 
    
    // Scanner for string values
    private static String enterString() {
        Scanner input = new Scanner(System.in);
        String string = input.nextLine();
        
        return string;
    }

    // Method to retrieve last 20 tweets from an user
    private static void getTimeline(String user) throws TwitterException, IOException {
        Paging page = new Paging(1, 100);
        List<Status> statuses = TWITTER.getUserTimeline(user, page);
        String username = "@" + statuses.get(0).getUser().getScreenName();
        int tweetnumber = 1;
        
        // Get the current date and time and format it to desired layout
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        
        System.out.println("Latest tweets: ");
        
        // Scan all the tweets and prints them
        for (Status status : statuses) {
            String tweets = "Tweet #" + tweetnumber++ + "\n" + status.getText() + "\n________________________________________________";
            System.out.println(tweets + "\n");
        } tweetnumber = 1;
        
        // Option to save tweets to a .txt file
        System.out.print("Do you want to save these tweets? [Y/N]: ");
        String option = enterString().toUpperCase();
        
        // Save twits to a file if user inputs yes
        if (option.equals("Y")) {
            FileWriter filewriter = new FileWriter(new File("files" + File.separator + username + "_tweets.txt"));
            try (BufferedWriter writer = new BufferedWriter(filewriter)) {
                // Write current date and time
                writer.write("Tweets from user "  + username + " at " + dtf.format(ldt) +"\n\n");
                
                // Scan all the twits and writes them to the file
                for (Status status : statuses) {
                    String tweets = "Tweet #" + tweetnumber++ + "\n" + status.getText();
                    writer.write(tweets + "\n________________________________________________");
                    writer.newLine();
                    writer.newLine();
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("");
    }
    
    // Method to retrieve last favourited tweets from an user
    private static void getUserFavTweets(String user) throws TwitterException {
        Paging page = new Paging(1, 100);
        int tweetnumber = 0;
        List<Status> favtweets = TWITTER.getFavorites(user, page);
        
        for (Status favtweet : favtweets) {
            String favtweetsuser = "@" + favtweets.get(tweetnumber).getUser().getScreenName();
            String tweets = "Tweet #" + (tweetnumber++ + 1) + " from user " + favtweetsuser + "\n" + favtweet.getText() + "\n________________________________________________";
            System.out.println(tweets + "\n");
        }
    }
    
    // Method to search tweets using a keyword
    private static void searchTweets(String search) throws TwitterException, IOException {
        Query query = new Query(search);
        // The search is limited to 100 petitions due to API limitations
        query.setCount(100);
        QueryResult result = TWITTER.search(query);
        List<Status> statuses = result.getTweets();
        int tweetnumber = 1;
        
        // Get the current date and time and format it to desired layout
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        
        System.out.println("Results: ");
        
        // Scan all the tweets and prints them
        for (Status status : statuses) {
            System.out.println("Result #" + tweetnumber++ + " from user " + "@" + status.getUser().getScreenName());
            System.out.println(status.getText() + "\n________________________________________________\n");
        } tweetnumber = 1;
        
        // Option to save tweets to a .txt file
        System.out.print("Do you want to save these tweets? [Y/N]: ");
        String option = enterString().toUpperCase();
        
        // Save twits to a file if user inputs yes
        if (option.equals("Y")) {
            FileWriter fileWriter = new FileWriter(new File("files" + File.separator + search + "_searchResult.txt"));
            try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                // Write current date and time
                writer.write("Results for keyword \""  + search + "\" at " + dtf.format(ldt) +"\n\n");
                
                // Scan all the twits and writes them to the file
                for (Status status : statuses) {
                    String tweets = "Result #" + tweetnumber++ + " from user " + "@" + status.getUser().getScreenName() + "\n" + status.getText();
                    writer.write(tweets + "\n________________________________________________");
                    writer.newLine();
                    writer.newLine();
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("");
    }
    
    private static void getReplies() throws TwitterException, Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter tweet ID to retrieve replies: ");
        long id = input.nextLong();
        System.out.println("");
        
        Status status = TWITTER.showStatus(id);
        
        try {
            System.out.println("Tweet from user " + "@" + status.getUser().getScreenName() + ": ");
            System.out.println(status.getText() + "\n");
            System.out.print("Is this the correct tweet? [Y/N]: ");
            String answer = enterString().toUpperCase();
            System.out.println("");
            
            if (answer.equals("Y")) {
                List<Status> replies; // IDK HOW TO DO IT aaaAAAAAAAAAAAAAAAAAA
                System.out.println("Replies to the tweet: ");
            } else {
                getReplies();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
