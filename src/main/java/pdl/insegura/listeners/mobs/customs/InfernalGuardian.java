package pdl.insegura.listeners.mobs.customs;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class InfernalGuardian implements Listener {

    public static void spawnInfernalGuardian(Strider strider) {
        strider.setGlowing(true);
        ElderGuardian infernalGuardian = strider.getWorld().spawn(strider.getLocation(), ElderGuardian.class);
        setupInfernalGuardian(infernalGuardian);
        strider.addPassenger(infernalGuardian); // Hacer que el Strider lleve de pasajero al Elder Guardian
    }

    private static void setupInfernalGuardian(ElderGuardian entity) {
        // Hacer que pueda nadar en lava sin quemarse
        entity.setFireTicks(Integer.MAX_VALUE);
        // Aumentar la velocidad
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5);

        // Aumentar la vida
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(100.0);
        entity.setHealth(100.0);

        // Hacer que el rayo haga más daño
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));

        // Añadir efecto de resistencia al fuego
        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));

        entity.setCustomName("Infernal Guardian");
    }

    @EventHandler
    public static void onInfernalGuardianAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof ElderGuardian && event.getEntity() instanceof LivingEntity) {
            ElderGuardian guardian = (ElderGuardian) event.getDamager();
            LivingEntity target = (LivingEntity) event.getEntity();

            // Verificar si este ElderGuardian es un InfernalGuardian
            if (guardian.getFireTicks() > 0) {
                // Aumentar el daño
                event.setDamage(event.getDamage() * 1.5);

                // Prender fuego al objetivo
                target.setFireTicks(100); // 5 segundos de fuego
            }
        }
    }

    @EventHandler
    public static void onGuardianNearby(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof ElderGuardian) {
            ElderGuardian guardian = (ElderGuardian) event.getEntity();

            // Verificar si este ElderGuardian es un InfernalGuardian
            if (guardian.getFireTicks() > 0) {
                List<Entity> nearbyEntities = guardian.getNearbyEntities(100, 100, 100); // 10 bloques de radio
                for (Entity nearby : nearbyEntities) {
                    if (nearby instanceof LivingEntity && nearby != guardian) {
                        LivingEntity target = (LivingEntity) nearby;
                        target.damage(5.0); // Daño adicional
                        target.setFireTicks(100); // 5 segundos de fuego
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof ElderGuardian) {
            ElderGuardian guardian = (ElderGuardian) event.getEntity();
            if (guardian.getFireTicks() > 0) {
                event.getDrops().clear();
                guardian.getLocation().getWorld().dropItemNaturally(guardian.getLocation(), new ItemStack(Material.NETHERITE_INGOT));
                guardian.getLocation().getWorld().playSound(guardian.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                guardian.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, guardian.getLocation(), 1);
            }
        }
    }
}
