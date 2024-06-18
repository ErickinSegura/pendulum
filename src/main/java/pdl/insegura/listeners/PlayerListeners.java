package pdl.insegura.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.QoLUtils;

import static org.bukkit.Bukkit.*;

public class PlayerListeners implements Listener {
    QoLUtils qol = new QoLUtils();
    private String[] jugadoresConVida18 = {""};


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        // Check if player died by falling into void (like in End)
        if (player.getLastDamageCause().getCause() == org.bukkit.event.entity.EntityDamageEvent.DamageCause.VOID) {
            // Set location to the lowest block possible
            location.setY(location.getWorld().getMinHeight()+1);
        } else {
            // Otherwise, set location to player's death location
            location = player.getLocation();
        }

        // Play death sound to all players
        getServer().dispatchCommand(getServer().getConsoleSender(), "playsound minecraft:muerte master @a " + location.getX() + " " + location.getY() + " " + location.getZ());

        // Broadcast death message
        getServer().broadcastMessage(MessageUtils.colorMessage("&dA &5&l" + player.getName() + "&r&d se le ha acabado el tiempo..."));
        if (player.getKiller() == null) {
            getServer().broadcastMessage(MessageUtils.colorMessage("Coordenadas de Muerte: &l" + location.getBlockX() + "/" + location.getBlockY() + "/" + location.getBlockZ()));
        }

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
        getServer().dispatchCommand(getConsoleSender(), "title @a times 20 40 20");
        getServer().dispatchCommand(getConsoleSender(), "title @a title [\"\",{\"text\":\"-\",\"obfuscated\":true},{\"text\":\" \\u231a \"},{\"text\":\"Muerto \",\"bold\":true,\"color\":\"light_purple\"},{\"text\":\"\\u231a \"},{\"text\":\"-\",\"obfuscated\":true}]");
    }

    @EventHandler
    public void onUseTotem(EntityResurrectEvent event) {
        if (event.getEntity() instanceof Player) {
            if (!event.isCancelled()) {
                getServer().broadcastMessage(MessageUtils.colorMessage("&d&l" + event.getEntity().getName() + "&r&d ha usado un tótem de la inmortalidad!"));
            }
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (containsIgnoreCase(jugadoresConVida18, player.getName())) {
            player.setHealthScale(18);
        }
    }

    private boolean containsIgnoreCase(String[] array, String target) {
        for (String s : array) {
            if (s.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}
