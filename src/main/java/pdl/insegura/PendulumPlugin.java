package pdl.insegura;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PendulumPlugin extends JavaPlugin {

    public void onEnable(){
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Test"));

    }

    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage("adio");

    }

}
