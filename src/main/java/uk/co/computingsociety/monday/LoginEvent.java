package uk.co.computingsociety.monday;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import org.bukkit.Bukkit;
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

    WhitelistResult result = checker.checkAndLog(event.getUniqueId().toString(), event.getAddress().getHostAddress(),
                                                 event.getName());

    if (result != WhitelistResult.ALLOWED && result != WhitelistResult.SUBNET_ALLOW_EXCEPTION) {
      event.disallow(result.action.asyncResult, result.getFullMessage(this.config));
    } else {
      event.allow();
    }
  }

  @EventHandler
  public void afterPlayerJoin(PlayerJoinEvent event) {
    FileConfiguration config = this.config;

    // If the kicking mechanism is turned Ion, ignore the event.
    if (kick) {
      return;
    }

    Bukkit.getScheduler().runTaskAsynchronously(monday, () -> {
      WhitelistResult result = checker.checkAndLog(event.getPlayer().getUniqueId().toString(),
                                                   event.getPlayer().getAddress().getAddress().getHostAddress(),
                                                   event.getPlayer().getName());
      event.getPlayer().sendMessage(result.getFullMessage(config));
    });
  }
}
