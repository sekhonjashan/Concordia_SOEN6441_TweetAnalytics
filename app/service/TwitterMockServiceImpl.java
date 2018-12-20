package service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import play.libs.ws.WSClient;

/**
 * The class is an implementation of the interface TwitterService. This mock class implementation is used specially
 * for Unit Testing. Instead of using live Twitter API, the methods loads data from a data file simulating a live
 * Twitter API call.
 * 
 * @author Ashwin Soorkeea
 * @since V1
 * @version V_1.0
 */
public class TwitterMockServiceImpl implements TwitterService {

	/**
	 * @see TwitterService#CompletionStage<JsonNode> getTweetsAsync(WSClient wsClient, String Keyword, String token, String secret)
	 * @since V_1.5
	 * 
	 */	
	@Override
	public CompletionStage<JsonNode> getTweetsAsync(WSClient wsClient, String hastag, String token, String secret) {		
		return CompletableFuture.supplyAsync(() -> {
			JsonNode jsonNode = null;
			try {				
				String fullPath = Paths.get(".").toAbsolutePath().resolve("mock").resolve("tweets.txt").toString();
				ObjectMapper mapper = new ObjectMapper();
				jsonNode = mapper.readTree(Files.readAllBytes(Paths.get(fullPath)));				
			} catch(Exception e) {
				e.printStackTrace();
			}
			return jsonNode;
		}).exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});		
	}

	/**
	 * {@code TwitterService#CompletionStage<JsonNode> getHomeTimeLineAsync(WSClient wsClient, String token, String secret)}
	 */
	@Override
	public CompletionStage<JsonNode> getHomeTimeLineAsync(WSClient wsClient, String token, String secret) {
		return CompletableFuture.supplyAsync(() -> {
			JsonNode jsonNode = null;
			try {				
				String fullPath = Paths.get(".").toAbsolutePath().resolve("mock").resolve("userTimeLine.txt").toString();
				ObjectMapper mapper = new ObjectMapper();
				jsonNode = mapper.readTree(Files.readAllBytes(Paths.get(fullPath)));
				//System.out.println(jsonNode);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return jsonNode;
		}).exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});	
	}

	/**
	 * {@code TwitterService#CompletionStage<JsonNode> getUserTimelineAsync(WSClient wsClient, String tag, String token, String secret)}
	 */
	@Override
	public CompletionStage<JsonNode> getUserTimelineAsync(WSClient wsClient, String tag, String token, String secret) {
		return CompletableFuture.supplyAsync(() -> {
			JsonNode jsonNode = null;
			try {				
				String fullPath = Paths.get(".").toAbsolutePath().resolve("mock").resolve("userTimeLine.txt").toString();
				ObjectMapper mapper = new ObjectMapper();
				jsonNode = mapper.readTree(Files.readAllBytes(Paths.get(fullPath)));
				//System.out.println(jsonNode);
			} catch(Exception e) {
				e.printStackTrace();
			}
			return jsonNode;
		}).exceptionally(ex -> {
			ex.printStackTrace();
			return null;
		});	
	}

}
