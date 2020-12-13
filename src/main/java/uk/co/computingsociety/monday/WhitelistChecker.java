package uk.co.computingsociety.monday;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

public class WhitelistChecker {
  private Monday monday;
  private String api;
  private String name;
  private String ver;
  private String token;

  WhitelistChecker(Monday monday) {
    this.monday = monday;
    this.api = monday.getConfig().getString("url");
    this.token = monday.getConfig().getString("token");
    this.name = monday.getDescription().getName();
    this.ver = monday.getDescription().getVersion();
  }


  public WhitelistResult check(String uuid) {
    // Check if the location is null
    if (this.api == null) {
      return WhitelistResult.MISSING_URI;
    }

    try {
      Gson gson = new Gson();

      // Try to connect to the server.
      URL url = new URL(this.api + uuid);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("User-Agent", String.format("%s (%s)", this.name, this.ver));
      if (this.token != null) connection.setRequestProperty("X-Auth-Token", this.token);

      int code = connection.getResponseCode();

      switch (code) {
        case 200:
          BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
          WhitelistResponse[] data = gson.fromJson(br.readLine(), WhitelistResponse[].class);

          if (data.length == 0) {
            return WhitelistResult.NOT_WHITELISTED;
          }

          for (WhitelistResponse userConnection : data) {
            if (userConnection.isUserBanned()) return WhitelistResult.BANNED;
          }

          return WhitelistResult.ALLOWED;
        case 401:
          return WhitelistResult.SERVER_UNAUTHORISED;
        case 403:
          return WhitelistResult.SERVER_FORBIDDEN;
        default:
          return WhitelistResult.SERVER_ERROR;
      }
    } catch (IOException e) {
      return WhitelistResult.IO_EXCEPTION;
    } catch (Exception e) {
      e.printStackTrace();
      monday.getLogger().log(Level.SEVERE, e.toString());
      return WhitelistResult.UNCAUGHT_EXCEPTION;
    }
  }
}
