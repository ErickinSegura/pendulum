package pdl.insegura.commands.subcommands;

import org.bukkit.entity.Player;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

public class RetoCommand implements SubCommand {
    @Override
    public String getName() {
        return "reto";
    }

    @Override
    public void execute(Player player, String[] args) {
        boolean retoCumplido = player.getScoreboard()
                .getObjective("reto")
                .getScore(player.getName())
                .getScore() > 0;

        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        player.sendMessage(MessageUtils.colorMessage("&lReto: &d" + PendulumSettings.getInstance().getDesafio()));
        player.sendMessage(MessageUtils.colorMessage("&lRecompensa: &d" + PendulumSettings.getInstance().getPremio()));
        player.sendMessage(MessageUtils.colorMessage("&lCastigo: &d" + PendulumSettings.getInstance().getCastigo()));
        player.sendMessage(MessageUtils.colorMessage("&lEstado: &d" + (retoCumplido ? "Cumplido" : "No cumplido")));
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}