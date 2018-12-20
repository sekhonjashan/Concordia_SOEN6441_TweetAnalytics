package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * General helper methods to parse the JsonNode model.  
 * 
 * @author Jashan
 * @since V1
 * @version 1.0.0
 * 
 */
public class JsonHelper {
	/**
	 * Builds a {@code Map} containing "text" as key and "user" node element as value
	 * for the list of "statuses" present in the {@link com.fasterxml.jackson.databind.JsonNode}.
	 *  
	 * @param rootNode - {@link com.fasterxml.jackson.databind.JsonNode}
	 * @return {@code List<Map<String, JsonNode>>}
	 */
	public static List<Map<String, JsonNode>> recentSearchTweets =  new ArrayList<Map<String, JsonNode>>();
	public static List<String> searchKeyword = new ArrayList<String>();
	
	public static List<Map<String, JsonNode>> groupByText(JsonNode rootNode) {
		Map<String, JsonNode> map = new HashMap<>();
		if(rootNode != null) {
			for (JsonNode jsonStatus : rootNode.get("statuses")) {				
				map.put(jsonStatus.findValue("text").asText(), jsonStatus.findValue("user"));
			}
			recentSearchTweets.add(0 , map);
		}
		return recentSearchTweets;
	}
	
	/**
	 * Builds a {@code Map} containing "profile_image_url" as key and "name" as value
	 * for the list of "user" present in the {@link com.fasterxml.jackson.databind.JsonNode}.
	 *  
	 * @param rootNode - {@link com.fasterxml.jackson.databind.JsonNode}
	 * @return {@code Map<String, String>}
	 */
	public static Map<String, String> groupByProfileImage(JsonNode rootNode) {
		Map<String, String> map = new HashMap<>();
		for (JsonNode jsonNode : rootNode) {
			map.put(jsonNode.findValue("user").findValue("profile_image_url").asText().replace("normal", "400x400"),
					jsonNode.findValue("user").findValue("name").asText());
		}
		return map;
	}
	
	/**
	 * Builds a {@code Map} containing "location" as key and "description" as value
	 * for the list of "user" present in the {@link com.fasterxml.jackson.databind.JsonNode}.
	 *  
	 * @param rootNode - {@link com.fasterxml.jackson.databind.JsonNode}
	 * @return {@code Map<String, String>}
	 */
	public static Map<String, String> groupByLocDesc(JsonNode rootNode) {
		Map<String, String> map = new HashMap<>();
		for (JsonNode jsonNode : rootNode) {
			map.put(jsonNode.findValue("user").findValue("location").asText(),
					jsonNode.findValue("user").findValue("description").asText());
		}
		return map;
	}
	
	/**
	 * Builds a {@code Map} containing "created_at" as key and "text" as value
	 * for the list present in the {@link com.fasterxml.jackson.databind.JsonNode}.
	 *  
	 * @param rootNode - {@link com.fasterxml.jackson.databind.JsonNode}
	 * @return {@code Map<String, String>}
	 */
	public static Map<String, String> groupByCreatedAt(JsonNode rootNode) {
		Map<String, String> map = new HashMap<>();
		for (JsonNode jsonNode : rootNode) {
			map.put(jsonNode.findValue("created_at").asText(), jsonNode.findValue("text").asText());
		}
		return map;
	}
}
