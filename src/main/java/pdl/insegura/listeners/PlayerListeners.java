package pdl.insegura.listeners;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.QoLUtils;

import static org.bukkit.Bukkit.*;

public class PlayerListeners implements Listener {
    QoLUtils qol = new QoLUtils();
    public PendulumPlugin plugin = PendulumPlugin.getPlugin(PendulumPlugin.class);


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "execute run playsound minecraft:muerte master @a");


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


    // Interacts
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(MessageUtils.colorMessage("&c&lDirty Hearty")) && player.getScoreboard().getObjective("dirty").getScore(player.getName()).getScore() < 4) {
                player.getScoreboard().getObjective("dirty").getScore(player.getName()).setScore(player.getScoreboard().getObjective("dirty").getScore(player.getName()).getScore() + 1);
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 1);
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + 2);
                player.getInventory().remove(item);
                player.sendMessage(MessageUtils.colorMessage("&d&lHaz consumido un Dirty Hearty"));
            }


            }
    }






    @EventHandler

    public void onPlayerLogin(PlayerLoginEvent event) {

    }








}

