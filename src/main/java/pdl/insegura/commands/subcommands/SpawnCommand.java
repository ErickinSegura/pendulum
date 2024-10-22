package pdl.insegura.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.listeners.mobs.customs.VoidedKnight;
import pdl.insegura.listeners.mobs.customs.InfernalGuardian;
import pdl.insegura.listeners.mobs.customs.ExplosivePenguinVillager;
import pdl.insegura.utils.MessageUtils;

import java.util.UUID;

public class SpawnCommand implements SubCommand {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(MessageUtils.colorMessage("&cUso: /pendulum spawn <tipo>"));
            player.sendMessage(MessageUtils.colorMessage("&cTipos disponibles: knight, guardian, penguin"));
            return;
        }

        String mobType = args[1].toLowerCase();
        Location spawnLocation = player.getLocation();

        switch (mobType) {
            case "knight" -> spawnVoidedKnight(player, spawnLocation);
            case "guardian" -> spawnInfernalGuardian(player, spawnLocation);
            case "penguin" -> spawnPenguinVillager(player, spawnLocation);
            default -> player.sendMessage(MessageUtils.colorMessage("&cTipo de mob no válido. Usa: knight, guardian, o penguin"));
        }
    }

    private void spawnVoidedKnight(Player player, Location location) {
        WitherSkeleton witherSkeleton = (WitherSkeleton) player.getWorld().spawnEntity(location, EntityType.WITHER_SKELETON);
        VoidedKnight voidedKnight = new VoidedKnight(PendulumPlugin.getInstance());
        voidedKnight.setupVoidedKnight(witherSkeleton);

        if (VoidedKnight.bossBars.containsKey(witherSkeleton.getUniqueId())) {
            player.sendMessage(MessageUtils.colorMessage("&aHas spawneado un Voided Knight en tu ubicación."));
            playSpawnSound(Sound.ENTITY_WITHER_SPAWN, 1F, 0.5F);
            logSpawn("Voided Knight", location, witherSkeleton.getUniqueId());
        } else {
            player.sendMessage(MessageUtils.colorMessage("&cHubo un problema al spawnear el Voided Knight."));
            witherSkeleton.remove();
        }
    }

    private void spawnInfernalGuardian(Player player, Location location) {
        Strider strider = player.getWorld().spawn(location, Strider.class);
        InfernalGuardian.spawnInfernalGuardian(strider);
        player.sendMessage(MessageUtils.colorMessage("&aHas spawneado un Infernal Guardian en tu ubicación."));
        playSpawnSound(Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1F, 0.5F);
        logSpawn("Infernal Guardian", location, strider.getUniqueId());
    }

    private void spawnPenguinVillager(Player player, Location location) {
        ExplosivePenguinVillager.trySpawnPenguinVillager(location);
        player.sendMessage(MessageUtils.colorMessage("&aHas spawneado un Aldeano Pingüino en tu ubicación."));
        playSpawnSound(Sound.ENTITY_VILLAGER_AMBIENT, 1F, 1F);
        logSpawn("Aldeano Pingüino", location, null);
    }

    private void playSpawnSound(Sound sound, float volume, float pitch) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), sound, volume, pitch);
        }
    }

    private void logSpawn(String mobType, Location location, UUID entityId) {
        String logMessage = mobType + " spawneado en " + location;
        if (entityId != null) {
            logMessage += " con UUID: " + entityId;
        }
        Bukkit.getLogger().info(logMessage);
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }
}