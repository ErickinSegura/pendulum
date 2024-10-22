package pdl.insegura.commands.subcommands;

import org.bukkit.entity.Player;

public class TimeCommand implements SubCommand {
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void execute(Player player, String[] args) {
        player.performCommand("ptl info");
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}