package pdl.insegura.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.QoLUtils;

import static org.bukkit.Bukkit.getServer;

public class PlayerListeners implements Listener {
    QoLUtils qol = new QoLUtils();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        //environment
        qol.comando("playsound minecraft:muerte master @a");

        //chat
        getServer().broadcastMessage(event.getDeathMessage());

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
        base.setType(Material.BEDROCK);

        //screen






    }
}
