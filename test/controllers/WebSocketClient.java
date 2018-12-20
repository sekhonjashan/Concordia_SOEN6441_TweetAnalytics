package controllers;

import play.shaded.ahc.org.asynchttpclient.AsyncHttpClient;
import play.shaded.ahc.org.asynchttpclient.BoundRequestBuilder;
import play.shaded.ahc.org.asynchttpclient.ListenableFuture;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocket;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketListener;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketTextListener;
import play.shaded.ahc.org.asynchttpclient.ws.WebSocketUpgradeHandler;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * This class is used to unit test the WebSocket class.
 * 
 * @author Sadaf Najam, Jashan Singh
 *
 */
public class WebSocketClient {

    private AsyncHttpClient client;

    public WebSocketClient(AsyncHttpClient c) {
        this.client = c;
    }
    /**
     * B
     * @param url
     * @param listener
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public CompletableFuture<WebSocket> call(String url, WebSocketTextListener listener) throws ExecutionException, InterruptedException {
        final BoundRequestBuilder requestBuilder = client.prepareGet(url);

        final WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().addWebSocketListener(listener).build();
        final ListenableFuture<WebSocket> future = requestBuilder.execute(handler);
        return future.toCompletableFuture();
    }

    /**
     * This inner class is used to handle web socket connection events once a connection is established to the server. 
     */
    static class LoggingListener implements WebSocketTextListener {
        private final Consumer<String> onMessageCallback;

        public LoggingListener(Consumer<String> onMessageCallback) {
            this.onMessageCallback = onMessageCallback;
        }

        private Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingListener.class);

        private Throwable throwableFound = null;

        public Throwable getThrowable() {
            return throwableFound;
        }

        /**
         * Gets called on opening the web socket connection.
         */
        public void onOpen(WebSocket websocket) {
        }

        /**
         * Gets called before closing the connection.
         */
        public void onClose(WebSocket websocket) {
        }

        /**
         * Handle the exception
         */
        public void onError(Throwable t) {
            throwableFound = t;
        }
        
        /**
         * Sends a message to the callback
         */
        @Override
        public void onMessage(String s) {
            onMessageCallback.accept(s);
        }
    }

}
