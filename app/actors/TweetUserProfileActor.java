package actors;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Json;
import play.libs.ws.WSClient;
import service.TwitterService;

/**
 * This <code>Actor</code> class is used to perform twitter user profile get information. An  {@link ActorRef} representing
 * an actor flow is passed as argument in the constructor for message processing.
 * The class also extends the <code>AbstractActor</code>.
 * The class uses the message protocol {@link RequestUserProfileMessage} as an inner class which is used to communicate 
 * to the twitter service for the twitter get information operation.
 * 
 * @author Created by Ashwin Soorkeea.
 * @author Modified by Sadaf Najam, Jashan Singh.
 * @since V_1.1
 * @version V1
 * 
 */
public class TweetUserProfileActor extends AbstractActor {

	private ActorRef actorRef;
	
	/**
	 * Constructor using {@link ActorRef} which represents an Actor Flow.
	 * 
	 * @param actorRef - of type ActorRef.
	 */
	public TweetUserProfileActor(ActorRef actorRef) {
		this.actorRef = actorRef;
	}
	
	/**
	 * Create an instance of the class using {@link Props}.
	 *  
	 * @param actorRef - of type ActorRef, the actorFlow.
	 * @return Props - an instance of {@link TweetUserProfileActor}.
	 */
	public static Props props(final ActorRef actorRef) {
		return Props.create(TweetUserProfileActor.class, actorRef);
	}
	
	/**
	 * A message protocol of the actor class. It contains all information relevant for making
	 * a request for a get user profile information on Twitter.
	 */
	public static class RequestUserProfileMessage {
		private final String tag;
		private final String token;
		private final String secret;
		private final TwitterService twitterService;
		private final WSClient ws;
		
		/**
		 * Constructor for the <code>RequestUserProfileMessage</code> message protocol.
		 * 
		 * @param twitterService - an interface for Twitter API call to retrieve data information.
		 * @param ws - an interface for the {@link WSClient} web service client connection.
		 * @param tag - String representing the user identifier on Twitter.
		 * @param token - String representing the secured token for messaging communication with Twitter.
		 * @param secret - String representing the secret key of the authenticated user for twitter communication.
		 */
		public RequestUserProfileMessage(TwitterService twitterService, WSClient ws, String tag, String token, String secret) {
			this.tag = tag;
			this.token = token;
			this.secret = secret;
			this.twitterService = twitterService;
			this.ws = ws;
		}
	}
	
	/**
	 * Calls the {@link TwitterService} by passing information stored in the message protocol {@link RequestUserProfileMessage}.
	 * The twitter service then returns a {@link JsonNode} asynchronously which is then converted to {@link ObjectNode}
	 * which is then sent to the actor flow, through the {@link ActorRef}.
	 * 
	 * @param RequestUserProfileMessage - tweetMessage representing the message protocol.
	 */
	private void getUserProfile(RequestUserProfileMessage req) throws Exception {
		CompletionStage<JsonNode> jsonResult = req.twitterService.getTweetsAsync(req.ws, req.tag, req.token, req.secret);
		JsonNode userProfile = jsonResult.toCompletableFuture().get();
		System.out.println(userProfile);
		final ObjectNode responseTweetMessage = Json.newObject();		
		responseTweetMessage.set("userProfile", userProfile);
		actorRef.tell(responseTweetMessage, self());
	}
	
	/**
     * Gets called just before an actor class is created.
     */
	@Override
	public void preStart() throws Exception {
		System.out.println("TweetUserProfileActor actor started.");	
	}
	
	/**
     * Gets called just before an actor class is destroyed.
     */
	@Override
	public void postStop() throws Exception {
		System.out.println("TweetUserProfileActor actor stopped.");
	}
	
	/**
     * Performs the following actions:
     * 1. Whenever a {@link RequestUserProfileMessage} message protocol is received, it performs a call on the get
     * user profile information, @see getUserProfile(RequestUserProfileMessage req).
     */ 
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(RequestUserProfileMessage.class, this::getUserProfile)				
				.build();
	}

}
