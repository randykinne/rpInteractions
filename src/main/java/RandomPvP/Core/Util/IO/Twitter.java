package RandomPvP.Core.Util.IO;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class Twitter {

    public void postTweet(String tweet, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        try {
            twitter4j.Twitter t = new TwitterFactory().getInstance();
            {
                t.setOAuthConsumer(consumerKey, consumerSecret);
                t.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));
            }
            t.updateStatus(tweet);
        } catch (TwitterException ex) {
            ex.printStackTrace();
        }
    }

}
