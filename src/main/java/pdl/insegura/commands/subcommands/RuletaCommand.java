package pdl.insegura.commands.subcommands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;
import pdl.insegura.utils.ConfigUpdater;
import pdl.insegura.utils.Reto;

import static org.bukkit.Bukkit.getServer;

public class RuletaCommand implements SubCommand {
    private static final int ANIMATION_DURATION = 5;
    private static final double INITIAL_SPEED = 0.1;
    private static final double FINAL_SPEED = 0.5;

    @Override
    public String getName() {
        return "ruleta";
    }

    @Override
    public void execute(Player player, String[] args) {
        Reto[] retos = PendulumSettings.getInstance().getRetos();
        String[] castigos = PendulumSettings.getInstance().getCastigos();

        if (retos.length == 0 || castigos.length == 0) {
            player.sendMessage(MessageUtils.colorMessage("&c¡Error! No hay suficientes retos o castigos configurados."));
            return;
        }

        int finalRetoIndex = (int) (Math.random() * retos.length);
        int finalCastigoIndex = (int) (Math.random() * castigos.length);

        // Efectos iniciales
        Bukkit.broadcastMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r &6¡La ruleta está girando!"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            p.spawnParticle(Particle.SPELL_WITCH, p.getLocation().add(0, 2, 0), 20, 0.5, 0.5, 0.5, 0);
        }

        new BukkitRunnable() {
            int ticks = 0;
            int currentRetoIndex = 0;
            int currentCastigoIndex = 0;
            double currentSpeed = INITIAL_SPEED;
            boolean showingReto = true;

            @Override
            public void run() {
                double progress = (double) ticks / (ANIMATION_DURATION * 20);
                currentSpeed = INITIAL_SPEED + (FINAL_SPEED - INITIAL_SPEED) * progress;

                if (ticks % (int)(currentSpeed * 20) == 0) {
                    showingReto = !showingReto;
                    String currentText;
                    String subtitle;

                    if (showingReto) {
                        currentText = retos[currentRetoIndex].getTitulo();
                        subtitle = "&7Seleccionando reto...";
                        currentRetoIndex = (currentRetoIndex + 1) % retos.length;
                    } else {
                        currentText = castigos[currentCastigoIndex];
                        subtitle = "&7Seleccionando castigo...";
                        currentCastigoIndex = (currentCastigoIndex + 1) % castigos.length;
                    }

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                MessageUtils.colorMessage("&d&l" + currentText),
                                MessageUtils.colorMessage(subtitle),
                                0, 20, 10
                        );

                        float pitch = 1.0f + (float)progress;
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, pitch);

                        if (ticks % 5 == 0) {
                            Location loc = p.getLocation();
                            p.spawnParticle(Particle.SPELL_INSTANT,
                                    loc.add(0, 1, 0),
                                    5, 0.5, 0.5, 0.5, 0);
                        }
                    }
                }

                if (ticks >= ANIMATION_DURATION * 20) {
                    // Seleccionar reto y castigo finales
                    Reto selectedReto = retos[finalRetoIndex];
                    String selectedCastigo = castigos[finalCastigoIndex];

                    // Actualizar config
                    ConfigUpdater.updateChallenge(selectedReto, selectedCastigo);

                    // Efectos finales
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        p.spawnParticle(Particle.EXPLOSION_LARGE,
                                p.getLocation().add(0, 2, 0),
                                1, 0, 0, 0, 0);

                        p.sendTitle(
                                MessageUtils.colorMessage("&6&l¡SELECCIÓN COMPLETADA!"),
                                MessageUtils.colorMessage("&e¡Nuevo reto y castigo definidos!"),
                                10, 60, 20
                        );
                    }

                    // Mensaje final con formato especial
                    getServer().broadcastMessage("");
                    getServer().broadcastMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k||&r &6&lNUEVO RETO SEMANAL&r &d&l&k||"));
                    getServer().broadcastMessage(MessageUtils.colorMessage("&7Reto seleccionado:"));
                    getServer().broadcastMessage(MessageUtils.colorMessage("&d&l" + selectedReto.getTitulo()));
                    getServer().broadcastMessage(MessageUtils.colorMessage("&7Castigo seleccionado:"));
                    getServer().broadcastMessage(MessageUtils.colorMessage("&c&l" + selectedCastigo));
                    getServer().broadcastMessage("");

                    // Recargar configuración
                    PendulumSettings.getInstance().load();

                    this.cancel();
                }

                ticks++;
            }
        }.runTaskTimer(PendulumPlugin.getInstance(), 0L, 1L);
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }
}