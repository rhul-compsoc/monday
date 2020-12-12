package uk.co.computingsociety.monday;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

public class LoginEvent implements Listener {
  

  public LoginEvent(Monday plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent event) {
    event.disallow(Result.KICK_WHITELIST, "no");
    
  }
}
