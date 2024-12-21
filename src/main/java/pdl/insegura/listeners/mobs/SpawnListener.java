package pdl.insegura.listeners.mobs;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pdl.insegura.listeners.mobs.customs.ExplosivePenguinVillager;
import pdl.insegura.utils.PendulumSettings;
import pdl.insegura.listeners.mobs.customs.InfernalGuardian;

import java.util.Random;

public class SpawnListener implements Listener {

    private final Random random = new Random();

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
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2, false, true));
            // Velocidad 5
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, true));
            // Salto 2
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, true));


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

            if (event.getEntity() instanceof Villager &&
                    (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL ||
                            event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CHUNK_GEN)) {
                ExplosivePenguinVillager.trySpawnPenguinVillager(event.getLocation());
                event.setCancelled(true);
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

        if (PendulumSettings.getInstance().getDia() >= 20) {
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4, false, true));
            // Velocidad 5
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, true));
            // Resistencia 2
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, true));
            // Salto
            event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 1, false, true));


            if (event.getEntity() instanceof Skeleton) {
                // 50% de probabilidad de dar flechas de ceguera
                if (random.nextBoolean()) {
                    ItemStack arrowBlindness = new ItemStack(Material.TIPPED_ARROW, 64);
                    PotionMeta arrowMeta = (PotionMeta) arrowBlindness.getItemMeta();

                    // Añadir efecto de ceguera por 10 minutos (600 segundos)
                    arrowMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 600, 0), true);
                    arrowBlindness.setItemMeta(arrowMeta);

                    // Dar al esqueleto el arco con punch y las flechas de ceguera
                    ItemStack bow = new ItemStack(Material.BOW);
                    ItemMeta bowMeta = bow.getItemMeta();
                    bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 10, true);
                    bow.setItemMeta(bowMeta);

                    // Equipar al esqueleto
                    event.getEntity().getEquipment().setItemInMainHand(bow);
                    event.getEntity().getEquipment().setItemInMainHandDropChance(0.0F);
                    event.getEntity().getEquipment().setItemInOffHand(arrowBlindness);
                    event.getEntity().getEquipment().setItemInOffHandDropChance(0.0F);
                }
            }
        }
    }

}
