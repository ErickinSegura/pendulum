package pdl.insegura.commands.subcommands;

import org.bukkit.entity.Player;
import pdl.insegura.utils.MessageUtils;

public class HelpCommand implements SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        player.sendMessage(MessageUtils.colorMessage("&lComandos de &d&lPendulum&r&l:\n"));
        player.sendMessage(MessageUtils.colorMessage("&l> reto &d- &rMuestra el reto actual."));
        player.sendMessage(MessageUtils.colorMessage("&l> entregar &d- &rEntrega el reto actual."));
        player.sendMessage(MessageUtils.colorMessage("&l> check &d- &rMuestra tu tiempo restante."));
        player.sendMessage(MessageUtils.colorMessage("&l> time &d- &rMuestra datos del reinicio de tiempo."));
        player.sendMessage(MessageUtils.colorMessage("&l> help &d- &rMuestra este mensaje."));

        if (player.hasPermission("pendulum.admin")) {
            player.sendMessage(MessageUtils.colorMessage("\n&lComandos de Administrador:"));
            player.sendMessage(MessageUtils.colorMessage("&l> give &d- &rOtorga items especiales."));
            player.sendMessage(MessageUtils.colorMessage("&l> ruleta &d- &rSelecciona un reto aleatorio."));
            player.sendMessage(MessageUtils.colorMessage("&l> spawn &d- &rInvoca un Voided Knight."));
            player.sendMessage(MessageUtils.colorMessage("&l> reset &d- &rReinicia contadores."));
        }

        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}