package uk.co.computingsociety.monday;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

public class Monday extends JavaPlugin {
  

  @Override
  public void onEnable() {
    getLogger().info("Welcome to Minecraft Mondays!");
    getConfig().addDefault("url", "https://example.com/");
    getConfig().options().copyDefaults(true);
    this.saveDefaultConfig();

    getServer().getPluginManager().registerEvents(new LoginEvent(this), this);
  }

  @Override
  public void onDisable() {
    getLogger().info("Disabled Minecraft Mondays.");
  }
}
