package pdl.insegura.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pdl.insegura.utils.PendulumSettings;

import java.util.*;

public class CommandCompletion implements TabCompleter {
    private final Map<String, List<String>> subCommandCompletions;

    public CommandCompletion() {
        this.subCommandCompletions = new HashMap<>();
        initializeCompletions();
    }

    private void initializeCompletions() {
        // Comandos básicos disponibles para todos
        List<String> basicCommands = Arrays.asList(
                "help", "reto", "entregar", "check", "time"
        );

        // Comandos admin
        List<String> adminCommands = Arrays.asList(
                "give", "ruleta", "spawn", "reset"
        );

        // Subcomandos de give
        List<String> giveOptions = Arrays.asList(
                "agile_netherite", "reinforced_netherite", "voided_armor",
                "assault_armor", "guardian_armor", "voided_tools",
                "voided_ingot", "voidad_shard", "dirty_hearty", "oro_doble"
        );

        // Subcomandos de spawn
        List<String> spawnOptions = Arrays.asList(
                "knight", "guardian", "penguin"
        );

        subCommandCompletions.put("basic", basicCommands);
        subCommandCompletions.put("admin", adminCommands);
        subCommandCompletions.put("give", giveOptions);
        subCommandCompletions.put("spawn", spawnOptions);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command,
                                      String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>(subCommandCompletions.get("basic"));
            if (checkPermission(player)) {
                completions.addAll(subCommandCompletions.get("admin"));
            }
            return filterCompletions(completions, args[0]);
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("give") && checkPermission(player)) {
                return filterCompletions(subCommandCompletions.get("give"), args[1]);
            } else if (args[0].equalsIgnoreCase("spawn") && checkPermission(player)) {
                return filterCompletions(subCommandCompletions.get("spawn"), args[1]);
            }
        }

        return Collections.emptyList();
    }

    private List<String> filterCompletions(List<String> completions, String partial) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(partial.toLowerCase()))
                .sorted()
                .toList();
    }

    private boolean checkPermission(Player player) {
        return Arrays.asList(PendulumSettings.getInstance().getOp())
                .contains(player.getName());
    }
}