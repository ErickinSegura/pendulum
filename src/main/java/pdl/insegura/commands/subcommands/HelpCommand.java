package pdl.insegura.commands.subcommands;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;

public class HelpCommand implements SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(Player player, String[] args) {
        // Sonido sutil de apertura del menú
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 0.6f, 1.2f);

        // Cabecera con diseño mejorado
        player.sendMessage("");
        player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &6&lPENDULUM AYUDA&r &d&l&k|&r &8&l≪"));
        player.sendMessage("");

        // Comandos básicos con mejor formato y descripción
        player.sendMessage(MessageUtils.colorMessage("&7Comandos Disponibles:"));
        sendCommandInfo(player, "reto", "Visualiza el reto actual y sus requisitos");
        sendCommandInfo(player, "entregar", "Entrega los materiales para completar el reto");
        sendCommandInfo(player, "check", "Consulta tu tiempo restante disponible");
        sendCommandInfo(player, "time", "Información sobre el próximo reinicio de tiempo");
        sendCommandInfo(player, "help", "Muestra esta guía de comandos");

        // Si es admin, mostrar comandos adicionales con delay para mejor legibilidad
        if (player.hasPermission("pendulum.admin")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage("");
                    player.sendMessage(MessageUtils.colorMessage("&7Comandos de Administrador:"));
                    sendAdminCommandInfo(player, "give", "Otorga items especiales del servidor");
                    sendAdminCommandInfo(player, "ruleta", "Activa la ruleta de selección de retos");
                    sendAdminCommandInfo(player, "spawn", "Genera un Voided Knight en tu ubicación");
                    sendAdminCommandInfo(player, "reset", "Reinicia los contadores del servidor");

                    // Pie de página
                    player.sendMessage("");
                    player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &7Usa &d/pdl &7seguido del comando &d&l&k|&r &8&l≪"));
                    player.sendMessage("");

                    // Sonido de finalización para admin
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.6f, 1.0f);
                }
            }.runTaskLater(PendulumPlugin.getInstance(), 2L);
        } else {
            // Pie de página para usuarios normales
            player.sendMessage("");
            player.sendMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k|&r &7Usa &d/pdl &7seguido del comando &d&l&k|&r &8&l≪"));
            player.sendMessage("");
        }
    }

    private void sendCommandInfo(Player player, String command, String description) {
        player.sendMessage(MessageUtils.colorMessage("&8└ &d/pdl " + command + " &8» &7" + description));
    }

    private void sendAdminCommandInfo(Player player, String command, String description) {
        player.sendMessage(MessageUtils.colorMessage("&8└ &c/pdl " + command + " &8» &7" + description));
    }

    @Override
    public boolean requiresPermission() {
        return false;
    }
}