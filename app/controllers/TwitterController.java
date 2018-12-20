package controllers;

import com.google.inject.Inject;

import actors.UserActor;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import service.TwitterService;
import util.SessionUtils;
/**
 * <h1> Reactive TweetAnalytics !</h1> 
 * Reactive TweetAnalytics implements an application that simply searches the 
 * tweets for a specific keyword and displays the continues stream results screen.
 * If another search is made tweets are added to the same result page reactively.
 * Application allows to navigate to the user's profile and displays basic information
 * about the tweet user.
 * Also, on the profile page latest 10 tweets are displayed.
 * Application is build using play framework 2.6. 
 * 
 * <p>
 * <b>NOTE:</b> Application uses twitter normal search API , which allows only tweets
 * search in past week only.
 * 
 * @author Created by Ashwin Soorkeea. 
 * @author Modified by Sadaf Najam, Jashan Singh.
 * @version V1
 * @since V_1.1
 * 
 * Class constructor 
 * {@code #TwitterController(ActorSystem, WSClient)Twitter}
 * 
 *  
 */
public class TwitterController extends Controller {
	
	@Inject private ActorSystem actorSystem;
	@Inject private Materializer materializer;
	@Inject TwitterService twitterService;
	
	private WSClient wsClient;	
	private String token;
	private String secret;
	
	/**
	 * Constructor
	 * @param system - An instance of the {@link ActorSystem} initialized when the object is created through injection. 
	 * @param wsClient - an interface for the {@link WSClient} web service client connection.
	 */
	@Inject
	public TwitterController(ActorSystem system, WSClient wsClient) {
		this.wsClient = wsClient;
	}
	
	/**
	 * Retrieves the security tokens from the user session and renders the page.	
	 * @return Renders sandbox HTML page 
	 */
	public Result sandbox() {
		secret = SessionUtils.getSecret(session());
		token = SessionUtils.getToken(session());
        return ok(views.html.sandbox.render(request()));
    }
	
	/**
	 * Accepts the Json request from the websocket connection and transfers the request to the {@link UserActor}.	
	 * @return WebSocket - response message materialized into Json.
	 */
	public WebSocket ws() {
		return WebSocket.Json.accept(request -> ActorFlow.actorRef(s -> UserActor.props(s, twitterService, wsClient, token, secret), 
				actorSystem, materializer));
	}
}
