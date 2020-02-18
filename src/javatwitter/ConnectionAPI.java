package javatwitter;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Daniel
 */
public class ConnectionAPI {
    // Keys and tokens necessary to connect to Twitter API
    private static final String CONSUMER_KEY = "HDZbUhcAZUehKDGpsTqmUzi2H";
    private static final String CONSUMER_SECRET = "YVUZtoJ1gg3VQFRtFNZJgTvIc8afLnio7tnM84CMIEX7VcKvOb";
    private static final String ACCESS_TOKEN = "1015941676974706688-R3PN3S5OhspZg4y7WwxoSZVFUiv2Zt";
    private static final String ACCESS_TOKEN_SECRET = "Sr7mrzqS4kC05Sx8xrIPxFgtS7LqEufuWt6R3YKp9ZL29";

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
}
