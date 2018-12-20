package util;

import play.mvc.Http.Session;

/**
 * General utility methods to manage token/secret key pair for a specific user in a session object.
 * It is used to store, retrieve token/secret values in the HTTP Session object and
 * also verify that the token/secret key pair exist.  
 * 
 *  
 * @author Jashan
 * @since V1
 * @version 1.0.0
 * 
 * @see class {@link play.libs.oauth.OAuth.RequestToken}
 * @see  class {@link play.mvc.Session}
 *
 */

public class SessionUtils {
	/**
	 * Determine if a token key is present in the HTTP session object.
	 * @param session - HTTP {@code play.mvc.Session} 
	 * @return boolean {@code true} if token is present in {@code play.mvc.Session} object. 
	 */
	public static boolean isSessionActive(Session session) {
		return (session.get("token") != null) ? true : false;
	}
	
	/**
	 * Gets the token value from the HTTP session object.
	 * @param session - HTTP {@code play.mvc.Session} 
	 * @return String - the token value.
	 */
	public static String getToken(Session session) {
		return session.get("token");
	}
	
	/**
	 * Gets the secret value from the HTTP session object.
	 * @param session - HTTP {@code play.mvc.Session} 
	 * @return String - the secret value.
	 */
	public static String getSecret(Session session) {
		return session.get("secret");
	}
	
	/**
	 * Stores the secret and token value in the HTTP session object.
	 * 
	 *  {@link saveSecret(Session session, String value)}
	 *  {@link saveToken(Session session, String value)}
	 *  
	 * @param session - HTTP {@link play.mvc.Session} 
	 * @param token - the token value.
	 * @param secret - the secret value.
	 */
	public static void saveKeys(Session session, String token, String secret) {
		saveSecret(session, secret);
		saveToken(session, token);
	}
	
	/**
	 * Stores the secret value in the HTTP session object.
	 * @param session - HTTP {@link play.mvc.Session} 
	 * @param value - the secret value.
	 */
	public static void saveSecret(Session session, String value) {
		session.put("secret", value);
	}
	
	/**
	 * Stores the token value in the HTTP session object.
	 * @param session - HTTP {@link play.mvc.Session} 
	 * @param value - the token value.
	 */
	public static void saveToken(Session session, String value) {
		session.put("token", value);
	}
}
