package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.AsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.DefaultAsyncHttpClientConfig;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import org.junit.Test;
import play.test.TestServer;

import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;
import static org.awaitility.Awaitility.*;

/**
 * This class is used to unit test the WebSocket class.
 * 
 * @author Sadaf Najam, Jashan Singh
 *
 */
public class TwitterWebSocketTest {

	/**
	 * Validates the web socket connection by sending a request and awaiting for a response.
	 */
    @Test
    public void testRejectWebSocket() {
        TestServer server = testServer(37117);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:37117/ws";
                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener(message -> {});
                    CompletableFuture<WebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    await().until(completionStage::isDone);
                    assertThat(completionStage)
                            .hasFailedWithThrowableThat()
                            .hasMessageContaining("Invalid Status Code 404");
                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }

    /**
	 * Validates the web socket connection by sending a request and awaiting for a response.
	 */
    @Test
    public void testAcceptWebSocket() {
        TestServer server = testServer(19001);
        running(server, () -> {
            try {
                AsyncHttpClientConfig config = new DefaultAsyncHttpClientConfig.Builder().setMaxRequestRetry(0).build();
                AsyncHttpClient client = new DefaultAsyncHttpClient(config);
                WebSocketClient webSocketClient = new WebSocketClient(client);

                try {
                    String serverURL = "ws://localhost:19001/ws";
                    ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);
                    WebSocketClient.LoggingListener listener = new WebSocketClient.LoggingListener((message) -> {
                        try {
                            queue.put(message);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    CompletableFuture<WebSocket> completionStage = webSocketClient.call(serverURL, listener);
                    await().until(completionStage::isDone);
                } finally {
                    client.close();
                }
            } catch (Exception e) {
                fail("Unexpected exception", e);
            }
        });
    }
}
