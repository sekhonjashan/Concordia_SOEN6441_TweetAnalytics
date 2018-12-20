package service;

import java.util.Optional;

import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.ConsumerKey;
import play.libs.oauth.OAuth.OAuthCalculator;
import play.libs.oauth.OAuth.RequestToken;
import play.libs.oauth.OAuth.ServiceInfo;
import play.libs.ws.WSSignatureCalculator;

/**
 * This class provides the methods to allow the application to authenticate to the service provider based on the tokens.
 * 
 * The flow is as follows:
 * 1. Gets a request token from the server.
 * 2. Redirects this user to the service provider for application rights.
 * 3. Service Provider provides a verified token in the callback URL.
 * 4. Exchanges an access token using verifier token and request token, .
 * 5. Uses Access token to access protected data. 
 * 
 * Once the access token is granted, it is used in the signature during an HTTP Request. 
 * 
 * The class follows the singleton design pattern.
 * 
 *  {@link https://www.playframework.com/documentation/2.6.x/JavaOAuth
 * 	 	https://developer.twitter.com/en/docs/basics/authentication/api-reference/authenticate}
 * 
 * @author Sadaf
 * @since V1
 * @version V_1.2
 * 
 */

public class TwitterOAuth {
	
	/**
	 * CONSUMER KEY refers to Consumer Key (API Key) and Consumer Secret Key (API Secret) in the Keys and Access Tokens on Twitter
	 * Application Settings.
	 */

	private final ConsumerKey CONSUMER_KEY = new ConsumerKey("PfGngTU5iHTqfadA2434li8hV", "eDpAbFOFP9Xp7nDU2DCryVYIRnwsndvqTDKiinbffNiQMv4jS3");	
	
	/**
	 * Different access URL for the tokens and authorization from Twitter.
	 */
	
	private final ServiceInfo SERVICE_INFO = new ServiceInfo(
			"https://api.twitter.com/oauth/request_token",
			"https://api.twitter.com/oauth/access_token",
            "https://api.twitter.com/oauth/authorize",
            CONSUMER_KEY
    );
	
	private final OAuth OAUTH = new OAuth(SERVICE_INFO);
	
	private static TwitterOAuth singleton = new TwitterOAuth();
	/** 
	 * A private Constructor prevents any other class from instantiating.
	 */
	private TwitterOAuth() { 
			
	}
	/** 
	 * Static 'instance' method 
	 */
	public static TwitterOAuth getInstance( ) {
		 return singleton;	   
	}
	/**
	 * Gets the ConsumerKey - key consumer/secret pair.
	 * @see {@link play.libs.oauth.OAuth.ConsumerKey}
	 * @return CONSUMER_KEY
	 */
	public ConsumerKey getConsumerKey() {
		return CONSUMER_KEY;
	}
	/**
	 * Gets the OAuth
	 * @return OAUTH {@link play.libs.oauth.OAuth}
	 */
	public OAuth getOAuth() {
		return OAUTH;
	}
	
	/**
	 * Retrieves the RequestToken from the OAuth.
	 *  {@link play.libs.oauth.OAuth}
	 * @param callbackURL - callbackUrl defined in Twitter settings.
	 * @return OAUTH {@link play.libs.oauth.OAuth}
	 */
	public RequestToken getRequestToken(String callbackURL) {
		return OAUTH.retrieveRequestToken(callbackURL);
	}
	
	/**
	 * Gets the access token from the OAuth using the verifier token and token/secret key.
	 * @param verifier - String verifier token
	 * @param token - String request token
	 * @param secret - String secret token
	 * @return String - access token.
	 */
	public RequestToken getAccessToken(String verifier, String token, String secret) {
		Optional<RequestToken> requestToken = Optional.ofNullable(new RequestToken(token, secret));
		return OAUTH.retrieveAccessToken(requestToken.get(), verifier);
	}
	
	/**
	 * Gets the OAuthCalculator from the RequestToken and ConsumerKey pair.
	 * @param token - String for the request token
	 * @param secret - String for the secret token
	 * @return WSSignatureCalculator - interface for the OAuthCalculator
	 */
	
	public WSSignatureCalculator getSignatureCalculator(String token, String secret) {
		Optional<RequestToken> requestToken = Optional.ofNullable(new RequestToken(token, secret));
		return (requestToken.isPresent()) ? new OAuthCalculator(getConsumerKey(), requestToken.get()) : null;	
	}
}
