package pdl.insegura.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class FurnaceListener implements Listener {

    private static final double BURN_PROBABILITY = 0.1; // 10%
    private final Random random = new Random();

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        ItemStack result = event.getResult();

        // Asegúrate de importar org.bukkit.block.Furnace (no block.data.type.Furnace)
        Furnace furnaceState = (Furnace) event.getBlock().getState();
        FurnaceInventory inv = furnaceState.getInventory();
        ItemStack currentOutput = inv.getResult();

        // Verificamos si el resultado es comida cocinada
        if (isCookableFood(result.getType())) {
            // Aplicamos la probabilidad de quemar
            if (random.nextDouble() < BURN_PROBABILITY) {
                // Calculamos la cantidad total a quemar: lo que ya había más lo recién cocinado
                int totalAmount = result.getAmount();
                if (currentOutput != null && currentOutput.getType() != Material.AIR) {
                    totalAmount += currentOutput.getAmount();
                }

                // Creamos el ítem quemado con el total
                ItemStack burnedFood = new ItemStack(Material.CHARCOAL, totalAmount);
                ItemMeta meta = burnedFood.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName("§cComida Quemada");
                    burnedFood.setItemMeta(meta);
                }

                // Ajustamos el resultado del evento al ítem quemado
                event.setResult(burnedFood);

                // Forzamos el inventario del horno para que quede con la comida quemada, sobreescribiendo lo que haya
                inv.setResult(burnedFood);

                // Opcional: reproducir sonido de "quemado"
                event.getBlock().getWorld().playSound(event.getBlock().getLocation(), Sound.ITEM_FIRECHARGE_USE, 1.0F, 1.0F);
            }
        }
    }

    private boolean isCookableFood(Material material) {
        return material == Material.COOKED_BEEF ||
                material == Material.COOKED_CHICKEN ||
                material == Material.COOKED_COD ||
                material == Material.COOKED_MUTTON ||
                material == Material.COOKED_PORKCHOP ||
                material == Material.COOKED_RABBIT ||
                material == Material.COOKED_SALMON ||
                material == Material.BAKED_POTATO;
    }
}
