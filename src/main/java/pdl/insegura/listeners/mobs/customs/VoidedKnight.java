package pdl.insegura.listeners.mobs.customs;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import pdl.insegura.PendulumPlugin;

import java.util.*;

import static pdl.insegura.items.customs.voided.VoidedItems.CrearVoidedShard;

public class VoidedKnight implements Listener {

    public static Map<UUID, BossBar> bossBars = new HashMap<>();
    private Plugin plugin;
    private Map<UUID, Integer> minionSpawnCounters = new HashMap<>();
    private List<UUID> activeMinions = new ArrayList<>();
    private Map<UUID, Integer> phaseCounters = new HashMap<>();
    private static final int MAX_MINIONS = 10;
    private Map<UUID, Integer> attackPatternCounters = new HashMap<>();

    public VoidedKnight(Plugin plugin) {
        this.plugin = plugin;
    }

    private ItemStack VoidedBanner() {
        ItemStack banner = new ItemStack(Material.BLACK_BANNER);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();

        // Patrón mejorado para el banner
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.PURPLE, PatternType.RHOMBUS_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.MAGENTA, PatternType.CIRCLE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.FLOWER));
        bannerMeta.addPattern(new Pattern(DyeColor.PURPLE, PatternType.BORDER));
        bannerMeta.addPattern(new Pattern(DyeColor.MAGENTA, PatternType.GRADIENT_UP));

        bannerMeta.setDisplayName(ChatColor.DARK_PURPLE + "§l✧ Void Knight's Banner ✧");

        // Agregar lore al banner
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.LIGHT_PURPLE + "A symbol of the Void Knight's power");
        lore.add(ChatColor.LIGHT_PURPLE + "Radiates with dark energy");
        bannerMeta.setLore(lore);

        banner.setItemMeta(bannerMeta);
        return banner;
    }

    public void setupVoidedKnight(LivingEntity entity) {
        // Configuración base mejorada
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(750);
        entity.setHealth(750);

        EntityEquipment equipment = entity.getEquipment();
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, 20, true);
        bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_KNOCKBACK, 10, true);
        bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_INFINITE, 1, true);
        bow.setItemMeta(bowMeta);
        equipment.setItemInMainHand(bow);

        equipment.setHelmet(VoidedBanner());

        ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta chestMeta = chestplate.getItemMeta();
        chestMeta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        chestplate.setItemMeta(chestMeta);
        equipment.setChestplate(chestplate);

        equipment.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

        entity.setCustomName(ChatColor.DARK_PURPLE + "§l☠ Voided Knight ☠");
        entity.setCustomNameVisible(true);
        entity.setArrowCooldown(entity.getArrowCooldown()/2);
        entity.setRemoveWhenFarAway(false);
        entity.setGlowing(true);

        entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(5.0);
        entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(25.0);
        entity.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);

        if (!bossBars.containsKey(entity.getUniqueId())) {
            BossBar bossBar = Bukkit.createBossBar(
                    ChatColor.DARK_PURPLE + "§l⚔ Voided Knight ⚔",
                    BarColor.PURPLE,
                    BarStyle.SEGMENTED_12
            );
            bossBar.setProgress(1.0);
            bossBars.put(entity.getUniqueId(), bossBar);

            // Sistema de fases y patrones de ataque
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entity.isValid() || entity.isDead()) {
                        bossBar.removeAll();
                        bossBars.remove(entity.getUniqueId());
                        minionSpawnCounters.remove(entity.getUniqueId());
                        phaseCounters.remove(entity.getUniqueId());
                        activeMinions.clear();
                        cancel();
                        return;
                    }

                    double healthPercent = entity.getHealth() / entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    bossBar.setProgress(healthPercent);

                    // Gestión de fases basada en la salud
                    handlePhases(entity, healthPercent);

                    // Contador de patrones de ataque
                    int attackCounter = attackPatternCounters.getOrDefault(entity.getUniqueId(), 0);
                    attackCounter++;

                    // Ejecutar patrones de ataque cada 10 segundos
                    if (attackCounter >= 10) {
                        executeAttackPattern(entity, healthPercent);
                        attackCounter = 0;
                    }

                    attackPatternCounters.put(entity.getUniqueId(), attackCounter);
                    updateBossBarVisibility(entity);
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    private void handlePhases(LivingEntity entity, double healthPercent) {
        if (healthPercent <= 0.75 && healthPercent > 0.5) { // Fase 2
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 1));
            spawnVoidRift(entity.getLocation());
        } else if (healthPercent <= 0.5 && healthPercent > 0.25) { // Fase 3
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
        } else if (healthPercent <= 0.25) { // Fase final
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 3));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 2));
            entity.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 1));
            voidExplosion(entity);
        }
    }

    private void executeAttackPattern(LivingEntity entity, double healthPercent) {
        Random random = new Random();
        int pattern = random.nextInt(4);

        switch (pattern) {
            case 0:
                teleportAttack(entity);
                break;
            case 1:
                if (healthPercent <= 0.75) {
                    summonVoidWall(entity);
                }
                break;
            case 2:
                if (healthPercent <= 0.5) {
                    voidPull(entity);
                }
                break;
            case 3:
                if (activeMinions.size() < MAX_MINIONS) {
                    spawnEnhancedMinions(entity.getLocation());
                }
                break;
        }
    }

    private void teleportAttack(LivingEntity entity) {
        for (Player player : entity.getLocation().getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 50) {
                Location behind = player.getLocation().add(player.getLocation().getDirection().multiply(-2));
                entity.teleport(behind);
                entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, entity.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
            }
        }
    }

    private void summonVoidWall(LivingEntity entity) {
        Location loc = entity.getLocation();
        for (int i = -3; i <= 3; i++) {
            Location wallLoc = loc.clone().add(i * 2, 0, i * 2);
            entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, wallLoc, 100, 0.5, 2, 0.5, 0.1);

            for (Entity nearby : wallLoc.getWorld().getNearbyEntities(wallLoc, 2, 2, 2)) {
                if (nearby instanceof Player) {
                    Player player = (Player) nearby;
                    player.damage(10.0, entity);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                }
            }
        }
    }

    private void voidPull(LivingEntity entity) {
        for (Player player : entity.getLocation().getWorld().getPlayers()) {
            if (player.getLocation().distance(entity.getLocation()) <= 20) {
                Vector direction = entity.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                player.setVelocity(direction.multiply(2));
                player.getWorld().spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 50, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }

    private void spawnEnhancedMinions(Location location) {
        for (int i = 0; i < 2; i++) {
            if (activeMinions.size() >= MAX_MINIONS) break;

            Location spawnLoc = location.clone().add(
                    (Math.random() - 0.5) * 10,
                    0,
                    (Math.random() - 0.5) * 10
            );

            Phantom phantom = (Phantom) location.getWorld().spawnEntity(spawnLoc, EntityType.PHANTOM);
            customizeEnhancedMinion(phantom);
            activeMinions.add(phantom.getUniqueId());
        }
    }

    private void customizeEnhancedMinion(Phantom phantom) {
        phantom.setCustomName(ChatColor.DARK_PURPLE + "☠ Void Phantom ☠");
        phantom.setCustomNameVisible(true);
        phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        phantom.setHealth(40);
        phantom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15);
        phantom.setSize(2);
        phantom.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(2.0);
        phantom.setRemoveWhenFarAway(true);
        phantom.setGlowing(true);
        phantom.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        phantom.addScoreboardTag("voided_minion");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (phantom.isDead()) {
                    cancel();
                    return;
                }

                Player target = null;
                double minDistance = Double.MAX_VALUE;

                for (Player player : phantom.getWorld().getPlayers()) {
                    double distance = player.getLocation().distance(phantom.getLocation());
                    if (distance < minDistance && distance <= 50) {
                        minDistance = distance;
                        target = player;
                    }
                }

                if (target != null) {
                    phantom.setTarget(target);
                    // Ataque especial cada cierto tiempo
                    if (Math.random() < 0.1) {
                        phantom.getWorld().spawnParticle(
                                Particle.DRAGON_BREATH,
                                phantom.getLocation(),
                                50,
                                0.5,
                                0.5,
                                0.5,
                                0.1
                        );
                        target.damage(5.0, phantom);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void spawnVoidRift(Location location) {
        location.getWorld().spawnParticle(
                Particle.PORTAL,
                location,
                100,
                2,
                2,
                2,
                0.1
        );

        for (Entity entity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
            if (entity instanceof Player) {
                Player player = (Player) entity;
                player.damage(5.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, 1));
            }
        }
    }

    private void voidExplosion(LivingEntity entity) {
        Location loc = entity.getLocation();
        entity.getWorld().spawnParticle(
                Particle.DRAGON_BREATH,
                loc,
                200,
                3,
                3,
                3,
                0.1
        );
        entity.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 0.5f);

        for (Entity nearby : loc.getWorld().getNearbyEntities(loc, 8, 8, 8)) {
            if (nearby instanceof Player) {
                Player player = (Player) nearby;
                player.damage(15.0, entity);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                Vector knockback = player.getLocation().toVector().subtract(loc.toVector()).normalize().multiply(2);
                player.setVelocity(knockback);
            }
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if (event.getEntity().getScoreboardTags().contains("voided_minion")) {
            event.setCancelled(true);
        }
    }

    private void updateBossBarVisibility(LivingEntity boss) {
        BossBar bossBar = bossBars.get(boss.getUniqueId());
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld().equals(boss.getWorld()) && player.getLocation().distance(boss.getLocation()) <= 50) {
                bossBar.addPlayer(player);
            } else {
                bossBar.removePlayer(player);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Location deathLocation = entity.getLocation();

        if (entity.getScoreboardTags().contains("voided_minion")) {
            event.getDrops().clear();
            if (Math.random() < 0.3) { // 30% de probabilidad de soltar un Voided Shard
                ItemStack voidedShard = CrearVoidedShard();
                deathLocation.getWorld().dropItemNaturally(deathLocation, voidedShard);
            }
            activeMinions.remove(entity.getUniqueId());

            // Efecto de muerte para los minions
            entity.getWorld().spawnParticle(Particle.DRAGON_BREATH, deathLocation, 50, 0.5, 0.5, 0.5, 0.1);
            entity.getWorld().playSound(deathLocation, Sound.ENTITY_PHANTOM_DEATH, 1.0f, 0.5f);
        } else if (bossBars.containsKey(entity.getUniqueId())) {
            // Muerte del Voided Knight
            BossBar bossBar = bossBars.get(entity.getUniqueId());
            bossBar.removeAll();
            bossBars.remove(entity.getUniqueId());

            // Drops mejorados
            event.getDrops().clear();

            // Voided Bow mejorado
            ItemStack voidedBow = new ItemStack(Material.BOW);
            ItemMeta bowMeta = voidedBow.getItemMeta();
            bowMeta.setDisplayName(ChatColor.DARK_PURPLE + "§l✧ Void Striker ✧");
            List<String> bowLore = new ArrayList<>();
            bowLore.add(ChatColor.LIGHT_PURPLE + "Forged in the depths of the void");
            bowLore.add(ChatColor.LIGHT_PURPLE + "A weapon of immense power");
            bowMeta.setLore(bowLore);
            bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, 10, true);
            bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_KNOCKBACK, 2, true);
            bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_INFINITE, 1, true);
            voidedBow.setItemMeta(bowMeta);

            // Voided Armor piece
            ItemStack voidedArmor = new ItemStack(Material.NETHERITE_CHESTPLATE);
            ItemMeta armorMeta = voidedArmor.getItemMeta();
            armorMeta.setDisplayName(ChatColor.DARK_PURPLE + "§l✧ Void Warrior's Plate ✧");
            List<String> armorLore = new ArrayList<>();
            armorLore.add(ChatColor.LIGHT_PURPLE + "Infused with void energy");
            armorLore.add(ChatColor.LIGHT_PURPLE + "Grants supernatural protection");
            armorMeta.setLore(armorLore);
            armorMeta.addEnchant(org.bukkit.enchantments.Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
            armorMeta.addEnchant(org.bukkit.enchantments.Enchantment.THORNS, 3, true);
            voidedArmor.setItemMeta(armorMeta);

            // Drops
            deathLocation.getWorld().dropItemNaturally(deathLocation, voidedBow);
            deathLocation.getWorld().dropItemNaturally(deathLocation, voidedArmor);
            deathLocation.getWorld().dropItemNaturally(deathLocation, VoidedBanner());

            // Efecto de muerte épico
            deathLocation.getWorld().strikeLightningEffect(deathLocation);
            deathLocation.getWorld().spawnParticle(Particle.DRAGON_BREATH, deathLocation, 500, 3, 3, 3, 0.1);
            deathLocation.getWorld().playSound(deathLocation, Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);

            // Efecto para todos los jugadores cercanos
            for (Player player : deathLocation.getWorld().getPlayers()) {
                if (player.getLocation().distance(deathLocation) <= 50) {
                    player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    player.sendTitle(
                            ChatColor.DARK_PURPLE + "§l✧ Voided Knight Defeated ✧",
                            ChatColor.LIGHT_PURPLE + "The void grows quiet...",
                            10,
                            70,
                            20
                    );
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (bossBars.containsKey(entity.getUniqueId())) {
                // Actualizar barra de boss
                BossBar bossBar = bossBars.get(entity.getUniqueId());
                double healthPercent = entity.getHealth() / entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                bossBar.setProgress(healthPercent);
                updateBossBarVisibility(entity);

                // Efectos de daño
                entity.getWorld().spawnParticle(
                        Particle.DRAGON_BREATH,
                        entity.getLocation(),
                        20,
                        0.5,
                        0.5,
                        0.5,
                        0.1
                );
                entity.getWorld().playSound(
                        entity.getLocation(),
                        Sound.ENTITY_ENDERMAN_HURT,
                        1.0f,
                        0.5f
                );
            }
        }
    }
}