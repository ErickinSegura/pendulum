package pdl.insegura.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pdl.insegura.commands.subcommands.*;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

import java.util.HashMap;
import java.util.Map;

public class PendulumCommand implements CommandExecutor {
    private final Map<String, SubCommand> subCommands;

    public PendulumCommand() {
        this.subCommands = new HashMap<>();
        registerSubCommands();
    }

    private void registerSubCommands() {
        addSubCommand(new InfoCommand());
        addSubCommand(new RetoCommand());
        addSubCommand(new EntregarCommand());
        addSubCommand(new CheckCommand());
        addSubCommand(new TimeCommand());
        addSubCommand(new HelpCommand());
        addSubCommand(new ResetCommand());
        addSubCommand(new RuletaCommand());
        addSubCommand(new GiveCommand());
        addSubCommand(new SpawnCommand());
    }

    private void addSubCommand(SubCommand command) {
        subCommands.put(command.getName().toLowerCase(), command);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.colorMessage("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        if (args.length == 0) {
            subCommands.get("info").execute(player, args);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            player.sendMessage(MessageUtils.colorMessage("&cComando no reconocido. Usa /pendulum help para ver los comandos disponibles."));
            return true;
        }

        if (subCommand.requiresPermission() && !checkPermission(player)) {
            player.sendMessage(MessageUtils.colorMessage("&cNo tienes permisos para ejecutar este comando."));
            return true;
        }

        subCommand.execute(player, args);
        return true;
    }

    private boolean checkPermission(Player player) {
        return player.hasPermission("pendulum.admin") ||
                java.util.Arrays.asList(PendulumSettings.getInstance().getOp())
                        .contains(player.getName());
    }
}