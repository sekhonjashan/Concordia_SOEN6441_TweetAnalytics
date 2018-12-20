package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static play.inject.Bindings.bind;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import io.gatling.http.request.builder.RequestBuilder;
import models.Tweet;
import play.Application;
import play.core.j.JavaContextComponents;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import service.TwitterMockServiceImpl;
import service.TwitterOAuth;
import service.TwitterService;
import util.SessionUtils;
import play.mvc.Http.Session;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import play.libs.oauth.OAuth.RequestToken;
import play.mvc.Http.Session;

/**
 * This class is used to unit test the TwitterTest class.
 * 
 * @author Sadaf Najam, Jashan Singh
 *
 */
public class TwitterTest extends WithApplication {

	private static Application testApp;
	private FormFactory formFactory;
	static Session session;
	// private final WSClient ws;
	// private HttpExecutionContext ec;

	/**
	 * Initializes the {@code TwitterService} interface with the
	 * {@code TwitterMockServiceImpl} implementation.
	 */
	@BeforeClass
	public static void initTestApp() {
		testApp = new GuiceApplicationBuilder().overrides(bind(TwitterService.class).to(TwitterMockServiceImpl.class))
				.build();
		Map<String, String> mapSession = new HashMap<>();
				session = new Session(mapSession);
				Map<String, String> mapSession1 = new HashMap<>();
				session = new Session(mapSession1);
	}

	/**
	 * Initializes the objects Http.Context, play.api.mvc.RequestHeader and
	 * JavaContextComponents required for the unit testing.
	 */
	@Before
	public void setUp() {
		// play.mvc.Http.RequestBuilder fakeRequest = Helpers.fakeRequest(Call action);
		// without it the test cry -> java.lang.RuntimeException: There is no HTTP
		// Context available from here.
		Map<String, String> flashData = Collections.emptyMap();
		Map<String, String> sessionData = Collections.emptyMap();
		Map<String, Object> argData = Collections.emptyMap();
		Http.Request requestMock = mock(Http.Request.class);
		Long id = 2L;
		play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
		JavaContextComponents contextComponents = app.injector().instanceOf(JavaContextComponents.class);

		Http.Context context = new Http.Context(id, header, requestMock, sessionData, flashData, argData,
				contextComponents);
		Http.Context.current.set(context);
	}

	/**
	 * Validates a call on the router for the path / and validates that the returned
	 * result is the HTML page.
	 */
	@Test
	public void testIntialRoute() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder request = Helpers.fakeRequest().method("GET").uri("/");

