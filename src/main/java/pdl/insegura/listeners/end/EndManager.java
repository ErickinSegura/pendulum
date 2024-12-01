package pdl.insegura.listeners.end;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EndManager implements Listener {
    private final Plugin plugin;
    private final Random random = new Random();
    private int attackCounter = 0;

    public EndManager(Plugin plugin) {
        this.plugin = plugin;
        startPeriodicAttacks();
    }

    private void startPeriodicAttacks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World endWorld = Bukkit.getWorld("world_the_end");
                if (endWorld != null) {
                    for (EnderDragon dragon : endWorld.getEntitiesByClass(EnderDragon.class)) {
                        if (!dragon.isDead()) {
                            dragonPeriodicAttack(dragon);
                            // Probabilidad aleatoria de realizar un ataque especial
                            if (random.nextDouble() < 0.4) {
                                dragonSpecialAttack(dragon);
                            }
                        }
                    }
                }
            }
            // Random delay between 10-20 seconds (200-400 ticks)
        }.runTaskTimer(plugin, 200L, 200L + random.nextInt(200));
    }

    @EventHandler
    public void onEntityGetHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderDragon dragon) {
            if (event.getDamager() instanceof Explosive ||
                    event.getCause() == EntityDamageByEntityEvent.DamageCause.BLOCK_EXPLOSION ||
                    event.getCause() == EntityDamageByEntityEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setCancelled(true);
                return;
            }

            if (dragon.getHealth() <= dragon.getMaxHealth() * 0.75) {
                enragedDragon(dragon);
            }
        }

        if (event.getEntity() instanceof Endermite || event.getEntity() instanceof Silverfish) {
            if (event.getDamager() instanceof Explosive ||
                    event.getCause() == EntityDamageByEntityEvent.DamageCause.BLOCK_EXPLOSION ||
                    event.getCause() == EntityDamageByEntityEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setCancelled(true);
                return;
            }
        }


        if (event.getEntity() instanceof EnderCrystal crystal) {
            if (event.getDamager() instanceof Arrow || event.getDamager() instanceof Snowball) {
                event.setCancelled(true);
            } else if (event.getDamager() instanceof Player) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        crystal.getWorld().spawnEntity(crystal.getLocation(), EntityType.ENDERMITE);
                        crystal.getWorld().spawnEntity(crystal.getLocation(), EntityType.ENDERMITE);
                        crystal.getWorld().createExplosion(crystal.getLocation(), 4F, false);
                    }
                }.runTaskLater(plugin, 1L);
            }
        }
    }

    private void dragonPeriodicAttack(EnderDragon dragon) {
        attackCounter++;
        World world = dragon.getWorld();
        Location dragonLoc = dragon.getLocation();

        if (attackCounter % 3 == 0) {
            // Cada tercer ataque es más poderoso
            switch (random.nextInt(3)) {
                case 0: // Invocar Phantoms agresivos
                    for (Entity nearby : dragon.getNearbyEntities(40, 40, 40)) {
                        if (nearby instanceof Player) {
                            Location spawnLoc = nearby.getLocation().add(0, 15, 0);
                            Phantom phantom = (Phantom) world.spawnEntity(spawnLoc, EntityType.PHANTOM);
                            phantom.setSize(random.nextInt(5) + 3);
                        }
                    }
                    break;

                case 1: // Círculo de TNT
                    new BukkitRunnable() {
                        int angle = 0;
                        @Override
                        public void run() {
                            if (angle >= 360 || dragon.isDead()) {
                                this.cancel();
                                return;
                            }
                            double radians = Math.toRadians(angle);
                            Location tntLoc = dragonLoc.clone().add(
                                    Math.cos(radians) * 10,
                                    0,
                                    Math.sin(radians) * 10
                            );
                            TNTPrimed tnt = world.spawn(tntLoc, TNTPrimed.class);
                            tnt.setFuseTicks(40);
                            angle += 45;
                        }
                    }.runTaskTimer(plugin, 0L, 2L);
                    break;

                case 2: // Lluvia de anvils
                    new BukkitRunnable() {
                        int count = 0;
                        @Override
                        public void run() {
                            if (count++ >= 5 || dragon.isDead()) {
                                this.cancel();
                                return;
                            }
                            for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                                if (nearby instanceof Player) {
                                    Location anvilLoc = nearby.getLocation().clone().add(
                                            random.nextInt(7) - 3,
                                            10,
                                            random.nextInt(7) - 3
                                    );
                                    world.spawnFallingBlock(anvilLoc, Material.ANVIL.createBlockData());
                                }
                            }
                        }
                    }.runTaskTimer(plugin, 0L, 10L);
                    break;
            }
        }
    }

    private void dragonSpecialAttack(EnderDragon dragon) {
        World world = dragon.getWorld();
        Location dragonLoc = dragon.getLocation();

        switch (random.nextInt(25)) { // Aumentado a 25 tipos de ataques
            case 0: // Lluvia de rayos con TNT
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 3 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(25, 25, 25)) {
                            if (nearby instanceof Player) {
                                Location loc = nearby.getLocation();
                                world.strikeLightning(loc);
                                TNTPrimed tnt = world.spawn(loc, TNTPrimed.class);
                                tnt.setFuseTicks(20);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 15L);
                break;

            case 1: // Shulker preciso
                for (Entity nearby : dragon.getNearbyEntities(40, 40, 40)) {
                    if (nearby instanceof Player && random.nextBoolean()) {
                        Location loc = nearby.getLocation();
                        world.spawnEntity(loc, EntityType.SHULKER);
                    }
                }
                break;

            case 2: // Minas de TNT
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 5 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        Location explosionLoc = dragonLoc.clone().add(random.nextInt(40) - 20, -5, random.nextInt(40) - 20);
                        TNTPrimed tnt = world.spawn(explosionLoc, TNTPrimed.class);
                        tnt.setFuseTicks(60);
                    }
                }.runTaskTimer(plugin, 0L, 10L);
                break;

            case 3: // Levitación y lanzamiento con TNT
                for (Entity nearby : dragon.getNearbyEntities(20, 20, 20)) {
                    if (nearby instanceof Player player) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 100, 2));
                        Vector velocity = player.getLocation().toVector().subtract(dragon.getLocation().toVector()).normalize();
                        player.setVelocity(velocity.multiply(2));
                        // Spawn TNT that follows the player
                        new BukkitRunnable() {
                            int count = 0;
                            @Override
                            public void run() {
                                if (count++ >= 5 || dragon.isDead() || !player.isOnline()) {
                                    this.cancel();
                                    return;
                                }
                                TNTPrimed tnt = world.spawn(player.getLocation(), TNTPrimed.class);
                                tnt.setFuseTicks(20);
                            }
                        }.runTaskTimer(plugin, 20L, 20L);
                    }
                }
                break;

            case 4: // Invocación de Blazes con TNT
                for (int i = 0; i < 2; i++) {
                    Location blazeLoc = dragonLoc.clone().add(random.nextInt(20) - 10, -2, random.nextInt(20) - 10);
                    Blaze blaze = (Blaze) world.spawnEntity(blazeLoc, EntityType.BLAZE);
                    blaze.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                    TNTPrimed tnt = world.spawn(blazeLoc, TNTPrimed.class);
                    tnt.setFuseTicks(40);
                }
                break;

            case 5: // Lluvia de flechas explosivas
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 3 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                            if (nearby instanceof Player) {
                                Location arrowLoc = nearby.getLocation().clone().add(0, 15, 0);
                                for (int i = 0; i < 3; i++) {
                                    Arrow arrow = world.spawnArrow(arrowLoc, new Vector(random.nextDouble() - 0.5, -1, random.nextDouble() - 0.5), 1.5f, 12);
                                    arrow.setGlowing(true);
                                }
                                TNTPrimed tnt = world.spawn(arrowLoc, TNTPrimed.class);
                                tnt.setFuseTicks(30);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 15L);
                break;

            case 6: // Guardián del End
                for (int i = 0; i < 2; i++) {
                    Location guardianLoc = dragonLoc.clone().add(random.nextInt(20) - 10, -2, random.nextInt(20) - 10);
                    Guardian guardian = (Guardian) world.spawnEntity(guardianLoc, EntityType.ELDER_GUARDIAN);
                    guardian.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                    guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
                }
                break;

            case 7: // Ataque de Magma Cubes
                for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player) {
                        Location cubeLoc = nearby.getLocation();
                        MagmaCube cube = (MagmaCube) world.spawnEntity(cubeLoc, EntityType.MAGMA_CUBE);
                        cube.setSize(random.nextInt(8) + 4);
                    }
                }
                break;

            case 8: // Jinetes esqueletos
                for (Entity nearby : dragon.getNearbyEntities(40, 40, 40)) {
                    if (nearby instanceof Player && random.nextBoolean()) {
                        Location loc = nearby.getLocation();
                        Skeleton skeleton = (Skeleton) world.spawnEntity(loc, EntityType.SKELETON);
                        Spider spider = (Spider) world.spawnEntity(loc, EntityType.SPIDER);
                        spider.addPassenger(skeleton);
                        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                    }
                }
                break;

            case 9: // Creepers potenciados
                for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player && random.nextBoolean()) {
                        Location loc = nearby.getLocation();
                        Creeper creeper = (Creeper) world.spawnEntity(loc, EntityType.CREEPER);
                        creeper.setPowered(true);
                        creeper.setExplosionRadius(5);
                    }
                }
                break;

            case 10: // Ataque de Wither Skeletons
                for (int i = 0; i < 3; i++) {
                    Location loc = dragonLoc.clone().add(random.nextInt(20) - 10, -2, random.nextInt(20) - 10);
                    WitherSkeleton skeleton = (WitherSkeleton) world.spawnEntity(loc, EntityType.WITHER_SKELETON);
                    skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                    skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                }
                break;

            case 11: // Ataque combinado
                Location loc = dragonLoc.clone();
                // Spawn Wither Skeleton montado en un Ravager
                Ravager ravager = (Ravager) world.spawnEntity(loc, EntityType.RAVAGER);
                WitherSkeleton skeleton = (WitherSkeleton) world.spawnEntity(loc, EntityType.WITHER_SKELETON);
                ravager.addPassenger(skeleton);
                // Dar efectos al Ravager
                ravager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                ravager.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                break;
            case 12: // Tormenta de End Crystals
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 3 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                            if (nearby instanceof Player) {
                                Location crystalLoc = nearby.getLocation().add(
                                        random.nextInt(10) - 5,
                                        5,
                                        random.nextInt(10) - 5
                                );
                                EnderCrystal crystal = world.spawn(crystalLoc, EnderCrystal.class);
                                crystal.setShowingBottom(false);
                                crystal.setInvulnerable(false);

                                // Explotar después de un tiempo
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (!crystal.isDead()) {
                                            crystal.getWorld().createExplosion(crystal.getLocation(), 4F, false);
                                            crystal.remove();
                                        }
                                    }
                                }.runTaskLater(plugin, 40L);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
                break;

            case 13: // Jaula de Bedrock temporal
                for (Entity nearby : dragon.getNearbyEntities(40, 40, 40)) {
                    if (nearby instanceof Player player) {
                        Location playerLoc = player.getLocation();
                        List<Location> cageBlocks = new ArrayList<>();

                        // Crear jaula temporal
                        for (int x = -1; x <= 1; x++) {
                            for (int y = 0; y <= 2; y++) {
                                for (int z = -1; z <= 1; z++) {
                                    if (y == 0 || y == 2 || x == -1 || x == 1 || z == -1 || z == 1) {
                                        Location blockLoc = playerLoc.clone().add(x, y, z);
                                        blockLoc.getBlock().setType(Material.BEDROCK);
                                        cageBlocks.add(blockLoc);
                                    }
                                }
                            }
                        }

                        // Remover la jaula después de 5 segundos
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Location loc : cageBlocks) {
                                    loc.getBlock().setType(Material.AIR);
                                }
                            }
                        }.runTaskLater(plugin, 100L);
                    }
                }
                break;

            case 14: // Invocación de Shulkers explosivos
                for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player) {
                        Location shulkerLoc = nearby.getLocation().add(0, 3, 0);
                        Shulker shulker = (Shulker) world.spawnEntity(shulkerLoc, EntityType.SHULKER);
                        shulker.setGlowing(true);
                        shulker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 4));

                        // Explotar después de un tiempo
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!shulker.isDead()) {
                                    Location loc = shulker.getLocation();
                                    shulker.remove();
                                    world.createExplosion(loc, 4F, false);
                                    for (int i = 0; i < 3; i++) {
                                        world.spawnEntity(loc, EntityType.SHULKER_BULLET);
                                    }
                                }
                            }
                        }.runTaskLater(plugin, 60L);
                    }
                }
                break;

            case 15: // Tormenta de rayos y endermites
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 5 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(25, 25, 25)) {
                            if (nearby instanceof Player) {
                                Location loc = nearby.getLocation();
                                world.strikeLightning(loc);

                                // Spawn endermites con efectos
                                Endermite endermite = (Endermite) world.spawnEntity(loc, EntityType.ENDERMITE);
                                endermite.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
                                endermite.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 15L);
                break;

            case 16: // Teletransporte masivo
                for (Entity nearby : dragon.getNearbyEntities(50, 50, 50)) {
                    if (nearby instanceof Player player) {
                        // Teletransportar a una posición aleatoria en el aire
                        Location teleportLoc = player.getLocation().add(
                                random.nextInt(40) - 20,
                                random.nextInt(20) + 10,
                                random.nextInt(40) - 20
                        );
                        player.teleport(teleportLoc);

                        // Efectos visuales y de sonido
                        world.spawnParticle(Particle.PORTAL, player.getLocation(), 100, 1, 1, 1, 0.5);
                        world.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);

                        // Invocar algunos endermites en la ubicación original
                        Location originalLoc = player.getLocation();
                        for (int i = 0; i < 3; i++) {
                            world.spawnEntity(originalLoc, EntityType.ENDERMITE);
                        }
                    }
                }
                break;

            case 17: // Evoker corrupto
                Location evokerLoc = dragonLoc.clone().add(random.nextInt(20) - 10, -2, random.nextInt(20) - 10);
                Evoker evoker = (Evoker) world.spawnEntity(evokerLoc, EntityType.EVOKER);
                evoker.setGlowing(true);
                evoker.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
                evoker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));

                // Darle una montura especial
                Ravager mount = (Ravager) world.spawnEntity(evokerLoc, EntityType.RAVAGER);
                mount.addPassenger(evoker);
                mount.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

                // Invocar Vex modificados periódicamente
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 5 || evoker.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (int i = 0; i < 3; i++) {
                            Vex vex = (Vex) world.spawnEntity(evoker.getLocation(), EntityType.VEX);
                            vex.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
                            vex.setGlowing(true);
                        }
                    }
                }.runTaskTimer(plugin, 0L, 100L);
                break;

            case 18: // Lluvia de pociones
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 5 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                            if (nearby instanceof Player) {
                                Location potionLoc = nearby.getLocation().clone().add(
                                        random.nextInt(10) - 5,
                                        10,
                                        random.nextInt(10) - 5
                                );

                                ThrownPotion potion = (ThrownPotion) world.spawnEntity(potionLoc, EntityType.SPLASH_POTION);
                                ItemStack potionItem = new ItemStack(Material.SPLASH_POTION);
                                PotionMeta meta = (PotionMeta) potionItem.getItemMeta();

                                // Efectos aleatorios negativos
                                switch (random.nextInt(5)) {
                                    case 0:
                                        meta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 200, 1), true);
                                        break;
                                    case 1:
                                        meta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 1), true);
                                        break;
                                    case 2:
                                        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2), true);
                                        break;
                                    case 3:
                                        meta.addCustomEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1), true);
                                        break;
                                    case 4:
                                        meta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0), true);
                                        break;
                                }

                                potionItem.setItemMeta(meta);
                                potion.setItem(potionItem);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
                break;

            case 19: // Ejército de Silverfish explosivos
                for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                    if (nearby instanceof Player) {
                        Location spawnLoc = nearby.getLocation();
                        for (int i = 0; i < 8; i++) {
                            Silverfish silverfish = (Silverfish) world.spawnEntity(spawnLoc, EntityType.SILVERFISH);
                            silverfish.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                            silverfish.setGlowing(true);

                            // Explotar al morir
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (silverfish.isDead()) {
                                        this.cancel();
                                        world.createExplosion(silverfish.getLocation(), 2F, false);
                                    }
                                }
                            }.runTaskTimer(plugin, 0L, 5L);
                        }
                    }
                }
                break;

            case 20: // Lluvia de anvils con TNT
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 3 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                            if (nearby instanceof Player) {
                                Location anvilLoc = nearby.getLocation().clone().add(
                                        random.nextInt(10) - 5,
                                        15,
                                        random.nextInt(10) - 5
                                );

                                // Crear anvil cayendo
                                world.spawnFallingBlock(anvilLoc, Material.ANVIL.createBlockData());

                                // TNT que cae junto con el anvil
                                TNTPrimed tnt = world.spawn(anvilLoc, TNTPrimed.class);
                                tnt.setFuseTicks(40);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
                break;

            case 21: // Guardián del End con minions
                Location guardianLoc = dragonLoc.clone().add(random.nextInt(20) - 10, -2, random.nextInt(20) - 10);
                Guardian guardian = (Guardian) world.spawnEntity(guardianLoc, EntityType.ELDER_GUARDIAN);
                guardian.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
                guardian.setGlowing(true);

                // Spawn minions (guardianes normales)
                for (int i = 0; i < 4; i++) {
                    Location minionLoc = guardianLoc.clone().add(
                            random.nextInt(6) - 3,
                            random.nextInt(4) - 2,
                            random.nextInt(6) - 3
                    );
                    Guardian minion = (Guardian) world.spawnEntity(minionLoc, EntityType.GUARDIAN);
                    minion.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                }
                break;

            case 22: // Campo minado de End Crystals
                List<Location> crystalLocations = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    Location crystalLoc = dragonLoc.clone().add(
                            random.nextInt(40) - 20,
                            -2,
                            random.nextInt(40) - 20
                    );
                    EnderCrystal crystal = world.spawn(crystalLoc, EnderCrystal.class);
                    crystal.setShowingBottom(false);
                    crystalLocations.add(crystalLoc);
                }

                // Conectar crystales con rayos después de un tiempo
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < crystalLocations.size() - 1; i++) {
                            Location loc1 = crystalLocations.get(i);
                            Location loc2 = crystalLocations.get(i + 1);
                            world.strikeLightning(loc1);
                            world.strikeLightning(loc2);
                            world.createExplosion(loc1, 4F, false);
                            world.createExplosion(loc2, 4F, false);
                        }
                    }
                }.runTaskLater(plugin, 60L);
                break;

            case 23: // Tornado de Shulker Bullets
                Location centerLoc = dragonLoc.clone();
                new BukkitRunnable() {
                    double angle = 0;
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 20 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }

                        double x = Math.cos(angle) * 5;
                        double z = Math.sin(angle) * 5;
                        Location bulletLoc = centerLoc.clone().add(x, 0, z);

                        ShulkerBullet bullet = (ShulkerBullet) world.spawnEntity(bulletLoc, EntityType.SHULKER_BULLET);
                        bullet.setGlowing(true);

                        angle += Math.PI / 8;
                    }
                }.runTaskTimer(plugin, 0L, 2L);
                break;

            case 24: // Ataque definitivo
                // Combina varios ataques en uno solo
                // Invocar Wither Skeleton en Ravager
                Location witherLoc = dragonLoc.clone();
                Ravager ravagers = (Ravager) world.spawnEntity(witherLoc, EntityType.RAVAGER);
                WitherSkeleton skeletons = (WitherSkeleton) world.spawnEntity(witherLoc, EntityType.WITHER_SKELETON);
                ravagers.addPassenger(skeletons);
                ravagers.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
                skeletons.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));

                // Lluvia de TNT
                new BukkitRunnable() {
                    int count = 0;
                    @Override
                    public void run() {
                        if (count++ >= 5 || dragon.isDead()) {
                            this.cancel();
                            return;
                        }
                        for (Entity nearby : dragon.getNearbyEntities(30, 30, 30)) {
                            if (nearby instanceof Player) {
                                Location tntLoc = nearby.getLocation().clone().add(
                                        random.nextInt(10) - 5,
                                        10,
                                        random.nextInt(10) - 5
                                );
                                TNTPrimed tnt = world.spawn(tntLoc, TNTPrimed.class);
                                tnt.setFuseTicks(30);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 0L, 10L);

                // Invocar Shulkers y End Crystals
                for (int i = 0; i < 4; i++) {
                    Location spawnLoc = dragonLoc.clone().add(
                            random.nextInt(20) - 10,
                            0,
                            random.nextInt(20) - 10
                    );
                    world.spawnEntity(spawnLoc, EntityType.SHULKER);
                    Location crystalLoc = spawnLoc.clone().add(0, 3, 0);
                    EnderCrystal crystal = world.spawn(crystalLoc, EnderCrystal.class);
                    crystal.setShowingBottom(false);
                }
                break;
        }
    }

    private void enragedDragon(EnderDragon dragon) {
        // Efectos visuales de furia
        dragon.getWorld().spawnParticle(Particle.FLAME, dragon.getLocation(), 100, 3, 3, 3, 0.1);
        dragon.getWorld().spawnParticle(Particle.DRAGON_BREATH, dragon.getLocation(), 50, 2, 2, 2, 0.05);

        if (!dragon.hasPotionEffect(PotionEffectType.SPEED)) {
            dragon.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2));
        }

        // Efecto de daño en área cuando está enfurecida
        for (Entity nearby : dragon.getNearbyEntities(10, 10, 10)) {
            if (nearby instanceof Player player) {
                player.damage(4.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
            }
        }
    }

    @EventHandler
    public void onEndermanSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Enderman) {
            if (event.getEntity().getWorld().getEnvironment() == World.Environment.THE_END) {
                // 50% de probabilidad de creeper cargado, 50% de probabilidad de enderman normal
                if (random.nextBoolean()) {
                    Creeper creeper = (Creeper) event.getEntity().getWorld().spawnEntity(event.getEntity().getLocation(), EntityType.CREEPER);
                    creeper.setPowered(true);
                    creeper.setFuseTicks(creeper.getFuseTicks()/2);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDragonDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.getDrops().clear();
            // Mejores drops al matar a la dragona
            event.getDrops().add(new ItemStack(Material.DRAGON_EGG, 1));
            event.getDrops().add(new ItemStack(Material.ELYTRA, 1));
            event.getDrops().add(new ItemStack(Material.DRAGON_HEAD, 1));

        }
    }

    // Mantener los métodos originales de agua y elytras
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (event.getBucket() == Material.WATER_BUCKET) {
            World world = event.getPlayer().getWorld();
            if (world.getEnvironment() == World.Environment.THE_END) {
                boolean dragonAlive = world.getEntitiesByClass(EnderDragon.class).stream()
                        .anyMatch(enderDragon -> !enderDragon.isDead());
                if (dragonAlive) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent e) {
        if (!e.getChunk().getWorld().getName().equalsIgnoreCase("world_the_end")) {
            return;
        }

        // Verificar si el chunk está en el rango de la isla principal (-100 a 100 en X y Z)
        Chunk chunk = e.getChunk();
        int chunkX = chunk.getX() * 16;
        int chunkZ = chunk.getZ() * 16;

        if (chunkX > 100 || chunkX < -100 || chunkZ > 100 || chunkZ < -100) {
            return;
        }

        // Manejar item frames con elytras
        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof ItemFrame frame && frame.getItem().getType() == Material.ELYTRA) {
                ItemStack s = new ItemStack(Material.ELYTRA);
                s.setDurability((short) 431);
                frame.setItem(s);
            }
        }

        // Transformar end stone en la isla principal
        List<Block> endStoneBlocks = new ArrayList<>();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < 256; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block.getType() == Material.END_STONE) {
                        endStoneBlocks.add(block);
                    }
                }
            }
        }

        int blocksToChange = (int)(endStoneBlocks.size() * 0.25);
        Collections.shuffle(endStoneBlocks, random);
        for (int i = 0; i < blocksToChange; i++) {
            endStoneBlocks.get(i).setType(Material.END_STONE_BRICKS);
        }
    }



}
