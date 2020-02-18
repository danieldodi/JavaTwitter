package javatwitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author Daniel
 */
public class Main {
    public static void main(String[] args) throws TwitterException, IOException {
        // Scanner for username input
        Scanner input = new Scanner(System.in);
        System.out.print("Enter username: ");
        String user = input.nextLine();
        System.out.println("");
        
        // Calls getTwitterInstance() method and assigns it to a new Twitter object
        Twitter twitter = ConnectionAPI.getTwitterInstance();
        
        // Calls getUserTimeline() method receiving the twitter instance and inputed username
        getUserTimeline(twitter, user);
    }

    // Method to retrieve last 20 twits from an user
    private static void getUserTimeline(Twitter twitter, String user) throws TwitterException, IOException {
        List<Status> statuses = twitter.getUserTimeline(user);
        String userName = "@" + statuses.get(0).getUser().getScreenName();
        int twitNumber = 1;
        
        // Get the current date and time and format it to desired layout
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.now();
        
        System.out.println("Latest twits: ");
        
        // Scan all the twits and prints them
        for (Status status : statuses) {
            String twits = "Twit #" + twitNumber++ + "\n\n" + status.getText() + "\n________________________________________________";
            System.out.println(twits + "\n");
        } twitNumber = 1;
        
        // Option to save twits to a .txt file
        System.out.print("Do you want to save these twits? [Y/N]: ");
        Scanner input = new Scanner(System.in);
        String option = input.nextLine().toUpperCase();
        
        // Save twits to a file if user inputs yes
        if (option.equals("Y")) {
            FileWriter fileWriter = new FileWriter(new File("files" + File.separator + userName + "_twits.txt"));
            try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                // Write current date and time
                writer.write("Twits from user "  + userName + " at " + dtf.format(ldt) +"\n\n");
                
                // Scan all the twits and writes them to the file
                for (Status status : statuses) {
                    String twits = "Twit #" + twitNumber++ + "\n" + status.getText();
                    writer.write(twits + "\n________________________________________________");
                    writer.newLine();
                    writer.newLine();
                }
            }
        }
    }
}
