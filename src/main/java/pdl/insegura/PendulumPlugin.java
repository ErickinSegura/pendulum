package pdl.insegura;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.listeners.PlayerListeners;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.items.ItemsRecipes;


public class PendulumPlugin extends JavaPlugin {

    public static String prefix = "&d&lPendulum&r";
    private String version = getDescription().getVersion();

    public void onEnable(){
        registerEvents();
        registerRecipes();


        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage
                ("&l------[" + prefix + "&l]------\n" +
                 "&l|     &aPlugin enabled!&l     |\n" +
                 "&l|     &aVersion: "+version+"      |\n" +
                 "&l|     &aAuthor: Insegura&l    |\n" +
                 "&l-------------------------------"));
    }

    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage
                ("&l------[" + prefix + "&l]------\n" +
                 "&l|     &cPlugin disabled!&l    |\n" +
                 "&l-------------------------------"));
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    public void registerRecipes(){
        ItemsRecipes recipes = new ItemsRecipes();
        recipes.recipes();
    }





}
