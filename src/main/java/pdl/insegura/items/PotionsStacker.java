package pdl.insegura.items;

import java.lang.reflect.Field;

import static org.bukkit.Bukkit.getLogger;

public class PotionsStacker {
    public void setMaxStackSizeForPotions(int newSize) {
        try {
            // Ajusta la ruta de las clases NMS a la versión de tu servidor
            // Normalmente en Spigot 1.16.5 sería algo como:
            Class<?> itemsClass = Class.forName("net.minecraft.server.v1_16_R3.Items");
            Class<?> itemClass = Class.forName("net.minecraft.server.v1_16_R3.Item");

            // Campos estáticos en Items para cada tipo de poción
            Field potionField = itemsClass.getDeclaredField("POTION");
            Field splashPotionField = itemsClass.getDeclaredField("SPLASH_POTION");
            Field lingeringPotionField = itemsClass.getDeclaredField("LINGERING_POTION");

            potionField.setAccessible(true);
            splashPotionField.setAccessible(true);
            lingeringPotionField.setAccessible(true);

            Object potionItem = potionField.get(null);
            Object splashPotionItem = splashPotionField.get(null);
            Object lingeringPotionItem = lingeringPotionField.get(null);

            // Ahora necesitamos modificar el campo maxStackSize en la clase Item
            Field maxStackField = itemClass.getDeclaredField("maxStackSize");
            maxStackField.setAccessible(true);

            maxStackField.setInt(potionItem, newSize);
            maxStackField.setInt(splashPotionItem, newSize);
            maxStackField.setInt(lingeringPotionItem, newSize);

            getLogger().info("Se ha establecido el tamaño máximo de stack de las pociones a " + newSize);
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().warning("No se pudo modificar el tamaño máximo de stack de las pociones.");
        }
    }
}
