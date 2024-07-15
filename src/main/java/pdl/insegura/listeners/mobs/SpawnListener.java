package pdl.insegura.listeners.mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.PendulumSettings;

public class SpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {

        if (PendulumSettings.getInstance().getDia() >= 5) {
            if (event.getEntity() instanceof Skeleton) {
                ItemStack bowPunch = new ItemStack(Material.BOW);
                ItemMeta bowPunchMeta = bowPunch.getItemMeta();
                bowPunchMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 5, true);
                bowPunch.setItemMeta(bowPunchMeta);
                event.getEntity().getEquipment().setItemInMainHand(bowPunch);
                event.getEntity().getEquipment().setItemInMainHandDropChance(0.0F);
            }
        }

        if (PendulumSettings.getInstance().getDia() >= 10) {
            if (event.getEntity() instanceof Skeleton) {
                ItemStack bowPunch = new ItemStack(Material.BOW);
                ItemMeta bowPunchMeta = bowPunch.getItemMeta();
                bowPunchMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 10, true);
                bowPunch.setItemMeta(bowPunchMeta);
                event.getEntity().getEquipment().setItemInMainHand(bowPunch);
                event.getEntity().getEquipment().setItemInMainHandDropChance(0.0F);
            }

            if (event.getEntity() instanceof Creeper) {
                event.getEntity().setSilent(true);

            }
        }
    }

}








