package uk.co.computingsociety.monday;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEvent implements Listener {
  private WhitelistChecker checker;
  private boolean kick;
  private FileConfiguration config;
  private Monday monday;

  public LoginEvent(Monday monday) {
    this.monday = monday;
    this.checker = new WhitelistChecker(monday);
    this.config = monday.getConfig();
    this.kick = this.config.getBoolean("kick");
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
      event.disallow(result.action.asyncResult, result.getMessage(this.config));
    } else {
      event.allow();
    }
  }

  @EventHandler
  public void afterPlayerJoin(PlayerJoinEvent event) {
    FileConfiguration config = this.config;

    Bukkit.getScheduler().runTaskAsynchronously(monday, () -> {
      WhitelistResult result = checker.check(event.getPlayer().getUniqueId().toString());
      event.getPlayer().sendMessage(result.getMessage(config));
    });
  }
}
