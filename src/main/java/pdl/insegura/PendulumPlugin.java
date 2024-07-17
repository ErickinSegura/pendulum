package pdl.insegura;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.commands.CommandCompletion;
import pdl.insegura.commands.PendulumCommand;
import pdl.insegura.listeners.PlayerListeners;
import pdl.insegura.listeners.end.EndManager;
import pdl.insegura.listeners.mobs.MobListener;
import pdl.insegura.listeners.mobs.SpawnListener;
import pdl.insegura.listeners.mobs.customs.VoidedKnight;
import pdl.insegura.utils.DeathMessages;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.items.ItemsRecipes;
import pdl.insegura.utils.PendulumSettings;
import pdl.insegura.structures.StructureGenerator;

public class PendulumPlugin extends JavaPlugin {

    public static String prefix = "&d&lPendulum&r";
    private final String version = getDescription().getVersion();

    public static Object getPlugin() {
        return null;
    }

    @Override
    public void onEnable() {
        PendulumSettings.getInstance().load();
        DeathMessages.getInstance().load();
        registerEvents();
        registerRecipes();
        registerCommands();
        registerEventsMobs();


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
        getServer().getPluginManager().registerEvents(new StructureGenerator(this), this);
        getServer().getPluginManager().registerEvents(new VoidedKnight(this), this);

    }

    private void registerRecipes() {
        ItemsRecipes recipes = new ItemsRecipes(this);
        recipes.recipes();

    }

    public void registerEventsMobs(){
        getServer().getPluginManager().registerEvents(new MobListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new EndManager(), this);
    }

    private void registerCommands() {
        getServer().getPluginCommand("pendulum").setExecutor(new PendulumCommand());
        getServer().getPluginCommand("pendulum").setTabCompleter(new CommandCompletion());
    }

    public static PendulumPlugin getInstance(){
        return JavaPlugin.getPlugin(PendulumPlugin.class);
    }
}
