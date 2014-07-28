package net.kibotu.android.deviceinfo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import net.kibotu.android.deviceinfo.AsyncCallback;
import net.kibotu.android.deviceinfo.DeviceOld;
import net.kibotu.android.deviceinfo.DisplayHelper;
import net.kibotu.android.error.tracking.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TweetToTwitter implements CustomWebView.UrlHandler {

    private final Context context;

    /**
     * Name to store the users access token
     */
    private static final String PREF_ACCESS_TOKEN = "accessToken";
    /**
     * Name to store the users access token secret
     */
    private static final String PREF_ACCESS_TOKEN_SECRET = "accessTokenSecret";
    /**
     * Consumer Key generated when you registered your app at https://dev.twitter.com/apps/
     */
    private final String CONSUMER_KEY;
    /**
     * Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/
     */
    private final String CONSUMER_SECRET;  // XXX Encode in your app
    /**
     * The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity
     */
    public static final String CALLBACK_URL = "oauth://deviceinfo";
    /**
     * Preferences to store a logged in users credentials
     */
    private volatile SharedPreferences mPrefs;
    /**
     * Twitter4j object
     */
    private volatile Twitter mTwitter;
    /**
     * The request token signifies the unique ID of the request you are sending to twitter
     */
    private RequestToken mReqToken;

    private AsyncCallback cb;

    public TweetToTwitter(final Context context, final String key, final String secret) {
        this.context = context;
        CONSUMER_KEY = key;
        CONSUMER_SECRET = secret;
        onCreate();
    }

    /**
     * Called when the activity is first created.
     */
    public void onCreate() {
        Logger.v("Loading TweetToTwitterActivity");

        // Create a new shared preference object to remember if the user has
        // already given us permission
        mPrefs = context.getSharedPreferences("twitterPrefs", Context.MODE_PRIVATE);
        Logger.v("Got Preferences");

        // Load the twitter4j helper
        mTwitter = new TwitterFactory().getInstance();
        Logger.v("Got Twitter4j");

        // Tell twitter4j that we want to use it with our app
        mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        Logger.v("Inflated Twitter4j");
    }

    /**
     * Button clickables are declared in XML as this projects min SDK is 1.6</br> </br>
     * Checks if the user has given this app permission to use twitter
     * before</br> If so login and enable tweeting</br>
     * Otherwise redirect to Twitter for permission
     */
    public void login(final AsyncCallback cb) {

        this.cb = cb;

        Logger.v("Login Pressed");
        if (mPrefs.contains(PREF_ACCESS_TOKEN)) {
            Logger.v("Repeat User");
            loginAuthorisedUser();
        } else {
            Logger.v("New User");
            loginNewUser();
        }
    }

    /**
     * Create a request that is sent to Twitter asking 'can our app have permission to use Twitter for this user'</br>
     * We are given back the @link mReqToken}
     * that is a unique indetifier to this request</br>
     * The browser then pops up on the twitter website and the user logins in ( we never see this informaton
     * )</br> Twitter then redirects us to @link CALLBACK_URL} if the login was a success</br>
     */
    private void loginNewUser() {
        Logger.v("Request App Authentication");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mReqToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);
                    Logger.v("Starting Webview to login to twitter");
                    CustomWebView.showWebViewInDialog(DeviceOld.context(), mReqToken.getAuthenticationURL(), 0, 0, DisplayHelper.absScreenWidth, DisplayHelper.absScreenHeight, TweetToTwitter.this);
                } catch (final TwitterException e) {
                    Logger.e(e);
                }
            }
        }).start();
    }

    /**
     * The user had previously given our app permission to use Twitter</br>
     * Therefore we retrieve these credentials and fill out the Twitter4j helper
     */
    private void loginAuthorisedUser() {
        String token = mPrefs.getString(PREF_ACCESS_TOKEN, null);
        String secret = mPrefs.getString(PREF_ACCESS_TOKEN_SECRET, null);

        // Create the twitter access token from the credentials we got previously
        AccessToken at = new AccessToken(token, secret);

        mTwitter.setOAuthAccessToken(at);

        cb.callback(null);

        Logger.toast("Welcome back");
    }

    /**
     * Catch when Twitter redirects back to our @link CALLBACK_URL}</br>
     * We use onNewIntent as in our manifest we have singleInstance="true" if we did not the
     * getOAuthAccessToken() call would fail
     * <p/>
     * Twitter has sent us back into our app</br>
     * Within the intent it set back we have a 'key' we can use to authenticate the user
     *
     * @param intent
     */
    public void dealWithTwitterResponse(final Intent intent) {
        final Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
            String oauthVerifier = uri.getQueryParameter("oauth_verifier");

            authoriseNewUser(oauthVerifier, cb);
        }
    }

    /**
     * Create an access token for this new user</br>
     * Fill out the Twitter4j helper</br>
     * And save these credentials so we can log the user straight in next time
     *
     * @param oauthVerifier
     * @param cb
     */
    private void authoriseNewUser(final String oauthVerifier, final AsyncCallback cb) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                AccessToken at = null;
                try {
                    Logger.v("oauthVerifier " + oauthVerifier + " " + mReqToken.getToken() + " " + mReqToken.getTokenSecret());
                    at = mTwitter.getOAuthAccessToken(mReqToken, oauthVerifier);
                } catch (final TwitterException e) {
                    Logger.e(e);
                }

                mTwitter.setOAuthAccessToken(at);

                saveAccessToken(at);
                cb.callback(null);
            }
        }).start();

    }

    /**
     * Send a tweet on your timeline, with a Toast msg for success or failure
     *
     * @param s
     */
    public void tweetMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mTwitter.updateStatus(message);
                } catch (TwitterException e) {
                    Logger.e(e);
                }
                Logger.toast("Tweet Successful!");
            }
        }).start();
    }

    private void saveAccessToken(final AccessToken at) {
        if (at == null)
            return;
        final String token = at.getToken();
        final String secret = at.getTokenSecret();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_ACCESS_TOKEN, token);
        editor.putString(PREF_ACCESS_TOKEN_SECRET, secret);
        editor.commit();
    }

    @Override
    public String getUrlQualifier() {
        return CALLBACK_URL;
    }

    @Override
    public boolean dealWithUrl(final String url) {
        final Uri uri = Uri.parse(url);
        if (uri != null && uri.toString().startsWith(CALLBACK_URL)) { // If the user has just logged in
            final String oauthVerifier = uri.getQueryParameter("oauth_verifier");
            authoriseNewUser(oauthVerifier, cb);
        }
        return true;
    }
}