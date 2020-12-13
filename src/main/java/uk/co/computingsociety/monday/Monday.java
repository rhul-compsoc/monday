package uk.co.computingsociety.monday;

import org.bukkit.plugin.java.JavaPlugin;

public class Monday extends JavaPlugin {


  @Override
  public void onEnable() {
    getLogger().info("Welcome to Minecraft Mondays!");
    getConfig().options().copyDefaults(true);
    this.saveDefaultConfig();

    getServer().getPluginManager().registerEvents(new LoginEvent(this), this);
  }

  @Override
  public void onDisable() {
    getLogger().info("Disabled Minecraft Mondays.");
  }
}
