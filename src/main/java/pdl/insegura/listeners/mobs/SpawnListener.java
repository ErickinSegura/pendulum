package pdl.insegura.listeners.mobs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.PendulumSettings;
import pdl.insegura.listeners.mobs.customs.InfernalGuardian;

import java.util.List;

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

        if (PendulumSettings.getInstance().getDia() >= 15) {

            if (event.getEntity() instanceof Strider strider) {
                InfernalGuardian.spawnInfernalGuardian(strider);
            }

            if (event.getEntity() instanceof Piglin) {
                event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.PIGLIN_BRUTE);
                event.setCancelled(true);
            }

            if (event.getEntity() instanceof PigZombie pigZombie) {
                pigZombie.setAngry(true);
                pigZombie.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
                pigZombie.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                pigZombie.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                pigZombie.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                pigZombie.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
                pigZombie.getEquipment().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));

                pigZombie.getEquipment().setHelmetDropChance(0.0F);
                pigZombie.getEquipment().setChestplateDropChance(0.0F);
                pigZombie.getEquipment().setLeggingsDropChance(0.0F);
                pigZombie.getEquipment().setBootsDropChance(0.0F);
                pigZombie.getEquipment().setItemInMainHandDropChance(0.0F);
                pigZombie.getEquipment().setItemInOffHandDropChance(0.0F);

                // Hacer que el PigZombie ataque al jugador más cercano
                Player nearestPlayer = null;
                double nearestDistance = Double.MAX_VALUE;
                for (Player player : event.getLocation().getWorld().getPlayers()) {
                    double distance = player.getLocation().distance(event.getLocation());
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestPlayer = player;
                    }
                }

                if (nearestPlayer != null) {
                    pigZombie.setTarget(nearestPlayer);
                }
            }

        }
    }

}
