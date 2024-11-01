package pdl.insegura.commands.subcommands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

public class RetoCommand implements SubCommand {
    @Override
    public String getName() {
        return "reto";
    }

    @Override
    public void execute(Player player, String[] args) {
        // Obtener información del reto
        PendulumSettings settings = PendulumSettings.getInstance();
        boolean retoCumplido = player.getScoreboard()
                .getObjective("reto")
                .getScore(player.getName())
                .getScore() > 0;

        // Sonido inicial
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.2f);

        // Cabecera
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &6&lDETALLES DEL RETO&r &d&l&k|&r &8&l≪"));
        player.sendMessage("");

        // Información principal con iconos
        sendRetoDetail(player, "⚔", "Desafío", settings.getDesafio());
        sendRetoDetail(player, "✨", "Recompensa", settings.getPremio());
        sendRetoDetail(player, "☠", "Castigo", settings.getCastigo());

        // Estado con color y estilo según cumplimiento
        player.sendMessage("");
        if (retoCumplido) {
            player.sendMessage(MessageUtils.colorMessage("&8└ &7Estado: &a✔ Completado"));
            player.sendMessage(MessageUtils.colorMessage("&8   &7¡Felicitaciones por completar el reto!"));
        } else {
            player.sendMessage(MessageUtils.colorMessage("&8└ &7Estado: &c✘ Pendiente"));
            player.sendMessage(MessageUtils.colorMessage("&8   &7Usa &d/pdl entregar &7cuando tengas los materiales"));
        }

        // Materiales necesarios (solo si no está cumplido)
        if (!retoCumplido) {
            player.sendMessage("");
            player.sendMessage(MessageUtils.colorMessage("&8└ &7Necesitas:"));
            player.sendMessage(MessageUtils.colorMessage("&8   &d" + settings.getCantidadDesafio() + " &7de &d" +
                    settings.getMaterialDesafio().toString().toLowerCase().replace("_", " ")));
        }

        // Pie de página
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &7Actualizado: &d" + getCurrentTime() + " &d&l&k|&r &8&l≪"));
        player.sendMessage("");

        // Sonido final dependiendo del estado
        float pitch = retoCumplido ? 1.5f : 1.0f;
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.5f, pitch);
    }

    private void sendRetoDetail(Player player, String icon, String label, String value) {
        player.sendMessage(MessageUtils.colorMessage("&8└ " + icon + " &7" + label + ": &d" + value));
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