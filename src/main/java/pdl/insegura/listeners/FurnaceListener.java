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

        Furnace furnaceState = (Furnace) event.getBlock().getState();
        FurnaceInventory inv = furnaceState.getInventory();
        ItemStack currentOutput = inv.getResult();

        if (isCookableFood(result.getType())) {
            if (random.nextDouble() < BURN_PROBABILITY) {
                int totalAmount = result.getAmount();
                if (currentOutput != null && currentOutput.getType() != Material.AIR) {
                    totalAmount += currentOutput.getAmount();
                }

                ItemStack burnedFood = new ItemStack(Material.CHARCOAL, totalAmount);
                ItemMeta meta = burnedFood.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName("§cComida Quemada");
                    burnedFood.setItemMeta(meta);
                }
                event.setResult(burnedFood);
                inv.setResult(burnedFood);
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
