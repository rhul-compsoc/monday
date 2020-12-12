package uk.co.computingsociety.monday;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginEvent implements Listener {
  public Monday monday;
  public PluginDescriptionFile desc;

  public LoginEvent(Monday monday) {
    this.monday = monday;
    this.desc = monday.getDescription();
  }

  @EventHandler
  public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
    String strUrl = monday.getConfig().getString("url");
    // Check if the location is null
    if (strUrl == null) {
      event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "A URI was not provided to Monday.");
      return;
    } else if (strUrl.contains("?")) {
      event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "The Monday URI cannot contain query attributes.");
      return;
    }

    try {
      // Try to connect to the server.
      URL url = new URL(strUrl + "?uuid=" + event.getUniqueId());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", String.format("%s (%s)", desc.getName(), desc.getVersion()));

      int code = connection.getResponseCode();

      switch (code) {
        case 200:
          BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

          // Read a line of the response.
          switch (br.readLine()) {
            case "ok":
              // Whitelisted!
              event.allow();
              break;
            case "notfound":
              // The user was not found inside the whitelist.
              event.disallow(Result.KICK_WHITELIST, "You have not been whitelisted!");
              break;
            case "banned":
              // The user is banned.
              event.disallow(Result.KICK_BANNED, "You have been banned from the server.");
              break;
            default:
              // The server sent some crud to us. Kick the user.
              event.disallow(Result.KICK_OTHER, "Invalid response from the Monday server.");
              break;
          }
          break;
        case 401:
          event.disallow(Result.KICK_OTHER, "Invalid authorization header given to the Monday server.");
          break;
        default:
          event.disallow(Result.KICK_OTHER, "Unknown error by Monday server.");
      }
    } catch (IOException e) {
      // Kick when an IO error occurs.
      event.disallow(Result.KICK_OTHER, "A timeout occurred while attempting to contact the Monday server.");
    } catch (Exception e) {
      // An uncaught exception?!?
      // Print the exception, then kick the user with exception details.
      e.printStackTrace();
      event.disallow(Result.KICK_OTHER, String.format("%s %s:\n%s", desc.getName(), e.getClass().getName(), e.getMessage()));
    }
  }
}
