package uk.co.computingsociety.monday;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WhitelistWebSocket extends WebSocketClient {
  public WhitelistWebSocket(URI uri, Map<String, String> headers) {
    super(uri, headers);
  }

  @Override
  public void onOpen(ServerHandshake handshake) {
    send("hello");
  }

  @Override
  public void onMessage(String message) {
    // TODO: stub
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    // TODO: stub
  }

  @Override
  public void onError(Exception e) {
    e.printStackTrace();
  }

  /**
   * Create a new WebSocket client for listening to whitelist events from the Computing Society server.
   * @param uri The remote that will be sending the whitelist events
   * @param token The token required to authorise the client
   * @return The WebSocket listener.
   * @throws URISyntaxException If the given string isn't a URI.
   */
  public static WhitelistWebSocket createClient(String uri, String token) throws URISyntaxException {
    Map<String, String> map = new HashMap<String, String>();
    map.put("Authorization", String.format("Bearer %s", token));
    return new WhitelistWebSocket(new URI(uri), map);
  }
}
