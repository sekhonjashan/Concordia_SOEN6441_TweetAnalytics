package actors;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actors.TweetSearchActor.TickTweetMessage;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import service.TwitterMockServiceImpl;

/**
 * This class is used to unit test the TweetSearchActor class. 
 * @author Ashwin Soorkeea
 *
 */
public class TweetSearchActorTest {

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
	 * Validates that search returns at least one tweet by calling the {@code RequestTweetMessage} message protocol and using the
	 * {@code TweetSearchActor} actor.
	 */
	@Test
	public void testSearch() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef tweetActor = system.actorOf(TweetSearchActor.props(testActor));		
		TweetSearchActor.RequestTweetMessage requestTweetMessage = new TweetSearchActor.RequestTweetMessage(new TwitterMockServiceImpl(), 
				null, "bank", null, null);
		tweetActor.tell(requestTweetMessage, probe.getRef());
		ObjectNode objectNode = probe.expectMsgClass(ObjectNode.class);
		assertThat(objectNode.size(), CoreMatchers.is(1));
	}
	
	/**
	 * Validates the refresh client functionality by calling the {@code RequestTweetMessage} message protocol followed by the 
	 * {@code TickTweetMessage} and validates that the response returns at least message refreshed.
	 */
	@Test
	public void testRefreshClient() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef tweetActor = system.actorOf(TweetSearchActor.props(testActor));		
		TweetSearchActor.RequestTweetMessage requestTweetMessage = new TweetSearchActor.RequestTweetMessage(new TwitterMockServiceImpl(), 
				null, "bank", null, null);
		tweetActor.tell(requestTweetMessage, probe.getRef());
		tweetActor.tell(new TickTweetMessage(), probe.getRef());
		ObjectNode objectNode = probe.expectMsgClass(ObjectNode.class);
		assertThat(objectNode.size(), CoreMatchers.is(1));
	}
	
	/**
	 * Validates the refresh client functionality by calling {@code TickTweetMessage} if there are no search items. 
	 */
	@Test
	public void testRefreshClientNoMetadata() {
		TestKit probe = new TestKit(system);	
		ActorRef testActor = probe.getTestActor();
		ActorRef tweetActor = system.actorOf(TweetSearchActor.props(testActor));		
		tweetActor.tell(new TickTweetMessage(), probe.getRef());
		probe.expectNoMessage();		
	}
	
}
