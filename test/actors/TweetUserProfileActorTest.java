package actors;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import service.TwitterMockServiceImpl;

/**
 * This class is used to unit test the TweetUserProfileActor class.
 * 
 * @author Ashwin Soorkeea
 *
 */
public class TweetUserProfileActorTest {
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
	 * Validates the get user profile information by calling the {@code TweetUserProfileActor} by using the 
	 * {@code RequestUserProfileMessage} and confirming that we have at least 1 response.
	 */
	@Test
	public void testGetUserProfile() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef tweetUserProfileActor = system.actorOf(TweetUserProfileActor.props(testActor));		
		TweetUserProfileActor.RequestUserProfileMessage req = new TweetUserProfileActor.RequestUserProfileMessage(new TwitterMockServiceImpl(), null, "bank", null, null);
		tweetUserProfileActor.tell(req, probe.getRef());
		ObjectNode objectNode = probe.expectMsgClass(ObjectNode.class);
		assertThat(objectNode.size(), CoreMatchers.is(1));
	}
}
