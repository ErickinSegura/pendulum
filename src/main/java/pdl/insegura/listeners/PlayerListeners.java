package pdl.insegura.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
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
        World overworld = getWorld("world");
        World nether = getWorld("world_nether");
        World end = getWorld("world_the_end");


        //environment

        overworld.playSound(location, "minecraft:muerte", 1, 1);
        nether.playSound(location, "minecraft:muerte", 1, 1);
        end.playSound(location, "minecraft:muerte", 1, 1);


        //chat
        getServer().broadcastMessage(MessageUtils.colorMessage("&dA &5&l"+player.getName()+"&r&d se le ha acabado el tiempo..."));
        if (player.getKiller() == null) {
            getServer().broadcastMessage(MessageUtils.colorMessage("Coordenadas de Muerte: &l"+location.getBlockX()+"/"+location.getBlockY()+"/"+location.getBlockZ()));
        }

        //head
        Block fence = location.getBlock();
        fence.setType(Material.NETHER_BRICK_FENCE);

        location.add(0, 1, 0);
        Block head = location.getBlock();
        head.setType(Material.PLAYER_HEAD);
        Skull skullBlock = (Skull) head.getState();
        skullBlock.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        skullBlock.update(true);

        location.add(0, -2, 0);
        Block base = location.getBlock();
        int bloque = (int)Math.floor(Math.random()*10+1);

        if (bloque >= 1 && bloque < 5) {
            base.setType(Material.GOLD_BLOCK);
        } else if (bloque >= 5 && bloque < 8) {
            base.setType(Material.EMERALD_BLOCK);
        } else if (bloque >= 8 && bloque < 10) {
            base.setType(Material.DIAMOND_BLOCK);
        } else if (bloque == 10) {
            base.setType(Material.NETHERITE_BLOCK);
        }


        //screen

        getServer().dispatchCommand(getConsoleSender(), "title @a times 20 40 20");
        getServer().dispatchCommand(getConsoleSender(), "title @a title [\"\",{\"text\":\"-\",\"obfuscated\":true},{\"text\":\" \\u231a \"},{\"text\":\"Muerto \",\"bold\":true,\"color\":\"light_purple\"},{\"text\":\"\\u231a \"},{\"text\":\"-\",\"obfuscated\":true}]");



    }

    @EventHandler
    public void OnUseTotem(EntityResurrectEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if (!event.isCancelled()){
            getServer().broadcastMessage(MessageUtils.colorMessage("&d&l"+entity.getName()+"&r&d ha usado un tótem de la inmortalidad!"));
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

