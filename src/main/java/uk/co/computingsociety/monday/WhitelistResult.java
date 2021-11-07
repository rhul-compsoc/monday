package uk.co.computingsociety.monday;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum WhitelistResult {
  MISSING_URI(true, WhitelistAction.KICK_OTHER),
  MALFORMED_URI(true, WhitelistAction.KICK_OTHER),
  ALLOWED(false, WhitelistAction.ALLOW),
  SUBNET_ALLOW_EXCEPTION(false, WhitelistAction.ALLOW),
  NOT_WHITELISTED(false, WhitelistAction.KICK_WHITELIST),
  BANNED(false, WhitelistAction.KICK_BANNED),
  SERVER_ERROR(true, WhitelistAction.KICK_OTHER),
  SERVER_UNAUTHORISED(true, WhitelistAction.KICK_OTHER),
  SERVER_FORBIDDEN(true, WhitelistAction.KICK_OTHER),
  BAD_RESPONSE(true, WhitelistAction.KICK_OTHER),
  IO_EXCEPTION(true, WhitelistAction.KICK_OTHER),
  UNCAUGHT_EXCEPTION(true, WhitelistAction.KICK_OTHER);

  boolean error;
  WhitelistAction action;

  WhitelistResult(boolean error, WhitelistAction action) {
    this.error = error;
    this.action = action;
  }

  public String getKey(FileConfiguration options) {
    boolean kick = options.getBoolean("kick");
    return String.format("message.%s.%s", kick ? "kick" : "join", this.name());
  }


  public String getMessage(FileConfiguration options) {
    return options.getString(getKey(options));
  }

  public String getFullMessage(FileConfiguration options) {
    String header = options.getString("message.header");
    String text = this.getMessage(options);
    String footer = options.getString("message.footer");
    return ChatColor.translateAlternateColorCodes('&', header + text + footer);
  }
}
