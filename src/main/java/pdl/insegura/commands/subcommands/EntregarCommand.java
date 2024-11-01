package pdl.insegura.commands.subcommands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Score;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

import static org.bukkit.Bukkit.getServer;

public class EntregarCommand implements SubCommand {
    @Override
    public String getName() {
        return "entregar";
    }

    @Override
    public void execute(Player player, String[] args) {
        Score retoScore = player.getScoreboard().getObjective("reto").getScore(player.getName());
        PendulumSettings settings = PendulumSettings.getInstance();

        if (retoScore.getScore() == 0 &&
                player.getInventory().contains(settings.getMaterialDesafio(), settings.getCantidadDesafio())) {

            // Efectos de éxito
            successfulDelivery(player, settings);

        } else if (retoScore.getScore() > 0) {
            // Efectos de reto ya entregado
            alreadyDelivered(player);
        } else {
            // Efectos de materiales insuficientes
            insufficientMaterials(player, settings);
        }
    }

    private void successfulDelivery(Player player, PendulumSettings settings) {
        // Remover items y dar premio
        player.getInventory().removeItem(new ItemStack(settings.getMaterialDesafio(), settings.getCantidadDesafio()));
        player.getInventory().addItem(settings.getStackPremio());

        // Efectos visuales y sonoros para el jugador
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
        Location loc = player.getLocation();

        // Efectos de partículas
        player.spawnParticle(Particle.TOTEM, loc.add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);
        player.spawnParticle(Particle.END_ROD, loc, 20, 0.3, 0.3, 0.3, 0.1);

        // Título y subtítulo
        player.sendTitle(
                MessageUtils.colorMessage("&6&l¡RETO COMPLETADO!"),
                MessageUtils.colorMessage("&e¡Has recibido tu recompensa!"),
                10, 70, 20
        );

        // Actualizar score
        player.getScoreboard().getObjective("reto").getScore(player.getName()).setScore(1);

        // Mensaje en el chat con formato mejorado
        getServer().broadcastMessage("");
        getServer().broadcastMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k||&r &6&l¡RETO COMPLETADO!&r &d&l&k||"));
        getServer().broadcastMessage(MessageUtils.colorMessage("&7El jugador &d" + player.getName() + " &7ha completado el reto"));
        getServer().broadcastMessage(MessageUtils.colorMessage("&7¡Felicitaciones por tu recompensa!"));
        getServer().broadcastMessage("");

        // Efectos para todos los jugadores
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p != player) {
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.2f);}
        }
    }

    private void alreadyDelivered(Player player) {
        // Efectos visuales y sonoros
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        player.spawnParticle(Particle.SMOKE_NORMAL, player.getLocation().add(0, 1, 0), 20, 0.3, 0.3, 0.3, 0.05);

        // Mensaje en chat
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r &c¡Ya has entregado el reto!"));
        player.sendMessage(MessageUtils.colorMessage("&7Espera al siguiente reto para participar."));
        player.sendMessage("");
    }

    private void insufficientMaterials(Player player, PendulumSettings settings) {
        // Efectos visuales y sonoros
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 0.8f);
        player.spawnParticle(Particle.BARRIER, player.getLocation().add(0, 2, 0), 1, 0, 0, 0, 0);


        // Mensaje en chat
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r &c¡Materiales insuficientes!"));
        player.sendMessage(MessageUtils.colorMessage("&7Necesitas: &e" + settings.getCantidadDesafio() +
                " &7de &e" + settings.getMaterialDesafio().toString().toLowerCase()));
        player.sendMessage("");
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}