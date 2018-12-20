package util;

import static org.junit.Assert.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsMapContaining;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The class uses Hamcrest collection to test the Map collection
 * 
 * @see {@linkplain https://objectpartners.com/2013/09/18/the-benefits-of-using-assertthat-over-other-assert-methods-in-unit-tests/}
 * @author Group
 *
 */
public class JsonHelperTest {
	
	static JsonNode jsonNode;
	/**
	 * Initializes the Mock data ,retrive from .txt file and Objects required for the unit testing.
	 */
	@BeforeClass 
	public static void init() throws Exception {
		String fullPath = Paths.get(".").toAbsolutePath().resolve("mock").resolve("tweets.txt").toString();		
		ObjectMapper mapper = new ObjectMapper();
		jsonNode = mapper.readTree(Files.readAllBytes(Paths.get(fullPath)));
	}
	/**
	 * Validates a {@code Map} containing "text" as key and "user" one node element as value
	 * for the list of "user" present in the Mock data
	 */
	@Test
	public void JsonNodeGroupByText() {		
		List<Map<String, JsonNode>> list = JsonHelper.groupByText(jsonNode);
		Map<String, JsonNode> map = list.get(0);
		//1. Test size
		assertThat(map.size(), CoreMatchers.is(6));
		//2. Test map key
		assertThat(map, IsMapContaining.hasKey("@Banks ?"));
		
		JsonNode json = map.get("@Banks ?");
		//3. Test json user node not null
		assertThat(json, CoreMatchers.notNullValue());
		//4. Test json user name 		
		assertThat("Ed Shepherd", CoreMatchers.is(json.findValue("name").asText()));
	}
	/**
	 * Validates a {@code Map} containing node element as null
	 */
	@Test
	public void JsonNodeGroupByTextNull() {		
		List<Map<String, JsonNode>> list = JsonHelper.groupByText(null);
		Map<String, JsonNode> map = list.get(0);
		//1. Test map size
		assertThat(map.size(), CoreMatchers.is(6));
	}
	
	/**
	 * Validates a {@code Map} containing "profile_image_url" as key and "name" one node element as value
	 * for the list of "user" present in the Mock data
	 */
	@Test
	public void JsonNodeGroupByProfileImage() {			
		JsonNode jsonUsers = jsonNode.get("statuses");
		Map<String, String> map = JsonHelper.groupByProfileImage(jsonUsers);
		//1. Test size
		assertThat(map.size(), CoreMatchers.is(10));		
		//2. Test map key
		String profileImageUrl = "http://pbs.twimg.com/profile_images/893865773738885120/JFNlCStX_400x400.jpg";
		assertThat(map, IsMapContaining.hasKey(profileImageUrl));
		
		String user = map.get(profileImageUrl);
		//3. Test json user node not null
		assertThat(user, CoreMatchers.notNullValue());
		//4. Test json user name 		
		assertThat("Ed Shepherd", CoreMatchers.is(user));		
	}
	
	/**
	 * Validates a {@code Map} containing "location" as key and "description" as value
	 * for the list of "user" present in the Mock data
	 */
	@Test
	public void JsonNodeGroupByLocDesc() {			
		JsonNode jsonUsers = jsonNode.get("statuses");
		Map<String, String> map = JsonHelper.groupByLocDesc(jsonUsers);
		//1. Test size
		assertThat(map.size(), CoreMatchers.is(4));		
		//2. Test map key
		String location = "England, United Kingdom";
		assertThat(map, IsMapContaining.hasKey(location));
		
		String desc = map.get(location);
		//3. Test json desc		
		assertThat(desc, CoreMatchers.notNullValue());
		//4. Test json desc name 		
		assertThat("", CoreMatchers.is(desc));
	}
	
	/**
	 * Validates a {@code Map} containing "created_at" as key and "text" as value
	 * for the list of "user" present in the Mock data
	 */
	@Test
	public void JsonNodeGroupByCreatedAt() {			
		JsonNode jsonUsers = jsonNode.get("statuses");
		Map<String, String> map = JsonHelper.groupByCreatedAt(jsonUsers);		
		//1. Test size
		assertThat(map.size(), CoreMatchers.is(10));
		//2. Test map key
		String createdAt = "Sun Mar 11 12:19:38 +0000 2018";
		assertThat(map, IsMapContaining.hasKey(createdAt));
		
		String textDesc = map.get(createdAt);
		//3. Test json desc		
		assertThat(textDesc, CoreMatchers.notNullValue());
		//4. Test json desc name 		
		assertThat("RT @Banks: R2, R2, L1, R2, Left, Down, Right, Up, Left, Down, Right, Up", CoreMatchers.is(textDesc));		
	}
}
