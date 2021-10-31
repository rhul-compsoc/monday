package uk.co.computingsociety.monday;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public enum WhitelistResult {
  MISSING_URI(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  MALFORMED_URI(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  ALLOWED(false, WhitelistAction.ALLOW, 0x00FF00),
  SUBNET_ALLOW_EXCEPTION(false, WhitelistAction.ALLOW, 0x7FB413),
  NOT_WHITELISTED(false, WhitelistAction.KICK_WHITELIST, 0xFF0000),
  BANNED(false, WhitelistAction.KICK_BANNED, 0xFF0000),
  SERVER_ERROR(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  SERVER_UNAUTHORISED(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  SERVER_FORBIDDEN(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  BAD_RESPONSE(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  IO_EXCEPTION(true, WhitelistAction.KICK_OTHER, 0xFF0000),
  UNCAUGHT_EXCEPTION(true, WhitelistAction.KICK_OTHER, 0xFF0000);

  boolean error;
  WhitelistAction action;
  private int colour;

  WhitelistResult(boolean error, WhitelistAction action, int colour) {
    this.error = error;
    this.action = action;
    this.colour = colour;
  }

  public int getColour() {
    return this.colour;
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
