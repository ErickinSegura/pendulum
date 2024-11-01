package pdl.insegura.utils;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import pdl.insegura.PendulumPlugin;

import java.io.File;
import java.util.List;
import java.util.Map;

public class PendulumSettings {
    private final static PendulumSettings instance = new PendulumSettings();

    private File file;
    private YamlConfiguration config;
    private String[] castigosDia0, op;
    private String desafio, premio, castigo;
    private int cantidadDesafio, cantidadPremio, dia, jugadoresNoche;
    private Material materialDesafio;
    private ItemStack stackPremio;
    private String[] castigos;
    private Reto[] retos;


    private PendulumSettings() {

    }

    public void load() {
        file = new File(PendulumPlugin.getInstance().getDataFolder(), "settings.yml");

        if (!file.exists()) {
            System.out.println("[Pendulum Debug] settings.yml no existe, creando uno nuevo...");
            PendulumPlugin.getInstance().saveResource("settings.yml", false);
        }

        config = new YamlConfiguration();

        try {
            config.load(file);
            System.out.println("[Pendulum Debug] Archivo cargado correctamente");
        } catch (Exception e) {
            System.out.println("[Pendulum Debug] Error al cargar el archivo:");
            e.printStackTrace();
        }

        // Debug de secciones principales
        System.out.println("[Pendulum Debug] Secciones en el archivo:");
        for (String key : config.getKeys(false)) {
            System.out.println("- " + key);
        }

        // Debug de la sección reto
        System.out.println("[Pendulum Debug] Contenido de la sección reto:");
        if (config.contains("reto")) {
            for (String key : config.getConfigurationSection("reto").getKeys(false)) {
                System.out.println("- " + key + ": " + config.get("reto." + key));
            }
        } else {
            System.out.println("No se encontró la sección 'reto'");
        }

        // Cargar retos con debug
        List<Map<?, ?>> retosConfig = config.getMapList("reto.retos");
        System.out.println("[Pendulum Debug] Cantidad de retos encontrados: " + retosConfig.size());
        retos = new Reto[retosConfig.size()];
        for (int i = 0; i < retosConfig.size(); i++) {
            Map<?, ?> retoMap = retosConfig.get(i);
            String titulo = (String) retoMap.get("titulo");
            int cantidad = (int) retoMap.get("cantidad");
            Material material = Material.valueOf((String) retoMap.get("material"));
            retos[i] = new Reto(titulo, cantidad, material);
        }

        // Cargar castigos con debug
        castigos = config.getStringList("reto.castigos").toArray(new String[0]);
        System.out.println("[Pendulum Debug] Cantidad de castigos encontrados: " + castigos.length);


        castigosDia0 = config.getStringList("reto.castigos.dia0").toArray(new String[0]);


        // Cargar resto de configuraciones
        desafio = config.getString("reto.desafio");
        premio = config.getString("reto.premio");
        castigo = config.getString("reto.castigo");
        cantidadDesafio = config.getInt("reto.cantidadDesafio");
        cantidadPremio = config.getInt("reto.cantidadPremio");
        dia = config.getInt("mundo.dia");

        String materialDesafioString = config.getString("reto.materialDesafio");
        if (materialDesafioString != null) {
            try {
                materialDesafio = Material.valueOf(materialDesafioString);
            } catch (IllegalArgumentException e) {
                System.out.println("[Pendulum Debug] Error con materialDesafio: " + materialDesafioString);
                e.printStackTrace();
                materialDesafio = Material.AIR;
            }
        } else {
            materialDesafio = Material.AIR;
        }

        String materialPremioString = config.getString("reto.materialPremio");
        if (materialPremioString != null) {
            try {
                stackPremio = new ItemStack(Material.valueOf(materialPremioString), cantidadPremio);
            } catch (IllegalArgumentException e) {
                System.out.println("[Pendulum Debug] Error con materialPremio: " + materialPremioString);
                e.printStackTrace();
                stackPremio = new ItemStack(Material.AIR, cantidadPremio);
            }
        } else {
            stackPremio = new ItemStack(Material.AIR, cantidadPremio);
        }

        op = config.getStringList("permisos").toArray(new String[0]);
        jugadoresNoche = config.getInt("mundo.jugadoresNoche");

        // Debug final
        System.out.println("[Pendulum Debug] Carga completada:");
        System.out.println("- Retos cargados: " + (retos != null ? retos.length : 0));
        System.out.println("- Castigos cargados: " + (castigos != null ? castigos.length : 0));
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

    public Reto[] getRetos() {
        return retos;
    }

    public String[] getCastigos() {
        return castigos;
    }

    public String[] getCastigosDia0(){
        return castigosDia0;
    }

    public String[] getOp() {
        return op;
    }

    public int getDia(){
        return dia;
    }

    public int getJugadoresNoche(){
        return jugadoresNoche;
    }

    public static PendulumSettings getInstance() {
        return instance;
    }

}

