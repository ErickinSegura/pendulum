package pdl.insegura.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import pdl.insegura.PendulumPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigUpdater {

    public static void updateChallenge(Reto selectedReto, String selectedCastigo) {
        File file = new File(PendulumPlugin.getInstance().getDataFolder(), "settings.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Actualizar configuración con los valores del objeto Reto
        config.set("reto.desafio", selectedReto.getTitulo());
        config.set("reto.castigo", selectedCastigo);
        config.set("reto.cantidadDesafio", selectedReto.getCantidad());
        config.set("reto.materialDesafio", selectedReto.getMaterial().name());

        // Guardar cambios
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
