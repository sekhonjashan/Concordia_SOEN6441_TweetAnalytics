package actors;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Json;
import play.libs.ws.WSClient;
import scala.concurrent.duration.Duration;
import service.TwitterService;

/**
 * This <code>Actor</code> class is used to perform twitter search action. An  {@link ActorRef} representing
 * an actor flow is passed as argument in the constructor for message processing.
 * The class also extends the <code>AbstractActorWithTimers</code> to be able to start a periodic timer
 * for refreshing the search action during an interval of time of X seconds, defined in the method {@link preStart()}.
 * The class uses the message protocol {@link RequestTweetMessage} as an inner class which is used to communicate 
 * to the twitter service for the twitter search operation. It also uses the message protocol {@link TickTweetMessage}
 * as a trigger to refresh the search action.
 * 
 * @author Created by Ashwin Soorkeea.
 * @author Modified by Sadaf Najam, Jashan Singh.
 * @since V_1.1
 * @version V1
 * 
 */
public class TweetSearchActor extends AbstractActorWithTimers {

	private ActorRef actorRef;
	private Set<RequestTweetMessage> reqMsgMetaData = new HashSet<>();
	
	/**
	 * Constructor using {@link ActorRef} which represents an Actor Flow.
	 * 
	 * @param actorRef - of type ActorRef.
	 */
	public TweetSearchActor(ActorRef actorRef) {
		this.actorRef = actorRef;
	}
	
	/**
	 * Create an instance of the class using {@link Props}.
	 *  
	 * @param actorRef - of type ActorRef, the actorFlow.
	 * @return Props - an instance of {@link TweetSearchActor}.
	 */
	public static Props props(final ActorRef actorRef) {
		return Props.create(TweetSearchActor.class, actorRef);
	}
	
	/**
	 * A message protocol of the actor class. It contains all information relevant for making
	 * a request search operation on Twitter.
	 */
	public static class RequestTweetMessage {
		private final String searchKeyword;
		private final String token;
		private final String secret;
		private final TwitterService twitterService;
		private final WSClient ws;
		
		/**
		 * Constructor for the <code>RequestTweetMessage</code> message protocol.
		 * 
		 * @param twitterService - an interface for Twitter API call to retrieve data information.
		 * @param ws - an interface for the {@link WSClient} web service client connection.
		 * @param searchKeyword - String representing the search terms on Twitter.
		 * @param token - String representing the secured token for messaging communication with Twitter.
		 * @param secret - String representing the secret key of the authenticated user for twitter communication.
		 */
		public RequestTweetMessage(TwitterService twitterService, WSClient ws, String searchKeyword, String token, String secret) {
			this.searchKeyword = searchKeyword;
			this.token = token;
			this.secret = secret;
			this.twitterService = twitterService;
			this.ws = ws;
		}
	}
	
	/**
	 * A message protocol of the actor class. It is used as a trigger for periodic call for the
	 * search operation.
	 */
	public final static class TickTweetMessage {
		
	}
	
	/**
	 * Calls the {@link TwitterService} by passing information stored in the message protocol {@link RequestTweetMessage}.
	 * The twitter service then returns a {@link JsonNode} asynchronously which is then converted to {@link ObjectNode}
	 * which is then sent to the actor flow, through the {@link ActorRef}.
	 * 
	 * @param RequestTweetMessage - tweetMessage representing the message protocol.
	 */
	private void search(RequestTweetMessage tweetMessage) throws Exception {
		reqMsgMetaData.clear();
		reqMsgMetaData.add(tweetMessage);
		CompletionStage<JsonNode> jsonResult = tweetMessage.twitterService.getTweetsAsync(tweetMessage.ws, tweetMessage.searchKeyword, tweetMessage.token, tweetMessage.secret);
		JsonNode searchResults = jsonResult.toCompletableFuture().get();
		final ObjectNode responseTweetMessage = Json.newObject();		
		responseTweetMessage.set("search", searchResults);
		actorRef.tell(responseTweetMessage, self());
	}
	
	/**
	 * The method performs a search operation if there is any request message
	 * present in the {@link Set<RequestTweetMessage>}.
	 */
	private void refreshClients() throws Exception {
		Optional<RequestTweetMessage> tweetMessage = reqMsgMetaData.stream().findFirst();
		if(tweetMessage.isPresent()) {
			search(tweetMessage.get());
		}
	}
	
   /**
    * Creates a timer class and registers the {@link TickTweetMessage} which gets called periodically
    * for each 5 seconds. The initialization process is perform once when an instance of the class
    * is created. 
    */
	@Override
	public void preStart() throws Exception {
		getTimers().startPeriodicTimer("TweetTimer", new TickTweetMessage(), Duration.create(5, TimeUnit.SECONDS));
		System.out.println("TweetActor actor started.");	
	}
	
   /**
    * Gets called just before an actor class is destroyed.
    */
	@Override
	public void postStop() throws Exception {
		System.out.println("TweetActor actor stopped.");
	}
	
   /**
    * Performs the following actions:
    * 1. Whenever a {@link RequestTweetMessage} message protocol is received, it performs a call on the search
    * {@link search(RequestTweetMessage tweetMessage)}.
    * 2. Whenever a {@link TickTweetMessage} message protocol is received, it performs a call on the refreshing
    * operation {@link refreshClients}.
    */ 
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(RequestTweetMessage.class, this::search)
				.match(TickTweetMessage.class, msg -> {
					refreshClients();
				})
				.build();
	}
}
