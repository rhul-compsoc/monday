package uk.co.computingsociety.monday;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class LoginEvent implements Listener {
  private WhitelistChecker checker;
  private boolean kick;

  public LoginEvent(Monday monday) {
    this.checker = new WhitelistChecker(monday);
    this.kick = monday.getConfig().getBoolean("kick");
  }

  @EventHandler
  public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
    // If the kicking mechanism is turned off, ignore the event.
    if (!kick) {
      event.allow();
      return;
    }

    WhitelistResult result = checker.check(event.getUniqueId().toString());

    if (result != WhitelistResult.ALLOWED) {
      event.disallow(result.action.asyncResult, result.message);
    } else {
      event.allow();
    }
  }
}
