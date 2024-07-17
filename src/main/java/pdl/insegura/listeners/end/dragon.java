package pdl.insegura.listeners.end;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
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
            // Comrobar que sea en el end
            if (event.getEntity().getLocation().getWorld().getEnvironment() == World.Environment.THE_END) {
                Creeper creeper = (Creeper) event.getEntity().getLocation().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.CREEPER);
                creeper.setFuseTicks(creeper.getFuseTicks()/2);
                creeper.setPowered(true);
            }
        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.DRAGON_EGG, 1));
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        // Verificar si el contenido de la cubeta es agua
        if (event.getBucket() == Material.WATER_BUCKET) {
            World world = event.getPlayer().getWorld();
            // Verificar si el jugador está en el End
            if (world.getEnvironment() == World.Environment.THE_END) {
                // Verificar si hay una dragona del End viva
                boolean dragonAlive = world.getEntitiesByClass(EnderDragon.class).stream()
                        .anyMatch(enderDragon -> !enderDragon.isDead());
                if (dragonAlive) {
                    // Cancelar el evento para evitar que el agua sea colocada
                    event.setCancelled(true);
                }
            }
        }
    }

}
