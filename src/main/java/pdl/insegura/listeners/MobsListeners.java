package pdl.insegura.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pdl.insegura.items.customItems;


public class MobsListeners implements Listener {


    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if (event.getEntity() instanceof org.bukkit.entity.Skeleton) {
            ItemStack bowPunch = new ItemStack(Material.BOW);
            ItemMeta bowPunchMeta = bowPunch.getItemMeta();
            bowPunchMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 5, true);
            bowPunch.setItemMeta(bowPunchMeta);
            event.getEntity().getEquipment().setItemInMainHand(bowPunch);
            event.getEntity().getEquipment().setItemInMainHandDropChance(0);
        }

    }

    @EventHandler
    public void ZombieHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof org.bukkit.entity.Zombie) {
            Player player = (Player) event.getEntity();
            player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 220, 1));

    }
}






    }

