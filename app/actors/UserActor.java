package actors;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.ws.WSClient;
import service.TwitterService;

/**
 * This <code>Actor</code> class manages user and tweets operation whenever a message request is received.
 * 
 * @author Created by Ashwin Soorkeea.
 * @author Modified by Sadaf Najam, Jashan Singh.
 * @since V_1.1
 * @version V1
 * 
 */
public class UserActor extends AbstractActor {
	
	private final ActorRef ws;
	private final String token;
	private final String secret;
	private final TwitterService twitterService;
	private final WSClient wsClient;
	
	/**
	 * 
	 * @param wsOut - an actor reference for message materialization and flow.
	 * @param twitterService - an interface for Twitter API call to retrieve data information.
	 * @param wsClient - an interface for the {@link WSClient} web service client connection.
	 * @param token - String representing the secured token for messaging communication with Twitter.
	 * @param secret - String representing the secret key of the authenticated user for twitter communication.
	 */
	public UserActor(final ActorRef wsOut, final TwitterService twitterService, final WSClient wsClient, final String token, final String secret) {
		this.ws = wsOut;
		this.token = token;
		this.secret = secret;
		this.wsClient = wsClient;
		this.twitterService = twitterService;
	}
	
   /**
   * Create an instance of the class using {@link Props}.
   * 
   * @param wsOut - an actor reference for message materialization and flow.
   * @param twitterService - an interface for Twitter API call to retrieve data information. 
   * @param wsClient - an interface for the {@link WSClient} web service client connection. 
   * @param token - String representing the secured token for messaging communication with Twitter. 
   * @param secret - String representing the secret key of the authenticated user for twitter communication.
   * @return Props - an instance of {@link TweetUserProfileActor}.
   */
	public static Props props(final ActorRef wsOut, final TwitterService twitterService, final WSClient wsClient, 
			final String token, final String secret) {
		return Props.create(UserActor.class, wsOut, twitterService, wsClient, token, secret);
	}

	 /**
	 * @Method searchTweets
	 * connect to the Twitter stream and handle incoming messages.
	 * @param searchCriteria JSON class holding String for the searching element
	 * validating the searching element them before sending them to the child actor for broadcasting.
	 * @code {TweetSearchActor.RequestTweetMessage(twitterService, wsClient, searchKeyword, token, secret)}
	 * @code {tweetActor.tell()} send a message asynchronously and return immediately
	 * @return {@code CompletionStage<JsonNode>}
	 */
	
	/**
	 * Performs a search for tweets with search terms passed as argument of type {@link JsonNode}.
	 * The method performs the following action:
	 * 1. Retrieves the search term from the JsonNode.
	 * 2. Creates a sub actor of type {@link TweetSearchActor}.
	 * 3. Builds a message protocol {@link TweetSearchActor.RequestTweetMessage}
	 * 4. Calls a sub actor {@link TweetSearchActor} by passing the message protocol constructed previously.
	 *   
	 * @param searchCriteria - {@link JsonNode} containing the search terms.
	 */
	private void searchTweets(JsonNode searchCriteria) {			
		String searchKeyword = searchCriteria.get("symbol").asText();		
		if(searchKeyword.trim().length() > 0) {
			ActorRef tweetActor = getContext().actorOf(TweetSearchActor.props(ws));
			TweetSearchActor.RequestTweetMessage search = new TweetSearchActor.RequestTweetMessage(twitterService, wsClient, searchKeyword, token, secret);
			tweetActor.tell(search, self());	
		}		
	}
	
	/**
     * Gets called just before an actor class is created.
     */
	@Override
	public void preStart() throws Exception {
		System.out.println("UserActor actor started.");		
	}
	
	/**
     * Gets called just before an actor class is destroyed.
     */
	@Override
	public void postStop() throws Exception {
		System.out.println("UserActor actor stopped.");
	}

	/**
     * Performs the following actions:
     * 1. Whenever a {@link JsonNode} message protocol is received, it performs a call on the get
     * search tweets, @see searchTweets.
     */ 
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(JsonNode.class, this::searchTweets)	
				.build();
	}
}
