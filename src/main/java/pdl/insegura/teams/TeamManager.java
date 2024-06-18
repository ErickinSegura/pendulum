package pdl.insegura.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeamManager {

    public boolean createTeam(String teamName) {
        // Crea el equipo utilizando el comando /team
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team add " + teamName);
    }

    public boolean addPlayerToTeam(Player player, String teamName) {
        // Añade el jugador al equipo utilizando el comando /team
        return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "team join " + teamName + " " + player.getName());
    }

    public String getTeam(Player player) {
        // Obtén el equipo del jugador utilizando el comando /team
        for (org.bukkit.scoreboard.Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            if (team.hasEntry(player.getName())) {
                return team.getName();
            }
        }
        return null;
    }
}
