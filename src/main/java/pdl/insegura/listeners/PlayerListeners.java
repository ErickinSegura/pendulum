package pdl.insegura.listeners;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import java.util.HashSet;
import java.util.Set;

public class PlayerListeners implements Listener {
    private final int dia = PendulumSettings.getInstance().getDia();
    private final String[] castigosDia0 = PendulumSettings.getInstance().getCastigosDia0();


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        // Check if player died by falling into void (like in End)
        if (player.getLastDamageCause().getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID) {
            // Set location to the lowest block possible
            location.setY(location.getWorld().getMinHeight() + 1);
        } else {
            // Otherwise, set location to player's death location
            location = player.getLocation();
        }

        // Play death sound to all players
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), "minecraft:muerte", 1, 1);
        }

        if (PendulumSettings.getInstance().getDia() >= 10) {
            if (player.getScoreboard().getEntryTeam(player.getName()) != null) {
                player.getScoreboard().getEntryTeam(player.getName()).getEntries().forEach(entry -> {
                    Player teamMember = Bukkit.getPlayer(entry);
                    if (teamMember != null && teamMember.isOnline()) {
                        teamMember.sendMessage(MessageUtils.colorMessage("&d&lPor la muerte de alguien de tu equipo perdiste todos tus efectos de poción..."));
                        for (PotionEffect effect : teamMember.getActivePotionEffects()) {
                            teamMember.removePotionEffect(effect.getType());
                        }
                    }
                });
            }
        }

        // Broadcast death message
        Bukkit.getServer().broadcastMessage(MessageUtils.colorMessage("&dA &5&l" + player.getName() + "&r&d se le ha acabado el tiempo..."));
        if (player.getKiller() == null) {
            Bukkit.getServer().broadcastMessage(MessageUtils.colorMessage("Coordenadas de Muerte: &l" + location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ()));
        }

        // Mensaje Custom de muerte
        Bukkit.getServer().broadcastMessage(MessageUtils.colorMessage(DeathMessages.getInstance().getDeathMessage(player.getName())));

        // Place head
        Block fence = location.getBlock();
        fence.setType(Material.END_ROD);

        location.add(0, 1, 0);
        Block head = location.getBlock();
        head.setType(Material.PLAYER_HEAD);
        Skull skullBlock = (Skull) head.getState();
        skullBlock.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        skullBlock.update(true);

        // Place block under head
        location.add(0, -2, 0);
        Block base = location.getBlock();
        int bloque = (int) Math.floor(Math.random() * 10 + 1);

        if (bloque >= 1 && bloque < 5) {
            base.setType(Material.GOLD_BLOCK);
        } else if (bloque >= 5 && bloque < 8) {
            base.setType(Material.EMERALD_BLOCK);
        } else if (bloque >= 8 && bloque < 10) {
            base.setType(Material.DIAMOND_BLOCK);
        } else if (bloque == 10) {
            base.setType(Material.NETHERITE_BLOCK);
        }

        // Display death message on screen
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title @a times 20 40 20");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "title @a title [\"\",{\"text\":\"-\",\"obfuscated\":true},{\"text\":\" \\u231a \"},{\"text\":\"Muerto \",\"bold\":true,\"color\":\"light_purple\"},{\"text\":\"\\u231a \"},{\"text\":\"-\",\"obfuscated\":true}]");
    }

    private final Set<Player> sleepingPlayers = new HashSet<>();

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        long time = player.getWorld().getTime();

        int neededPlayers = calculateNeededPlayers();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();

        if (onlinePlayers < neededPlayers) {
            player.sendMessage(MessageUtils.colorMessage("&cNo puedes dormir porque no hay suficientes personas en línea (" + neededPlayers + ")."));
            event.setCancelled(true);
            return;
        }

        if (time < 13000) {
            player.sendMessage(MessageUtils.colorMessage("&cSolo puedes dormir de noche."));
            event.setCancelled(true);
            return;
        }

        sleepingPlayers.add(player);
        Bukkit.broadcastMessage(MessageUtils.colorMessage("&e" + player.getName() + " se fue a dormir"));
        checkAndPassNight(player);
    }

    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        sleepingPlayers.remove(event.getPlayer());
        //Bukkit.broadcastMessage(MessageUtils.colorMessage("&e" + event.getPlayer().getName() + " se levanto de la cama"));
    }

    private int calculateNeededPlayers() {
        if (dia >= 10) {
            return PendulumSettings.getInstance().getJugadoresNoche(); // Ejemplo de ajuste basado en el día
        } else {
            return 1;
        }
    }

    private void checkAndPassNight(Player player) {
        if (sleepingPlayers.size() >= calculateNeededPlayers()) {
            Bukkit.getServer().getScheduler().runTaskLater(PendulumPlugin.getInstance(), () -> {
                player.getWorld().setTime(0L);
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                Bukkit.getServer().broadcastMessage(MessageUtils.colorMessage("&d&lLa noche ha pasado"));
                // Limpiar la lista de jugadores durmiendo
                sleepingPlayers.clear();
            }, 60L); // 3 segundos después de que un jugador se duerma
        }
    }

    @EventHandler
    public void onUseTotem(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!event.isCancelled()) {
                Bukkit.getServer().broadcastMessage(MessageUtils.colorMessage("&d&l" + event.getEntity().getName() + "&r&d ha usado un tótem de la inmortalidad!"));
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        for (String castigo : castigosDia0) {
            if (player.getName().equals(castigo)) {
                player.setHealthScale(player.getHealthScale() - 4.0);
            }
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if (event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasDisplayName()) {
            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(MessageUtils.colorMessage("&c&lDirty Hearthy"))) {
                PersistentDataContainer data = event.getPlayer().getPersistentDataContainer();
                NamespacedKey key = new NamespacedKey(PendulumPlugin.getInstance(), "DirtyCount");

                int count = data.getOrDefault(key, PersistentDataType.INTEGER, 0);

                if (count >= 5) {
                    event.getPlayer().sendMessage(MessageUtils.colorMessage("&cYa has consumido 5 Dirty Hearthy..."));
                    event.getPlayer().sendMessage(MessageUtils.colorMessage("&cDesperdiciando una cabeza, eh?"));
                    return;
                }

                count++;
                data.set(key, PersistentDataType.INTEGER, count);
                event.getPlayer().sendMessage(MessageUtils.colorMessage("&cHas consumido " + count + "/5 Dirty Hearthy"));

                double maxHealth = event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                event.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth + 2.0); // Aumenta 1 corazón (2 puntos de vida)

                if (count == 5) {
                    event.getPlayer().sendMessage(MessageUtils.colorMessage("&a¡Has alcanzado el máximo de 5 Dirty Hearthy!"));
                }
            }

            if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(MessageUtils.colorMessage("&d&lVoided Apple"))) {
                PotionEffect effect = new PotionEffect(PotionEffectType.HEALTH_BOOST, 12000, 1, true, false);
                event.getPlayer().addPotionEffect(effect, true);
                event.getPlayer().sendMessage(MessageUtils.colorMessage("&dVida Boosteada por 10 minutos."));
            }
        }
    }

    // No poermitir que los jugadores entren al end
    @EventHandler
    public void onPlayerEnterEnd(PlayerPortalEvent event) {
        if (dia < 10) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(MessageUtils.colorMessage("&cNo puedes entrar al End aún."));

                // Teletransportar al jugador fuera del portal
                Location teleportLocation = event.getPlayer().getLocation().add(3, 1.5, 0); // Ajusta las coordenadas según sea necesario
                event.getPlayer().teleport(teleportLocation);

                // Reproducir sonido de teletransporte
                event.getPlayer().playSound(teleportLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            }
        }
    }
}



