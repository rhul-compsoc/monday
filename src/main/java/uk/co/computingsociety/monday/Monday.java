package uk.co.computingsociety.monday;

import org.bukkit.plugin.java.JavaPlugin;

public class Monday extends JavaPlugin {
  

  @Override
  public void onEnable() {
    getLogger().info("Welcome to Minecraft Mondays!");
    getConfig().addDefault("url", "https://");
    getConfig().options().copyDefaults(true);

    // Register the login event
    new LoginEvent(this);
  }

  @Override
  public void onDisable() {
    getLogger().info("Disabled Minecraft Mondays.");
  }

  
}
