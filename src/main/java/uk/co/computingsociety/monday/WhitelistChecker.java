package uk.co.computingsociety.monday;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import com.google.gson.Gson;
import org.bukkit.configuration.file.FileConfiguration;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.logging.Level;

public class WhitelistChecker {
  private Monday monday;
  private String api;
  private String name;
  private String ver;
  private String token;
  private String allowedSubnet;
  private FileConfiguration config;
  private WebhookClient webhookClient;
  private IpAddressMatcher matcher;

  WhitelistChecker(Monday monday) {
    this.monday = monday;
    this.api = monday.getConfig().getString("url");
    this.token = monday.getConfig().getString("token");
    this.allowedSubnet = monday.getConfig().getString("allow_subnet");
    this.name = monday.getDescription().getName();
    this.ver = monday.getDescription().getVersion();
    this.config = monday.getConfig();
    this.matcher = new IpAddressMatcher(this.allowedSubnet);

    String webhook = this.config.getString("webhook");

    if (webhook != null) {
      this.webhookClient = WebhookClient.withUrl(webhook);
    }
  }

  public WhitelistResult checkAndLog(String uuid, InetAddress address) {
    return checkAndLog(uuid, address, null);
  }
  public WhitelistResult checkAndLog(String uuid) {
    return checkAndLog(uuid, null, null);
  }
  public WhitelistResult checkAndLog(String uuid, InetAddress address, String username) {
    WhitelistResult result = this.check(uuid, address);

    if (this.webhookClient != null) {
      WebhookEmbedBuilder embed = new WebhookEmbedBuilder()
        .setColor(result.error ? 0xFF0000 : 0x00FF00)
        .setDescription(result.name())
        .addField(new WebhookEmbed.EmbedField(false, "User Message", result.getMessage(this.config)));

      if (result == WhitelistResult.SUBNET_ALLOW_EXCEPTION) {
        embed.addField(new WebhookEmbed.EmbedField(false, "Subnet Exception", "This user was allowed because the connecting user matches the internal subnet mask of " + this.allowedSubnet + "."));
      }

      if (address != null) embed.addField(new WebhookEmbed.EmbedField(false, "Address", address.getHostAddress()));
      if (username != null) embed.addField(new WebhookEmbed.EmbedField(false, "Username", username));

      webhookClient.send(embed.build());
    }

    return result;
  }

  public WhitelistResult check(String uuid) { return check(uuid, null); }
  public WhitelistResult check(String uuid, InetAddress address) {
    if (address != null) {
      if (this.matcher.matches(address.getHostAddress())) {
        return WhitelistResult.SUBNET_ALLOW_EXCEPTION;
      };
    }

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
      monday.getLogger().log(Level.WARNING, e.toString());
      e.printStackTrace();
      return WhitelistResult.IO_EXCEPTION;
    } catch (Exception e) {
      monday.getLogger().log(Level.SEVERE, e.toString());
      e.printStackTrace();
      return WhitelistResult.UNCAUGHT_EXCEPTION;
    }
  }
}
