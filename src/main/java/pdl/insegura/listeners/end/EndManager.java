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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.Block;

import java.util.Random;

public class EndManager implements Listener {
    private final Plugin plugin;
    private final Random random = new Random();
    private int attackCounter = 0;
    private BossBar dragonBar;

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
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 200L, 200L);
    }

    @EventHandler
    public void onEntityGetHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderDragon dragon) {
            if (event.getDamager() instanceof Arrow || event.getDamager() instanceof Snowball) {
                event.setCancelled(true);
                return;
            }

            if (random.nextDouble() < 0.4) {
                dragonSpecialAttack(dragon);
            }

            if (dragon.getHealth() <= dragon.getMaxHealth() * 0.75) {
                enragedDragon(dragon);
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

        switch (random.nextInt(12)) { // Aumentado a 12 tipos de ataques
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
        }
    }

    private void enragedDragon(EnderDragon dragon) {
        // Actualizar o crear la barra del jefe
        if (dragonBar == null) {
            dragonBar = Bukkit.createBossBar(
                    ChatColor.RED + "☠ Dragón Enfurecido ☠",
                    BarColor.RED,
                    BarStyle.SEGMENTED_12
            );
        }

        // Mostrar la barra a todos los jugadores cercanos
        for (Entity nearby : dragon.getNearbyEntities(100, 100, 100)) {
            if (nearby instanceof Player) {
                dragonBar.addPlayer((Player) nearby);
            }
        }

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

        // Actualizar el progreso de la barra basado en la vida del dragón
        dragonBar.setProgress(dragon.getHealth() / dragon.getMaxHealth());
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

            if (dragonBar != null) {
                dragonBar.removeAll();
                dragonBar = null;
            }

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
        if (e.getChunk().getWorld().getName().equalsIgnoreCase("world_the_end")) {
            for (Entity entity : e.getChunk().getEntities()) {
                if (entity instanceof ItemFrame frame) {
                    if (frame.getItem().getType() == Material.ELYTRA) {
                        ItemStack s = new ItemStack(Material.ELYTRA);
                        s.setDurability((short) 431);
                        frame.setItem(s);
                    }
                }
            }

            Chunk chunk = e.getChunk();
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 0; y < 256; y++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (block.getType() == Material.END_STONE) {
                            // Probabilidades de diferentes bloques
                            double rand = random.nextDouble();
                            if (rand < 0.1) { // 10% de probabilidad
                                block.setType(Material.END_STONE_BRICKS);
                            } else if (rand < 0.15) { // 5% adicional
                                block.setType(Material.END_STONE_BRICK_STAIRS);
                                // Rotación aleatoria para las escaleras
                                org.bukkit.block.data.Directional stair = (org.bukkit.block.data.Directional) block.getBlockData();
                                stair.setFacing(org.bukkit.block.BlockFace.values()[random.nextInt(4)]);
                                block.setBlockData(stair);
                            } else if (rand < 0.17) { // 2% adicional
                                block.setType(Material.END_STONE_BRICK_WALL);
                            } else if (rand < 0.19) { // 2% adicional
                                block.setType(Material.PURPUR_BLOCK);
                            } else if (rand < 0.20) { // 1% adicional
                                block.setType(Material.PURPUR_PILLAR);
                            }
                        }
                    }
                }
            }
        }
    }
}