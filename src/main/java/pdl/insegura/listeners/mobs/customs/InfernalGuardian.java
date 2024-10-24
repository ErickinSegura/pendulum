package pdl.insegura.listeners.mobs.customs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Random;

public class InfernalGuardian implements Listener {
    private static Plugin plugin;
    private static final Random random = new Random();

    public InfernalGuardian(Plugin plugin) {
        InfernalGuardian.plugin = plugin;
    }

    public static void spawnInfernalGuardian(Strider strider) {
        ElderGuardian infernalGuardian = strider.getWorld().spawn(strider.getLocation(), ElderGuardian.class);
        setupInfernalGuardian(infernalGuardian);

        // Configurar el Strider
        setupStrider(strider);

        // Hacer que el guardián monte al Strider
        strider.addPassenger(infernalGuardian);

        // Efectos de spawn
        Location spawnLoc = strider.getLocation();
        World world = spawnLoc.getWorld();
        world.spawnParticle(Particle.LAVA, spawnLoc, 50, 1, 1, 1, 0.1);
        world.spawnParticle(Particle.FLAME, spawnLoc, 30, 1, 1, 1, 0.1);
        world.playSound(spawnLoc, Sound.BLOCK_LAVA_AMBIENT, 2.0F, 0.5F);
        world.playSound(spawnLoc, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 0.5F);
    }

    private static void setupStrider(Strider strider) {
        // Configuración básica del Strider
        strider.setGlowing(true);
        strider.setFireTicks(Integer.MAX_VALUE);
        strider.setCustomName("§6Infernal Strider");
        strider.setCustomNameVisible(true);

        // Mejorar atributos del Strider
        strider.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50.0);
        strider.setHealth(50.0);
        strider.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        strider.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(5.0);

        // Efectos permanentes
        strider.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));

        // Efectos visuales para el Strider
        startStriderEffects(strider);
    }

    private static void setupInfernalGuardian(ElderGuardian entity) {
        // Características base
        entity.setGlowing(true);
        entity.setFireTicks(Integer.MAX_VALUE);
        entity.setCustomName("§6Infernal Guardian");
        entity.setCustomNameVisible(true);

        // Atributos mejorados
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(150.0);
        entity.setHealth(150.0);
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(12.0);
        entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(10.0);
        entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);

        // Efectos permanentes
        entity.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1));
        entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));

        // Iniciar efectos visuales
        startGuardianEffects(entity);
    }

    private static void startStriderEffects(Strider strider) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (strider.isDead() || !strider.isValid()) {
                    this.cancel();
                    return;
                }

                strider.getWorld().spawnParticle(Particle.FLAME,
                        strider.getLocation().add(0, 0.5, 0), 3, 0.2, 0.2, 0.2, 0.02);
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    private static void startGuardianEffects(ElderGuardian guardian) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (guardian.isDead() || !guardian.isValid()) {
                    this.cancel();
                    return;
                }

                guardian.getWorld().spawnParticle(Particle.FLAME,
                        guardian.getLocation(), 5, 0.5, 0.5, 0.5, 0.05);
                guardian.getWorld().spawnParticle(Particle.LAVA,
                        guardian.getLocation(), 1, 0.5, 0.5, 0.5, 0);
            }
        }.runTaskTimer(plugin, 0L, 5L);
    }

    @EventHandler
    public void onInfernalGuardianAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Guardian)) return;
        Guardian guardian = (Guardian) event.getDamager();

        if (!isInfernalGuardian(guardian) || !(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity target = (LivingEntity) event.getEntity();

        // Aumentar daño base
        event.setDamage(event.getDamage() * 2.0);

        // Efectos adicionales
        target.setFireTicks(200); // 10 segundos de fuego
        target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));

        // Efectos visuales y sonoros
        target.getWorld().spawnParticle(Particle.FLAME, target.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 1.0F, 1.0F);
    }

    @EventHandler
    public void onGuardianBeamAttack(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Guardian)) return;
        Guardian guardian = (Guardian) event.getEntity();

        if (!isInfernalGuardian(guardian) || event.getTarget() == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!guardian.hasLineOfSight(event.getTarget()) || guardian.isDead() || !guardian.isValid() ||
                        event.getTarget().isDead() || !event.getTarget().isValid()) {
                    this.cancel();
                    return;
                }

                Location guardianLoc = guardian.getLocation();
                Location targetLoc = event.getTarget().getLocation();
                Vector direction = targetLoc.toVector().subtract(guardianLoc.toVector()).normalize();

                for (double d = 0; d < guardianLoc.distance(targetLoc); d += 0.5) {
                    Location particleLoc = guardianLoc.clone().add(direction.clone().multiply(d));
                    guardian.getWorld().spawnParticle(Particle.FLAME, particleLoc, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Strider) {
            Strider strider = (Strider) event.getEntity();
            if (isInfernalStrider(strider)) {
                handleInfernalMount(event);
            }
        } else if (event.getEntity() instanceof Guardian) {
            Guardian guardian = (Guardian) event.getEntity();
            if (isInfernalGuardian(guardian)) {
                handleInfernalGuardianDeath(event);
            }
        }
    }

    private static void handleInfernalMount(EntityDeathEvent event) {
        event.getDrops().clear();
        Location loc = event.getEntity().getLocation();
        World world = loc.getWorld();

        // Efectos de muerte del Strider
        world.spawnParticle(Particle.LAVA, loc, 30, 1, 1, 1, 0.1);
        world.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 1.5F, 1.0F);
    }

    private static void handleInfernalGuardianDeath(EntityDeathEvent event) {
        event.getDrops().clear();
        Location loc = event.getEntity().getLocation();
        World world = loc.getWorld();

        // Drops garantizados
        world.dropItemNaturally(loc, new ItemStack(Material.NETHERITE_INGOT));

        // Drops aleatorios
        if (random.nextFloat() < 0.3f) {
            world.dropItemNaturally(loc, new ItemStack(Material.BLAZE_ROD, random.nextInt(3) + 1));
        }
        if (random.nextFloat() < 0.2f) {
            world.dropItemNaturally(loc, new ItemStack(Material.MAGMA_CREAM, random.nextInt(4) + 1));
        }

        // Efectos de muerte
        world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 1);
        world.spawnParticle(Particle.LAVA, loc, 50, 1, 1, 1, 0.1);
        world.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 2.0F, 0.5F);
        world.playSound(loc, Sound.BLOCK_FIRE_EXTINGUISH, 2.0F, 1.0F);
    }

    private static boolean isInfernalGuardian(Guardian guardian) {
        return guardian.getFireTicks() > 0 && guardian.isGlowing();
    }

    private static boolean isInfernalStrider(Strider strider) {
        return strider.getFireTicks() > 0 && strider.isGlowing();
    }
}