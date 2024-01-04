package pdl.insegura;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.commands.PendulumCommand;
import pdl.insegura.listeners.PlayerListeners;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.items.ItemsRecipes;


public class PendulumPlugin extends JavaPlugin {

    public static String prefix = "&d&lPendulum&r";
    private String version = getDescription().getVersion();

    public void onEnable(){
        registerEvents();
        registerRecipes();
        registerCommands();


        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l[" + prefix + "&l]"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l&dPlugin enabled!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l&dVersion: &r" + version));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l[" + prefix + "&l]"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("       &l&dPlugin disabled!"));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    public void registerRecipes(){
        ItemsRecipes recipes = new ItemsRecipes();
        recipes.recipes();
    }

    public void registerCommands(){
        getServer().getPluginCommand("pendulum").setExecutor(new PendulumCommand());
    }





}
