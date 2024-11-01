package pdl.insegura.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
        // Obtener información
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        String equipo = (team != null) ? team.getPrefix() : "&cSin equipo";
        Score retoScore = player.getScoreboard().getObjective("reto").getScore(player.getName());
        boolean retoCumplido = retoScore.getScore() > 0;
        int playersOnline = Bukkit.getServer().getOnlinePlayers().size();
        int diaActual = PendulumSettings.getInstance().getDia();

        // Sonido sutil al mostrar información
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.2f);

        // Mostrar información con formato mejorado
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &6&lESTADÍSTICAS DEL SERVIDOR&r &d&l&k|&r &8&l≪"));
        player.sendMessage("");

        // Información principal
        sendStatistic(player, "Bloque de Días", "&d" + diaActual + "º &7día");
        sendStatistic(player, "Jugadores Online", "&d" + playersOnline + " &7conectados");
        sendStatistic(player, "Tu Equipo", equipo);
        sendStatistic(player, "Estado del Reto", getRetoStatus(retoCumplido));

        // Información adicional según el estado del reto
        if (retoCumplido) {
            player.sendMessage("");
            player.sendMessage(MessageUtils.colorMessage("&8└ &a¡Felicitaciones! &7Has completado el reto actual"));
        } else {
            player.sendMessage("");
            player.sendMessage(MessageUtils.colorMessage("&8└ &7Usa &d/pdl reto &7para ver los requisitos"));
        }

        // Pie de página
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &7Actualizado: &d" + getCurrentTime() + " &d&l&k|&r &8&l≪"));
        player.sendMessage("");

        // Sonido de finalización
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, 1.0f);
    }

    private void sendStatistic(Player player, String label, String value) {
        player.sendMessage(MessageUtils.colorMessage("&8└ &7" + label + ": " + value));
    }

    private String getRetoStatus(boolean completed) {
        return completed ? "&a✔ Completado" : "&c✘ Pendiente";
    }

    private String getCurrentTime() {
        java.time.LocalTime now = java.time.LocalTime.now();
        return String.format("%02d:%02d", now.getHour(), now.getMinute());
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}