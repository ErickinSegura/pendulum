package pdl.insegura.listeners;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.DeathMessages;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import static org.bukkit.Bukkit.getServer;

public class PlayerListeners implements Listener {
    private static final String DIRTY_HEARTHY_NAME = "&c&lDirty Hearthy";
    private static final String VOIDED_APPLE_NAME = "&d&lVoided Apple";
    private static final int DIRTY_HEARTHY_MAX = 5;
    private static final int NIGHT_START = 13000;
    private static final long SLEEP_DELAY = 60L;
    private static final int ENDER_PEARL_COOLDOWN = 120;
    private static final double WITHER_SPAWN_CHANCE = 0.1;
    private static final double CHEST_CHANCE = 0.25;

    private final Set<Player> sleepingPlayers = Collections.newSetFromMap(new WeakHashMap<>());
    private final PendulumSettings settings = PendulumSettings.getInstance();
    private final NamespacedKey dirtyHearthyKey;

    public PlayerListeners(PendulumPlugin plugin) {
        this.dirtyHearthyKey = new NamespacedKey(plugin, "DirtyCount");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = getDeathLocation(player);

        broadcastDeathSound();
        handleTeamEffects(player);
        broadcastDeathMessages(player, location);
        createDeathMemorial(location, player);
        displayDeathTitle();
    }

    private Location getDeathLocation(Player player) {
        Location location = player.getLocation();
        if (player.getLastDamageCause().getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID) {
            location.setY(location.getWorld().getMinHeight() + 1);
        }
        return location;
    }

