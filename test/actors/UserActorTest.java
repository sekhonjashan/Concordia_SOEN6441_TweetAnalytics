package actors;

import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import service.TwitterMockServiceImpl;

/**
 * This class is used to unit test the UserActor class.
 * 
 * @author Ashwin Soorkeea
 *
 */
public class UserActorTest {
static ActorSystem system;
	
	/**
	 * Create an actor system for the unit tests.  
	 */
	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
	}
	
	/**
	 * Destroys the actor system.
	 */
	@AfterClass
	public static void teardown() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}
	
	/**
	 * Performs a search by calling the {@code UserActor} and confirming that at least one response is returned.
	 */
	@Test
	public void testSearch() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef userActor = system.actorOf(UserActor.props(testActor, new TwitterMockServiceImpl(), null, null, null));		
		
		Map<String, String> data = new HashMap<>();
		data.put("symbol", "banks");
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.convertValue(data, JsonNode.class);
				
		userActor.tell(jsonNode, probe.getRef());
		ObjectNode objectNode = probe.expectMsgClass(ObjectNode.class);
		assertThat(objectNode.size(), CoreMatchers.is(1));
	}
	
	/**
	 * Performs a search by calling the {@code UserActor} with no search item and confirming that no response is returned.
	 */
	@Test
	public void testSearchEmptyValue() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef userActor = system.actorOf(UserActor.props(testActor, new TwitterMockServiceImpl(), null, null, null));		
		
		Map<String, String> data = new HashMap<>();
		data.put("symbol", "");
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.convertValue(data, JsonNode.class);
				
		userActor.tell(jsonNode, probe.getRef());
		probe.expectNoMessage();		
	}
	
	/**
	 * Performs a search by calling the {@code UserActor} with no search item with only a blank space criteria
	 * and confirming that no response is returned.
	 */
	@Test
	public void testSearchSpaceValue() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef userActor = system.actorOf(UserActor.props(testActor, new TwitterMockServiceImpl(), null, null, null));		
		
		Map<String, String> data = new HashMap<>();
		data.put("symbol", " ");
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.convertValue(data, JsonNode.class);
				
		userActor.tell(jsonNode, probe.getRef());
		probe.expectNoMessage();		
	}
}
