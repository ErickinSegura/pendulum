package pdl.insegura.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.listeners.mobs.customs.VoidedKnight;
import pdl.insegura.utils.MessageUtils;

public class SpawnCommand implements SubCommand {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public void execute(Player player, String[] args) {
        Location spawnLocation = player.getLocation();
        WitherSkeleton witherSkeleton = (WitherSkeleton) player.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);

        VoidedKnight voidedKnight = new VoidedKnight(PendulumPlugin.getInstance());
        voidedKnight.setupVoidedKnight(witherSkeleton);

        if (VoidedKnight.bossBars.containsKey(witherSkeleton.getUniqueId())) {
            player.sendMessage(MessageUtils.colorMessage("&aHas spawneado un Voided Knight en tu ubicación."));
        } else {
            player.sendMessage(MessageUtils.colorMessage("&cHubo un problema al spawnear el Voided Knight."));
            witherSkeleton.remove();
        }

        // Reproducir sonido para todos los jugadores
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1F, 0.5F);
        }

        // Log de depuración
        Bukkit.getLogger().info("Voided Knight spawneado en " + spawnLocation + " con UUID: " + witherSkeleton.getUniqueId());
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }
}