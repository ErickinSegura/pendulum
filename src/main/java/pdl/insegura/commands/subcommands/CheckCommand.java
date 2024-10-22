package pdl.insegura.commands.subcommands;

import org.bukkit.entity.Player;

public class CheckCommand implements SubCommand {
    @Override
    public String getName() {
        return "check";
    }

    @Override
    public void execute(Player player, String[] args) {
        player.performCommand("ptl check");
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}