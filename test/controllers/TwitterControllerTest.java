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
import service.TwitterService;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 * This class is used to unit test the TwitterController class.
 * 
 * @author Sadaf Najam, Jashan Singh
 *
 */
public class TwitterControllerTest extends WithApplication {

	private static Application testApp;
	private FormFactory formFactory;
	// private final WSClient ws;
	// private HttpExecutionContext ec;

	/**
	 * Initializes the {@code TwitterService} interface with the {@code TwitterMockServiceImpl} implementation.
	 */
	@BeforeClass
	public static void initTestApp() {
		testApp = new GuiceApplicationBuilder().overrides(bind(TwitterService.class).to(TwitterMockServiceImpl.class))
				.build();
	}

	/**
	 * Initializes the objects Http.Context, play.api.mvc.RequestHeader and JavaContextComponents required for the
	 * unit testing. 
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
	 * Validates a call on the router for the path /h and validates that the returned result is the HTML page.
	 */
	 @Test
	    public void testSandboxRoute() {
	    	Helpers.running(Helpers.fakeApplication(), () -> {
	    		play.mvc.Http.RequestBuilder request = Helpers.fakeRequest()
	                    .method("GET")
	                    .uri("/h");
	    		Result result = Helpers.route(testApp, request);
	    		assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
				assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
				assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
				assertThat(Helpers.contentAsString(result), CoreMatchers.is(CoreMatchers.containsString("See what's happening right now!")));
			});
	    }
	 	@Test
	    public void testSandboxWebSocketRoute() {
	    	Helpers.running(Helpers.fakeApplication(), () -> {
	    		play.mvc.Http.RequestBuilder request = Helpers.fakeRequest()
	                    .method("GET")
	                    .uri("ws://localhost:19001/ws");
	    		Result result = Helpers.route(testApp, request);
	    		assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(404)));
				assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
				assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			});
	    }
	 
	 /**
	  * Validates a call on the assets javascript and validates that the returned result is the scripted file.
	  */
	 @Test
	    public void testSandboxWebAssets() {
	    	Helpers.running(Helpers.fakeApplication(), () -> {
	    		play.mvc.Http.RequestBuilder request = Helpers.fakeRequest()
	                    .method("GET")
	                    .uri("/assets/javascript/index.js");
	    		Result result = Helpers.route(testApp, request);
	    		assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
				assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("application/javascript")));
				assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			});
	    }
	 
	 /**
	  * Validates a call on the controller for the sandbox and validates that the returned result is the HTML page.
	  */
	@Test
	public void sandbox() {
		TwitterController controller = new TwitterController(null, null);
		Result result = controller.sandbox();
			assertThat(result.status(), CoreMatchers.is(CoreMatchers.equalTo(OK)));
			assertThat(result.contentType().get(), CoreMatchers.is(CoreMatchers.equalTo("text/html")));
			assertThat(result.charset().get(), CoreMatchers.is(CoreMatchers.equalTo("utf-8")));
			assertThat(Helpers.contentAsString(result), CoreMatchers.is(CoreMatchers.containsString("See what's happening right now!")));
	}
	/**
	 * Stop the application after the tests execution.
	 */
	@AfterClass
	public static void stopTestApp() {
		Helpers.stop(testApp);
	}
}
