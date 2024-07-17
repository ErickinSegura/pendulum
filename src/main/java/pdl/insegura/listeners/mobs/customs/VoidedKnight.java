package pdl.insegura.listeners.mobs.customs;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.PendulumPlugin;

import java.util.*;

import static pdl.insegura.items.customs.voided.VoidedItems.CrearVoidedShard;

public class VoidedKnight implements Listener {

    public static Map<UUID, BossBar> bossBars = new HashMap<>();
    private Plugin plugin;
    private Map<UUID, Integer> minionSpawnCounters = new HashMap<>();
    private List<UUID> activeMinions = new ArrayList<>();
    private static final int MAX_MINIONS = 10;

    public VoidedKnight(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setupVoidedKnight(LivingEntity entity) {
        // Configurar salud
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
        entity.setHealth(500);

        // Equipar al jefe con arco Power 10
        EntityEquipment equipment = entity.getEquipment();
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, 20, true);
        bowMeta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_KNOCKBACK, 10, true);
        bow.setItemMeta(bowMeta);
        equipment.setItemInMainHand(bow);

        equipment.setHelmet(VoidedBanner());
        equipment.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        equipment.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.NETHERITE_BOOTS));

        // Hacer que el jefe parezca más grande
        entity.setCustomName(ChatColor.DARK_PURPLE + "§lVoided Knight");
        entity.setCustomNameVisible(true);
        entity.setArrowCooldown(entity.getArrowCooldown()/2);

        entity.setRemoveWhenFarAway(false);


        // Aumentar el alcance de ataque y el knockback resistance
        entity.getAttribute(Attribute.GENERIC_ATTACK_KNOCKBACK).setBaseValue(5.0);
        entity.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1.0);
        entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20.0);

        // Verificar si ya existe una BossBar para esta entidad
        if (!bossBars.containsKey(entity.getUniqueId())) {
            // Crear y configurar la barra de jefe
            BossBar bossBar = Bukkit.createBossBar(ChatColor.DARK_PURPLE + "§l⚔ Voided Knight ⚔", BarColor.PURPLE, BarStyle.SOLID);
            bossBar.setProgress(1.0);
            bossBars.put(entity.getUniqueId(), bossBar);

            // Actualización periódica de la barra de jefe y spawn de minions
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entity.isValid() || entity.isDead()) {
                        bossBar.removeAll();
                        bossBars.remove(entity.getUniqueId());
                        minionSpawnCounters.remove(entity.getUniqueId());
                        activeMinions.clear();
                        cancel();
                        return;
                    }

                    double healthPercent = entity.getHealth() / entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                    bossBar.setProgress(healthPercent);

                    // Spawn de minions cada 30 segundos (600 ticks)
                    int counter = minionSpawnCounters.getOrDefault(entity.getUniqueId(), 0);
                    counter++;
                    if (counter >= 15 && activeMinions.size() < MAX_MINIONS) {
                        spawnMinions(entity.getLocation());
                        counter = 0;
                    }
                    minionSpawnCounters.put(entity.getUniqueId(), counter);
                    updateBossBarVisibility(entity);
                }
            }.runTaskTimer(plugin, 0, 20); // Actualiza cada segundo (20 ticks)

            // Mostrar la barra de jefe a los jugadores cercanos

        }
    }

    private void spawnMinions(Location location) {
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            if (activeMinions.size() >= MAX_MINIONS) {
                break;
            }

            // Genera coordenadas aleatorias dentro de un radio de 10 bloques
            double offsetX = (random.nextDouble() * 20) - 10; // De -10 a 10
            double offsetZ = (random.nextDouble() * 20) - 10; // De -10 a 10
            Location spawnLocation = location.clone().add(offsetX, 0, offsetZ);

            Phantom phantom = (Phantom) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.PHANTOM);
            customizeMinion(phantom);
            activeMinions.add(phantom.getUniqueId());
        }
    }

    private ItemStack VoidedBanner() {
        ItemStack banner = new ItemStack(Material.BLACK_BANNER);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();

        // Añadir patrones al banner
        bannerMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.PURPLE, PatternType.RHOMBUS_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_DOWNRIGHT));
        bannerMeta.addPattern(new Pattern(DyeColor.MAGENTA, PatternType.CIRCLE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.FLOWER));

        bannerMeta.setDisplayName(ChatColor.DARK_PURPLE + "Voided Banner");

        banner.setItemMeta(bannerMeta);

        return banner;
    }

    private void customizeMinion(Phantom phantom) {
        // Nombre personalizado
        phantom.setCustomName(ChatColor.DARK_PURPLE + "Voided Minion");
        phantom.setCustomNameVisible(true);

        // Duplicar la vida
        double defaultMaxHealth = phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        phantom.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(defaultMaxHealth * 2);
        phantom.setHealth(defaultMaxHealth * 2);

        // Triplicar daño
        phantom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(phantom.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 3);

        phantom.setSize((int) (phantom.getSize() * 0.5));

        // Aumentar velocidad
        phantom.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(phantom.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() * 1.5);

        phantom.setRemoveWhenFarAway(true);
        phantom.setGlowing(true);

        // Añadir tag para identificar
        phantom.addScoreboardTag("voided_minion");


        // Hacer que el minion ataque a jugadores cercanos
        new BukkitRunnable() {
            @Override
            public void run() {
                if (phantom.isDead()) {
                    cancel();
                    return;
                }
                Player nearestPlayer = null;
                double nearestDistance = Double.MAX_VALUE;
                for (Player player : phantom.getWorld().getPlayers()) {
                    double distance = player.getLocation().distance(phantom.getLocation());
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        nearestPlayer = player;
                    }
                }
                if (nearestPlayer != null) {
                    phantom.setTarget(nearestPlayer);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Ejecutar cada segundo
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
            // Es un minion del Voided Knight
            event.getDrops().clear(); // Limpiar drops por defecto
            ItemStack voidedShard = CrearVoidedShard();
            deathLocation.getWorld().dropItemNaturally(deathLocation, voidedShard);
            activeMinions.remove(entity.getUniqueId()); // Eliminar de la lista de minions activos
        } else if (bossBars.containsKey(entity.getUniqueId())) {
            // Es el Voided Knight
            BossBar bossBar = bossBars.get(entity.getUniqueId());
            bossBar.removeAll();
            bossBars.remove(entity.getUniqueId());

            // Drop del arco especial
            ItemStack bow = new ItemStack(Material.BOW);
            ItemMeta meta = bow.getItemMeta();
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Voided Bow");
            meta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_DAMAGE, 10, true);
            bow.setItemMeta(meta);

            event.getDrops().clear(); // Limpiar drops existentes
            deathLocation.getWorld().dropItemNaturally(deathLocation, bow);
            deathLocation.getWorld().dropItemNaturally(deathLocation, VoidedBanner());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            if (bossBars.containsKey(entity.getUniqueId())) {
                BossBar bossBar = bossBars.get(entity.getUniqueId());
                double healthPercent = entity.getHealth() / entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                bossBar.setProgress(healthPercent);
                updateBossBarVisibility(entity);
            }
        }
    }





}
