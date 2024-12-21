package pdl.insegura.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;

public class ItemPickupListener implements Listener {

    PendulumPlugin plugin = PendulumPlugin.getInstance();
    AdvancementsListener advancementsListener = new AdvancementsListener(plugin);

    private final String shardName = MessageUtils.colorMessage("&d&lVoided Shard");;
    private final String oroName = MessageUtils.colorMessage("&6&lOro Doble");;

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        // Verificamos si el que recoge el ítem es un jugador
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        ItemStack item = event.getItem().getItemStack();

        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && shardName.equals(meta.getDisplayName())) {
                if (item.getType() == Material.NETHERITE_SCRAP) {
                    advancementsListener.obtainAdvancement(player, "voided/voided_scrap");
                }
            }

            if (meta.hasDisplayName() && oroName.equals(meta.getDisplayName())) {
                if (item.getType() == Material.POPPED_CHORUS_FRUIT) {
                    advancementsListener.obtainAdvancement(player, "items/oro");
                }
            }


        }
    }


}