    private void broadcastDeathSound() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), "minecraft:muerte", 1, 1);
        }
    }

    private void handleTeamEffects(Player player) {
        if (settings.getDia() >= 10 && player.getScoreboard().getEntryTeam(player.getName()) != null) {
            player.getScoreboard().getEntryTeam(player.getName()).getEntries().forEach(entry -> {
                Player teamMember = Bukkit.getPlayer(entry);
                if (teamMember != null && teamMember.isOnline()) {
                    teamMember.sendMessage(MessageUtils.colorMessage("&d&lPor la muerte de alguien de tu equipo perdiste todos tus efectos de poción..."));
                    teamMember.getActivePotionEffects().forEach(effect -> teamMember.removePotionEffect(effect.getType()));
                }
            });
        }
    }

    private void broadcastDeathMessages(Player player, Location location) {
        if (player != null) {
            // First message - player death announcement
            String playerName = player.getName();
            getServer().broadcastMessage(MessageUtils.colorMessage("&dA &5&l" + playerName + "&r&d se le ha acabado el tiempo..."));

            // Second message - death coordinates (only if no killer)
            if (player.getKiller() == null && location != null) {
                String coordMessage = String.format("Coordenadas de Muerte: &l%d/%d/%d",
                        location.getBlockX(), location.getBlockY(), location.getBlockZ());
                getServer().broadcastMessage(MessageUtils.colorMessage(coordMessage));
            }

            // Third message - custom death message
            String deathMessage = null;
            try {
                deathMessage = DeathMessages.getInstance().getDeathMessage(playerName);
                if (deathMessage != null) {
                    getServer().broadcastMessage(MessageUtils.colorMessage(deathMessage));
                }
            } catch (NullPointerException e) {
                getServer().broadcastMessage(MessageUtils.colorMessage("&7Fue consumido por el vacío del tiempo"));
            }
        }
    }

    private void createDeathMemorial(Location location, Player player) {
        // Place end rod
        location.getBlock().setType(Material.END_ROD);

        // Place player head
        location.add(0, 1, 0);
        Block headBlock = location.getBlock();
        headBlock.setType(Material.PLAYER_HEAD);
        Skull skull = (Skull) headBlock.getState();
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        skull.update(true);

        // Place base block
        location.add(0, -2, 0);
        setRandomBaseBlock(location.getBlock());
    }

    private void setRandomBaseBlock(Block block) {
        double random = Math.random();
        if (random < 0.4) {
            block.setType(Material.GOLD_BLOCK);
        } else if (random < 0.7) {
            block.setType(Material.EMERALD_BLOCK);
        } else if (random < 0.9) {
            block.setType(Material.DIAMOND_BLOCK);
        } else {
            block.setType(Material.NETHERITE_BLOCK);
        }
    }

    private void displayDeathTitle() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title @a times 20 40 20");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title @a title [\"\",{\"text\":\"-\",\"obfuscated\":true},{\"text\":\" \\u231a \"},{\"text\":\"Muerto \",\"bold\":true,\"color\":\"light_purple\"},{\"text\":\"\\u231a \"},{\"text\":\"-\",\"obfuscated\":true}]");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (!canPlayerSleep(player)) {
            event.setCancelled(true);
            return;
        }

        sleepingPlayers.add(player);
        getServer().broadcastMessage(MessageUtils.colorMessage("&e" + player.getName() + " se fue a dormir"));
        checkAndPassNight(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerEnterEnd(PlayerPortalEvent event) {
        if (settings.getDia() < 10 && event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage(MessageUtils.colorMessage("&cNo puedes entrar al End aún."));

            Location safeLocation = player.getLocation().add(3, 1.5, 0);
            player.teleport(safeLocation);
            player.playSound(safeLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
    }

    private boolean canPlayerSleep(Player player) {
        int neededPlayers = settings.getDia() > 10 ? settings.getJugadoresNoche() : 1;
        if (Bukkit.getOnlinePlayers().size() < neededPlayers) {
            player.sendMessage(MessageUtils.colorMessage("&cNo puedes dormir porque no hay suficientes personas en línea (" + neededPlayers + ")."));
            return false;
        }

        if (player.getWorld().getTime() < NIGHT_START) {
            player.sendMessage(MessageUtils.colorMessage("&cSolo puedes dormir de noche."));
            return false;
        }
        return true;
    }

    private void checkAndPassNight(Player player) {
        int neededPlayers = settings.getDia() > 10 ? settings.getJugadoresNoche() : 1;
        if (sleepingPlayers.size() >= neededPlayers) {
            Bukkit.getScheduler().runTaskLater(PendulumPlugin.getInstance(), () -> {
                player.getWorld().setTime(0L);
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                getServer().broadcastMessage(MessageUtils.colorMessage("&d&lLa noche ha pasado"));
                sleepingPlayers.clear();
            }, SLEEP_DELAY);
        }
    }

    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        sleepingPlayers.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onConsume(PlayerItemConsumeEvent event) {
        if (!event.getItem().hasItemMeta() || !event.getItem().getItemMeta().hasDisplayName()) {
            return;
        }

        String itemName = event.getItem().getItemMeta().getDisplayName();
        if (itemName.equalsIgnoreCase(MessageUtils.colorMessage(DIRTY_HEARTHY_NAME))) {
            handleDirtyHearthyConsumption(event.getPlayer());
        } else if (itemName.equalsIgnoreCase(MessageUtils.colorMessage(VOIDED_APPLE_NAME))) {
            handleVoidedAppleConsumption(event.getPlayer());
        }
    }

    private void handleDirtyHearthyConsumption(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        int count = data.getOrDefault(dirtyHearthyKey, PersistentDataType.INTEGER, 0);

        if (count >= DIRTY_HEARTHY_MAX) {
            player.sendMessage(MessageUtils.colorMessage("&cYa has consumido 5 Dirty Hearthy..."));
            player.sendMessage(MessageUtils.colorMessage("&cDesperdiciando una cabeza, eh?"));
            return;
        }

        count++;
        data.set(dirtyHearthyKey, PersistentDataType.INTEGER, count);
        player.sendMessage(MessageUtils.colorMessage("&cHas consumido " + count + "/5 Dirty Hearthy"));

        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth + 2.0);

        if (count == DIRTY_HEARTHY_MAX) {
            player.sendMessage(MessageUtils.colorMessage("&a¡Has alcanzado el máximo de 5 Dirty Hearthy!"));
        }
    }

    private void handleVoidedAppleConsumption(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 12000, 1, true, false));
        player.sendMessage(MessageUtils.colorMessage("&dVida Boosteada por 10 minutos."));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event) {
        if (settings.getDia() >= 15 && event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.getPlayer().setCooldown(Material.ENDER_PEARL, ENDER_PEARL_COOLDOWN);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEggThrow(PlayerEggThrowEvent event) {
        if (settings.getDia() >= 15 && event.getNumHatches() > 0) {
            event.setHatching(false);
            if (Math.random() < WITHER_SPAWN_CHANCE) {
                spawnWitherFromEgg(event);
            }
        }
    }

    private void spawnWitherFromEgg(PlayerEggThrowEvent event) {
        Wither wither = (Wither) event.getEgg().getLocation().getWorld().spawnEntity(event.getEgg().getLocation(), EntityType.WITHER);
        wither.setCustomName("Pollito Bebé de " + event.getPlayer().getName());

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onUseTotem(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player && !event.isCancelled()) {
            getServer().broadcastMessage(MessageUtils.colorMessage("&d&l" + event.getEntity().getName() + "&r&d ha usado un tótem de la inmortalidad!"));
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        for (String castigo : settings.getCastigosDia0()) {
            if (player.getName().equals(castigo)) {
                player.setHealthScale(player.getHealthScale() - 4.0);
                break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestOpen(PlayerInteractEvent event) {
        if (settings.getDia() >= 15 ){
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
                return;
            }
            Material blockType = event.getClickedBlock().getType();
            if (blockType != Material.CHEST && blockType != Material.TRAPPED_CHEST) {
                return;
            }

            if (Math.random() < CHEST_CHANCE) {
                Location chestLocation = event.getClickedBlock().getLocation();
                event.getPlayer().getWorld().playSound(chestLocation, Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);
            }
        }
    }
}