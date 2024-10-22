package pdl.insegura.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

public class InfoCommand implements SubCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public void execute(Player player, String[] args) {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        String equipo = (team != null) ? team.getPrefix() : "Sin equipo";
        Score retoScore = player.getScoreboard().getObjective("reto").getScore(player.getName());
        boolean retoCumplido = retoScore.getScore() > 0;

        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        player.sendMessage(MessageUtils.colorMessage("      &l[&d&lPendulum&r&l]"));
        player.sendMessage(MessageUtils.colorMessage("      Bloque de días: &d" + PendulumSettings.getInstance().getDia()));
        player.sendMessage(MessageUtils.colorMessage("      Jugadores: &d" + Bukkit.getServer().getOnlinePlayers().size()));
        player.sendMessage(MessageUtils.colorMessage("      Equipo: " + equipo));
        player.sendMessage(MessageUtils.colorMessage("      Reto: &d" + (retoCumplido ? "Cumplido" : "No cumplido")));
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}