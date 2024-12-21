// ArmorCheckListener.java
package pdl.insegura.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.enums.ArmorSet;

public class ArmorCheckListener implements Listener {

    private final Plugin plugin;
    private final AdvancementsListener advancementsListener;

    public ArmorCheckListener(Plugin plugin, AdvancementsListener advancementsListener) {
        this.plugin = plugin;
        this.advancementsListener = advancementsListener;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        // Si el click es en un slot de ARMOR o hace shift+click (que puede autoequipar),
        // esperamos 1 tick para que el inventario se actualice y luego comprobamos.
        if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick()) {
            Player player = (Player) event.getWhoClicked();
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Recorremos todos los sets definidos en nuestro enum
                    for (ArmorSet armorSet : ArmorSet.values()) {
                        if (isWearingFullSet(player, armorSet)) {
                            // Disparamos el advancement correspondiente
                            advancementsListener.obtainAdvancement(player, armorSet.getAdvancementKey());
                        }
                    }
                }
            }.runTaskLater(plugin, 1L);
        }
    }

    /**
     * Verifica si el jugador viste un set completo del tipo armorSet.
     */
    private boolean isWearingFullSet(Player player, ArmorSet armorSet) {
        ItemStack helmet = player.getInventory().getHelmet();
        ItemStack chest  = player.getInventory().getChestplate();
        ItemStack legs   = player.getInventory().getLeggings();
        ItemStack boots  = player.getInventory().getBoots();

        return isMatchingPiece(helmet, armorSet.getHelmetMaterial(), armorSet.getHelmetName()) &&
                isMatchingPiece(chest,  armorSet.getChestMaterial(),  armorSet.getChestName())   &&
                isMatchingPiece(legs,   armorSet.getLeggingsMaterial(), armorSet.getLeggingsName()) &&
                isMatchingPiece(boots,  armorSet.getBootsMaterial(),  armorSet.getBootsName());
    }


    private boolean isMatchingPiece(ItemStack item,
                                    org.bukkit.Material expectedMaterial,
                                    String expectedName) {
        return item != null
                && item.getType() == expectedMaterial
                && item.hasItemMeta()
                && item.getItemMeta().hasDisplayName()
                && item.getItemMeta().getDisplayName().equals(expectedName);
    }
}
