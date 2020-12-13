package uk.co.computingsociety.monday;

import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public enum WhitelistAction {
  ALLOW(AsyncPlayerPreLoginEvent.Result.ALLOWED),
  KICK_WHITELIST(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST),
  KICK_OTHER(AsyncPlayerPreLoginEvent.Result.KICK_OTHER),
  KICK_BANNED(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);

  AsyncPlayerPreLoginEvent.Result asyncResult;

  WhitelistAction(AsyncPlayerPreLoginEvent.Result asyncResult) {
    this.asyncResult = asyncResult;
  }
}
