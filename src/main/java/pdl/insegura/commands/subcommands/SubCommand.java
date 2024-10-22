package pdl.insegura.commands.subcommands;

import org.bukkit.entity.Player;

public interface SubCommand {
    String getName();
    void execute(Player player, String[] args);
    boolean requiresPermission();
}
