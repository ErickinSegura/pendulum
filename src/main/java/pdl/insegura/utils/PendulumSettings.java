package pdl.insegura.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pdl.insegura.PendulumPlugin;

import java.io.File;

public class PendulumSettings {
    private final static PendulumSettings instance = new PendulumSettings();

    private File file;
    private YamlConfiguration config;

    private String[] retos;
    private String desafio, premio, castigo;
    private int cantidadDesafio, cantidadPremio;
    private Material materialDesafio;
    private ItemStack stackPremio;

    private PendulumSettings() {

    }

    public void load(){
        file = new File(PendulumPlugin.getInstance().getDataFolder(), "settings.yml");

        if (!file.exists())
            PendulumPlugin.getInstance().saveResource("settings.yml", false);

        config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        desafio = config.getString("reto.desafio");
        premio = config.getString("reto.premio");
        castigo = config.getString("reto.castigo");
        cantidadDesafio = config.getInt("reto.cantidadDesafio");
        cantidadPremio = config.getInt("reto.cantidadPremio");

        String materialDesafioString = config.getString("reto.materialDesafio");
        if (materialDesafioString != null) {
            try {
                materialDesafio = Material.valueOf(materialDesafioString);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                materialDesafio = Material.AIR; // Valor por defecto si el material no es válido
            }
        } else {
            materialDesafio = Material.AIR; // Valor por defecto si el material es nulo
        }

        String materialPremioString = config.getString("reto.materialPremio");
        if (materialPremioString != null) {
            try {
                stackPremio = new ItemStack(Material.valueOf(materialPremioString), cantidadPremio);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                stackPremio = new ItemStack(Material.AIR, cantidadPremio); // Valor por defecto si el material no es válido
            }
        } else {
            stackPremio = new ItemStack(Material.AIR, cantidadPremio); // Valor por defecto si el material es nulo
        }

        retos = config.getStringList("reto.retos").toArray(new String[0]);

    }

    public String getDesafio(){
        return desafio;
    }

    public String getPremio(){
        return premio;
    }

    public String getCastigo(){
        return castigo;
    }

    public int getCantidadDesafio(){
        return cantidadDesafio;
    }

    public Material getMaterialDesafio(){
        return materialDesafio;
    }

    public ItemStack getStackPremio(){
        return stackPremio;
    }

    public String[] getRetos(){
        return retos;
    }

    public static PendulumSettings getInstance() {
        return instance;
    }
}
