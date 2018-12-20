package service;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.ws.WSClient;

/**
 * Contains operations performed on the Twitter API service provider to retrieve data information. 
 * 
 * The HTTP operations (GET/POST) is performed using the {@link play.libs.ws.WSClient}. For each
 * action the following stream pipeline methods are used.
 * 1. url 		- Connects @see {@link play.libs.ws.WSClient} for HTTP operations
 * 2. sign 		- Signs the HTTP request using the token/secret key passed as argument. @see {@code TwitterOAuth}
 * 3. get 		- Gets a {@code CompletableStage} for each response.
 * 4. thenApply - Retrieves the JSON element node from the Response Object.
 * 
 * @author Jashan
 * @since V1
 * @version V_1.2
 * 
 */
public interface TwitterService {
	/**
	 * Retrieves a list of Tweets in asynchronous mode based on the search keywords.
	 * @param wsClient - instance of WSClient used to perform the HTTP URL Request operation
	 * @param hastag - String representing the keyword to search for.
	 * @param token - String for the request token use to build the signature of the HTTP Request.
	 * @param secret - String for the secret token use to build signature of the HTTP Request.
	 * @return {@code CompletionStage<JsonNode>} - a list of tweets in Json format.
	 */
	CompletionStage<JsonNode> getTweetsAsync(WSClient wsClient, String hastag, String token, String secret);
	
	/**
	 * Retrieves the HomeTimeLine in asynchronous mode.
	 * @param wsClient - instance of WSClient used to perform the HTTP URL Request operation
	 * @param token - String for the request token use to build the signature of the HTTP Request.
	 * @param secret - String for the secret token use to build signature of the HTTP Request.
	 * @return {@code CompletionStage<JsonNode>} - a list of tweets in Json format.
	 */
	CompletionStage<JsonNode> getHomeTimeLineAsync(WSClient wsClient, String token, String secret);
	
	/**
	 * Gets the information for a twitter user.
	 * @param wsClient - instance of WSClient used to perform the HTTP URL Request operation
	 * @param tag - String representing the twitter client profile identifier to search for.
	 * @param token - String for the request token use to build the signature of the HTTP Request.
	 * @param secret - String for the secret token use to build signature of the HTTP Request.
	 * @return {@code CompletionStage<JsonNode>} - a list of tweets in Json format.
	 */
	CompletionStage<JsonNode> getUserTimelineAsync(WSClient wsClient, String tag, String token, String secret);
}
