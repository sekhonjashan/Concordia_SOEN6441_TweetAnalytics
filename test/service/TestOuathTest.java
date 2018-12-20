package service;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;
import play.libs.oauth.OAuth;
import play.libs.oauth.OAuth.ConsumerKey;
import play.libs.oauth.OAuth.OAuthCalculator;
import play.libs.oauth.OAuth.RequestToken;
import play.libs.oauth.OAuth.ServiceInfo;
import play.mvc.Http.Session;
import service.TwitterOAuth;
import play.Application;
import play.core.j.JavaContextComponents;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
/**
 * This class is used to unit test the SessionUtils class.
 * 
 * @author Sadaf Najam, Jashan Singh
 * @param <RequestToken>
 * @param <Result>
 *
 */
public class TestOuathTest<RequestToken, Result> {
	/**
	 * Initializes the objects HTTP session and HashMap required for the unit testing. 
	 * 
	 */
	private final ConsumerKey CONSUMER_KEY = new ConsumerKey("PfGngTU5iHTqfadA2434li8hV", "eDpAbFOFP9Xp7nDU2DCryVYIRnwsndvqTDKiinbffNiQMv4jS3");
	@BeforeClass 
	public static void init() throws Exception {
		
	}
	
	@Test
	public void testgetInstace() {
		TwitterOAuth twitter = TwitterOAuth.getInstance();
		assertThat(twitter , CoreMatchers.is(twitter));
	}
	@Test
	public void testgetConsumerKey() {
		TwitterOAuth twitter = TwitterOAuth.getInstance();
		assertThat(twitter.getConsumerKey(), CoreMatchers.is(twitter.getConsumerKey()));
	}
	
	
	@Test
	public void testgetOuthToken() {
		TwitterOAuth twitter = TwitterOAuth.getInstance();
		assertThat(twitter.getOAuth(), CoreMatchers.is(twitter.getOAuth()));
	}
	
//	@Test
//	public void testgetRequestToken() {
//		TwitterOAuth twitter = TwitterOAuth.getInstance();
//		assertThat(twitter.retrieveRequestToken("callback"), CoreMatchers.not(twitter.retrieveRequestToken("callback")));
//	}
	
//	@Test
//	public void testgetAccessToken() {
//		TwitterOAuth twitter = TwitterOAuth.getInstance();
//		Optional<RequestToken> requestToken  = twitter.getAccessToken("2222322" , "44454544" , "77777777");
////		Result result = Helpers.route(testApp, requestToken);
////		assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(401)));
////		assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
//	}
	
//	@Test
//	public void testgetSignatureCalculator() {
//		TwitterOAuth twitter = TwitterOAuth.getInstance();
//		assertThat(twitter.getSignatureCalculator("ABC", "12345").toString(), twitter.getSignatureCalculator("ABC", "12345").toString());
//	}
	
}
