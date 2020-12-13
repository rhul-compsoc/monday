package uk.co.computingsociety.monday;

public enum WhitelistResult {
  MISSING_URI(true, WhitelistAction.KICK_OTHER, "{monday} could not find authentication server URL"),
  MALFORMED_URI(true, WhitelistAction.KICK_OTHER, "{monday} cannot fetch from malformed server URL"),
  ALLOWED(false, WhitelistAction.ALLOW, ""),
  NOT_WHITELISTED(false, WhitelistAction.KICK_WHITELIST, "You have not yet signed up for {monday}"),
  BANNED(false, WhitelistAction.KICK_BANNED, "You have been banned on this server"),
  SERVER_ERROR(true, WhitelistAction.KICK_OTHER, "The {monday} server encountered an error"),
  SERVER_UNAUTHORISED(true, WhitelistAction.KICK_OTHER, "The {monday} client gave invalid credentials to the {monday} server"),
  BAD_RESPONSE(true, WhitelistAction.KICK_OTHER, "The {monday} server sent a bad response"),
  IO_EXCEPTION(true, WhitelistAction.KICK_OTHER, "Encountered IOException while communicating with the {monday} server"),
  UNCAUGHT_EXCEPTION(true, WhitelistAction.KICK_OTHER, "An uncaught exception was caught by the {monday} client");

  boolean error;
  WhitelistAction action;
  String message;

  WhitelistResult(boolean error, WhitelistAction action, String message) {
    this.error = error;
    this.message = message.replaceAll("\\{monday}", "MondayWhitelist");
    this.action = action;
  }
}