			Result result = Helpers.route(testApp, request);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
			assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
			assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			assertThat(Helpers.contentAsString(result),
					CoreMatchers.is(CoreMatchers.containsString("Welcome to TwitterAnalytics")));
		});
	}

	// #bad-route
	/**
	 * Validates a call on the router for the path /xx/Kiwi and validates that the
	 * returned result is the HTML page.
	 */
	@Test
	public void testBadRoute() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder request = Helpers.fakeRequest().method("GET").uri("/xx/Kiwi");

			Result result = Helpers.route(testApp, request);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(404)));
		});
	}

	/**
	 * Validates a call on the router for the path /twitter/homeTimeline and
	 * validates that the returned result is response code 303.
	 */
	@Test
	public void testHomeTimelineRoute() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			SessionUtils.saveToken(session, "1234567890");
			play.mvc.Http.RequestBuilder request = Helpers.fakeRequest().method("GET").uri("/twitter/homeTimeline");
			
			Result result = Helpers.route(testApp, request);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(303)));
		});
	}

	/**
	 * Validates a call on the router for the path /twitter/auth and validates that
	 * the returned result is response code 303.
	 */
	@Test
	public void testAuthRoute() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder request = Helpers.fakeRequest().method("GET").uri("/twitter/auth");

			Result resultObj = Helpers.route(testApp, request);
			assertThat(resultObj.status(), CoreMatchers.is(CoreMatchers.equalTo(303)));
			System.out.println();
			play.mvc.Http.RequestBuilder requ = Helpers.fakeRequest().method("GET").uri(resultObj.headers().get("LOCATION"));
			Result result = Helpers.route(testApp, requ); 
			assertThat(result.contentType().get(),
			 CoreMatchers.is(CoreMatchers.equalTo("text/html")));
			 assertThat(result.charset().get(),
			 CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			 assertThat(Helpers.contentAsString(result),
			 CoreMatchers.is(CoreMatchers.containsString("Action Not Found")));
		});
	}
	
	/**
	 * Validates a call on the controller for the {@code index()} action and
	 * validates that the returned result correspond to the welcome page.
	 */
	@Test
	public void index() {
		Twitter controller = new Twitter(null, null, null);
		Result result = controller.index();
		assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
		assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
		assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
		assertThat(Helpers.contentAsString(result),
				CoreMatchers.is(CoreMatchers.containsString("Welcome to TwitterAnalytics")));
	}

	/**
	 * Validates a call on the router for the
	 * {@code controllers.routes.Twitter.tweets()} action and validates that the
	 * returned result correspond to the search tweet page.
	 */
	@Test
	public void welcome() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(controllers.routes.Twitter.tweets());
			Result result = Helpers.route(testApp, mockActionRequest);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
			assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
			assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			assertThat(Helpers.contentAsString(result), CoreMatchers.is(CoreMatchers.containsString("Search Tweets")));
		});
	}

	/**
	 * Validates a call on the router for the
	 * {@code controllers.routes.Twitter.auth()} action and validates that the
	 * returned result correspond to the {@code SEE_OTHER}.
	 */
	/*
	 * reverse routing and validating the redirection status : 300
	 */
	@Test
	public void testauthRedirection() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder mockActionRequest = Helpers.fakeRequest(controllers.routes.Twitter.auth());
			Result result = Helpers.route(testApp, mockActionRequest);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(SEE_OTHER)));
		});
	}
	
	/**
	 * Validates a call on the router for the
	 * {@code controllers.routes.Twitter.homeTimeline()} action and validates that
	 * the returned result correspond to the {@code SEE_OTHER}.
	 */
	@Test
	public void testReauthRedirection() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder request = Helpers.fakeRequest().method("GET")
					.uri("https://api.twitter.com/oauth/authorize?oauth_token=k8bzdgAAAAAA4vTYAAABYqtVTHg");
			play.mvc.Http.RequestBuilder mockActionRequest = Helpers
					.fakeRequest(controllers.routes.Twitter.homeTimeline());
			Result result = Helpers.route(testApp, mockActionRequest);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(SEE_OTHER)));
		});
	}

	/**
	 * Validates a call on the controller for the {@code homeTimeline} action and
	 * validates that the returned result correspond to the {@code SEE_OTHER}.
	 */
	@Test
	public void testhomeTimelineRedirection() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			play.mvc.Http.RequestBuilder mockActionRequest = Helpers
					.fakeRequest(controllers.routes.Twitter.homeTimeline());
			Result result = Helpers.route(testApp, mockActionRequest);
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(SEE_OTHER)));
		});
	}

	/**
	 * Validates a call on the controller for the
	 * {@code controllers.routes.Twitter.getTweets} action and validates that the
	 * returned result correspond to the matching results.
	 */
	@Test
	public void testgetTweets() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			Result result = Helpers.route(testApp, Helpers.fakeRequest(controllers.routes.Twitter.getTweets("bank")));
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
			assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
			assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			// assertThat(Helpers.contentAsString(result),
			// CoreMatchers.is(CoreMatchers.containsString("bank")));
		});
	}

	/**
	 * Validates a call on the controller for the {@code controllers.routes.Twitter.userTimeline} action and
	 * validates that the returned result correspond to the output page.
	 */
	@Test
	public void testuserTimeline() {
		Helpers.running(Helpers.fakeApplication(), () -> {
			Result result = Helpers.route(testApp,
					Helpers.fakeRequest(controllers.routes.Twitter.userTimeline("biaasarfraz")));
			System.out.println(Helpers.contentAsString(result));
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
			assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
			assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			// assertThat(Helpers.contentAsString(result),
			// CoreMatchers.is(CoreMatchers.containsString("biaasarfraz")));
		});
	}

	/**
	 * Validates a call on the twitter search page form and
	 * validates that the returned result correspond to the {@code SEE_OTHER}.
	 */
	@Test
	public void testsearchTweets() throws Exception {
		Helpers.running(Helpers.fakeApplication(), () -> {
			String registeredUserName = "concordia";
			HashMap<String, String> accountForm = new HashMap<>();
			accountForm.put("name", registeredUserName);
			Http.RequestBuilder request = new Http.RequestBuilder().method(Helpers.POST).bodyForm(accountForm)
					.uri(controllers.routes.Twitter.searchTweets().url());
			Result result = Helpers.route(testApp, request);
			System.out.println(Helpers.contentAsString(result));
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(SEE_OTHER)));
		});
	}

	/**
	 * Validates the {@code Tweet} model class.
	 */
	@Test
	public void testModel() {
		Tweet _tweet = new Tweet("sample");
		assertEquals("sample", _tweet.name);
	}

	/**
	 * Stop the application after the tests execution.
	 */
	@AfterClass
	public static void stopTestApp() {
		Helpers.stop(testApp);
	}
}
