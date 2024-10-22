package pdl.insegura.utils;
import org.bukkit.configuration.file.YamlConfiguration;
import pdl.insegura.PendulumPlugin;

import java.io.File;

public class DeathMessages {

    public final static DeathMessages instance = new DeathMessages();

    private File file;
    private YamlConfiguration config;

    private String message;


    private DeathMessages() {
    }

    public void load(){
        file = new File(PendulumPlugin.getInstance().getDataFolder(), "players.yml");

        if (!file.exists())
            PendulumPlugin.getInstance().saveResource("players.yml", false);

        config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeathMessage(String key){
        return config.getString("players." + key);
    }

    public static DeathMessages getInstance(){
        return instance;
    }


}
