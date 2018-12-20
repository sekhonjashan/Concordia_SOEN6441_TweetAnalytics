package service;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.ws.WSClient;

/**
 * The class is an implementation of the interface TwitterService.
 * 
 * 
 * @author Sadaf
 * @since V1
 * @version V_1.5
 * @code {service.TwitterService;}
 * @see <a href="https://developer.twitter.com/en/docs/tweets/search/api-reference/get-search-tweets.html</a>
 */
public class TwitterServiceImpl implements TwitterService {

	private final String JSON_URL_HOME_TIMELINE = "https://api.twitter.com/1.1/statuses/home_timeline.json";
	private final String JSON_URL_USER_TIMELINE = "https://api.twitter.com/1.1/statuses/user_timeline.json";
	private final String JSON_URL_SEARCH_TWEETS = "https://api.twitter.com/1.1/search/tweets.json";
	
	private final String COUNT = "10";
	
	
	/**
	 * @see TwitterService#CompletionStage<JsonNode> getTweetsAsync(WSClient wsClient, String Keyword, String token, String secret)
	 * @since V_1.5
	 * 
	 */	
	
	public CompletionStage<JsonNode> getTweetsAsync(WSClient wsClient, String Keyword, String token, String secret) {
		
		StringBuffer stringUrl = new StringBuffer(JSON_URL_SEARCH_TWEETS);
		//https://api.twitter.com/1.1/search/tweets.json?q=twitterdev%20new%20premium
		//https://twitter.com/search?q=twitterdev%20new%20premium
		
   	 	stringUrl.append("?").append("q=").append(Keyword);
		
		stringUrl.append("&").append("count=").append("10");
	
   	 	TwitterOAuth twitterOAuth = TwitterOAuth.getInstance();
   	
   	 	return wsClient.url(stringUrl.toString())
   	 			.sign(twitterOAuth.getSignatureCalculator(token, secret))
   	 			.get()
   	 		    .thenApply(result -> {
   	 		    	return result.asJson();   	 		    	
   	 		    });
	}

	/**
	 * {@code TwitterService#CompletionStage<JsonNode> getHomeTimeLineAsync(WSClient wsClient, String token, String secret)}
	 */
	public CompletionStage<JsonNode> getHomeTimeLineAsync(WSClient wsClient, String token, String secret) {
		TwitterOAuth twitterOAuth = TwitterOAuth.getInstance();
		
		return wsClient.url(JSON_URL_HOME_TIMELINE)
					   .sign(twitterOAuth.getSignatureCalculator(token, secret))
	                   .get()
	                   .thenApply(result -> result.asJson());
	}
	
	/**
	 * {@code TwitterService#CompletionStage<JsonNode> getUserTimelineAsync(WSClient wsClient, String tag, String token, String secret)}
	 */
	public CompletionStage<JsonNode> getUserTimelineAsync(WSClient wsClient, String tag, String token, String secret) {
		StringBuffer stringUrl = new StringBuffer(JSON_URL_USER_TIMELINE);
		stringUrl.append("?").append("screen_name=").append(tag);
		stringUrl.append("&").append("count=").append(COUNT);
		
		TwitterOAuth twitterOAuth = TwitterOAuth.getInstance();
		
		
		return wsClient.url(stringUrl.toString())
				   	   .sign(twitterOAuth.getSignatureCalculator(token, secret))
				   	   .get()
				   	   .thenApply(result -> result.asJson());
	}
}
