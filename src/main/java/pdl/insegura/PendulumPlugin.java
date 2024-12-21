package pdl.insegura;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.commands.CommandCompletion;
import pdl.insegura.commands.PendulumCommand;
import pdl.insegura.items.PotionsStacker;
import pdl.insegura.listeners.*;
import pdl.insegura.listeners.end.EndManager;
import pdl.insegura.listeners.mobs.MobListener;
import pdl.insegura.listeners.mobs.SpawnListener;
import pdl.insegura.listeners.mobs.customs.InfernalGuardian;
import pdl.insegura.listeners.mobs.customs.VoidedKnight;
import pdl.insegura.utils.DeathMessages;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.items.ItemsRecipes;
import pdl.insegura.utils.PendulumSettings;
import pdl.insegura.structures.StructureGenerator;

import java.io.File;
import java.util.Arrays;
import java.util.List;

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
        registerAdvancements();
        setMaxStackSizeForPotions(16);

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

    private void registerAdvancements() {

        List<String> advancementsPaths = Arrays.asList(
                "pendulum",
                "items/oro",
                "kills/kill",
                "kills/rostam",
                "kills/norman",
                "kills/admin",
                "items/dirty", /*Dia 5*/
                "items/hearthy",
                "items/dirtiest",
                "armors/agile",
                "armors/reinforced",
                "armors/voided", /*Dia 10*/
                "voided/voided_scrap",
                "voided/voided_apple",
                "voided/voided_knight",
                "armors/guardian", /*Dia 15*/
                "armors/assault",
                "randoms/chick",
                "kills/carrot"
        );

        Bukkit.getScheduler().runTaskLater(this, () -> {
            for (String path : advancementsPaths) {
                NamespacedKey key = new NamespacedKey(this, path);
                Advancement advancement = Bukkit.getAdvancement(key);

                if (advancement == null) {
                    getLogger().warning("No se pudo encontrar el advancement: " + key);
                } else {
                    getLogger().info("Advancement cargado correctamente: " + key);

                }
            }
        }, 20L);
    }

    private void registerEvents() {
        AdvancementsListener advancementsListener = new AdvancementsListener(this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
        getServer().getPluginManager().registerEvents(new ItemsRecipes(this), this);
        getServer().getPluginManager().registerEvents(new StructureGenerator(this), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        getServer().getPluginManager().registerEvents(new FurnaceListener(), this);
        getServer().getPluginManager().registerEvents(new AdvancementsListener(this), this);
        getServer().getPluginManager().registerEvents(new ArmorCheckListener(this, advancementsListener), this);
        getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
    }

    private void registerRecipes() {
        ItemsRecipes recipes = new ItemsRecipes(this);
        recipes.recipes();
    }

    private void setMaxStackSizeForPotions(int newSize) {
        PotionsStacker stacker = new PotionsStacker();
        stacker.setMaxStackSizeForPotions(newSize);
    }

    public void registerEventsMobs(){
        getServer().getPluginManager().registerEvents(new MobListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(), this);
        getServer().getPluginManager().registerEvents(new EndManager(this), this);
        getServer().getPluginManager().registerEvents(new VoidedKnight(this), this);
        getServer().getPluginManager().registerEvents(new InfernalGuardian(this), this);
    }

    private void registerCommands() {
        getServer().getPluginCommand("pendulum").setExecutor(new PendulumCommand());
        getServer().getPluginCommand("pendulum").setTabCompleter(new CommandCompletion());
    }

    public static PendulumPlugin getInstance(){
        return JavaPlugin.getPlugin(PendulumPlugin.class);
    }
}
