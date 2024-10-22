package pdl.insegura.commands.subcommands;

import org.bukkit.Sound;
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

            player.getInventory().removeItem(new ItemStack(settings.getMaterialDesafio(), settings.getCantidadDesafio()));
            player.getInventory().addItem(settings.getStackPremio());
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            retoScore.setScore(1);

            getServer().broadcastMessage(MessageUtils.colorMessage(
                    "&l[&d&lPendulum&r&l] &d" + player.getName() + "&r ha cumplido el reto!"));
        } else if (retoScore.getScore() > 0) {
            player.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r Ya has entregado el reto!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        } else {
            player.sendMessage(MessageUtils.colorMessage(
                    "&l[&d&lPendulum&r&l]&r No tienes los materiales necesarios para entregar el reto!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}