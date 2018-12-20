package util;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import play.mvc.Http.Session;

/**
 * This class is used to unit test the SessionUtils class.
 * 
 * @author Sadaf Najam, Jashan Singh
 *
 */
public class SessionUtilsTest {

	static Session session;
	/**
	 * Initializes the objects HTTP session and HashMap required for the unit testing. 
	 * 
	 */
	@BeforeClass 
	public static void init() throws Exception {
		Map<String, String> mapSession = new HashMap<>();
		session = new Session(mapSession);
	}
	
	/**
	 * Validates a token key is present in the HTTP session object
	 * {@code true} if token is present in {@code play.mvc.Session} object.
	 */ 
	@Test
	public void isSessionActive() {
		SessionUtils.saveToken(session, "1234567890");
		assertThat(SessionUtils.isSessionActive(session), CoreMatchers.is(true));
	}
	
	/**
	 * Validates a token key is present in the HTTP session object
	 * {@code true} if token is not present in {@code play.mvc.Session} object.
	 */ 
	 
	@Test
	public void isSessionActiveFalse() {
		Map<String, String> mapSession = new HashMap<>();
		session = new Session(mapSession);
		assertThat(SessionUtils.isSessionActive(session), CoreMatchers.is(false));
	}
	
	/**
	 * Validates that saved token value is presnt in the HTTP session object.
	 */ 
	@Test
	public void getToken() {
		SessionUtils.saveToken(session, "33465645655");
		assertThat(SessionUtils.getToken(session), CoreMatchers.is(CoreMatchers.equalTo("33465645655")));
	}
	
	/**
	 * Validates that saved secrect value is presnt in the HTTP session object.
	 */ 
	@Test
	public void getSecret() {
		SessionUtils.saveSecret(session, "7676878768");
		assertThat(SessionUtils.getSecret(session), CoreMatchers.is(CoreMatchers.equalTo("7676878768")));
	}
	
	/**
	 * Validates that values Stores the secret and token  in the HTTP session object.
	 */ 
	@Test
	public void saveKeys() {
		SessionUtils.saveKeys(session, "2222322", "44454544");
		assertThat(SessionUtils.getToken(session), CoreMatchers.is(CoreMatchers.equalTo("2222322")));
		assertThat(SessionUtils.getSecret(session), CoreMatchers.is(CoreMatchers.equalTo("44454544")));
	}
}
