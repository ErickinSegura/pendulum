package pdl.insegura.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pdl.insegura.utils.PendulumSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandCompletion implements TabCompleter {
    public static String[] OP = PendulumSettings.getInstance().getOp();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("help");
            completions.add("reto");
            completions.add("entregar");
            completions.add("check");
            completions.add("time");

            if (checkPermision((Player) sender)) {
                completions.add("give");
                completions.add("ruleta");
                completions.add("spawn");

            }
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("give")) {
                completions.add("nether_armors");
                completions.add("voided_armor");
                completions.add("voided_items");
                completions.add("pendu_items");
            }
        }


        return completions;
    }

    private boolean checkPermision(Player player) {
        for (String s : OP) {
            if (Objects.equals(s, player.getName())) {
                return true;
            }
        }
        return false;
    }

}
