package pdl.insegura.listeners.end;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class dragon implements Listener {

    @EventHandler
    public void onEntityGetHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            if (event.getDamager() instanceof Arrow) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof Snowball) {
                event.setCancelled(true);
            }
        }
        if (event.getEntity() instanceof EnderCrystal) {
            if (event.getDamager() instanceof Arrow) {
                event.setCancelled(true);
            }
            if (event.getDamager() instanceof Snowball) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEndermanSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Enderman) {
            event.getEntity().getLocation().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.CREEPER);

        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.DRAGON_EGG, 1));
        }
    }

}
