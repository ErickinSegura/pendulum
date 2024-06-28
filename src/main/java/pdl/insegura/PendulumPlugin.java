package pdl.insegura;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.commands.PendulumCommand;
import pdl.insegura.listeners.PlayerListeners;
import pdl.insegura.utils.DeathMessages;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.items.ItemsRecipes;
import pdl.insegura.utils.PendulumSettings;

public class PendulumPlugin extends JavaPlugin {

    public static String prefix = "&d&lPendulum&r";
    private String version = getDescription().getVersion();

    @Override
    public void onEnable() {
        PendulumSettings.getInstance().load();
        DeathMessages.getInstance().load();
        registerEvents();
        registerRecipes();
        registerCommands();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l[" + prefix + "&l]"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l&dPlugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l&dVersion: &r" + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l[" + prefix + "&l]"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l&dPlugin disabled!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new ItemsRecipes(this), this);
    }

    private void registerRecipes() {
        ItemsRecipes recipes = new ItemsRecipes(this);
        recipes.recipes();
    }

    private void registerCommands() {
        getServer().getPluginCommand("pendulum").setExecutor(new PendulumCommand());
    }

    public static PendulumPlugin getInstance(){
        return JavaPlugin.getPlugin(PendulumPlugin.class);
    }
}
