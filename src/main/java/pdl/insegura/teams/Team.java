package pdl.insegura.teams;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private String name;
    private Set<Player> players;

    public Team(String name) {
        this.name = name;
        this.players = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    public boolean containsPlayer(Player player) {
        return players.contains(player);
    }

    public Set<Player> getPlayers() {
        return players;
    }
}
