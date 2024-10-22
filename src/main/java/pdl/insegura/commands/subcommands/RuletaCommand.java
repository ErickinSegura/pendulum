package pdl.insegura.commands.subcommands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

public class RuletaCommand implements SubCommand {
    private static final int ANIMATION_DURATION = 5; // Duración en segundos
    private static final double INITIAL_SPEED = 0.1; // Velocidad inicial entre cambios
    private static final double FINAL_SPEED = 0.5; // Velocidad final entre cambios

    @Override
    public String getName() {
        return "ruleta";
    }

    @Override
    public void execute(Player player, String[] args) {
        String[] retos = PendulumSettings.getInstance().getRetos();
        int finalIndex = (int) (Math.random() * retos.length);

        // Efectos iniciales
        Bukkit.broadcastMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r &6¡La ruleta está girando!"));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
            p.spawnParticle(Particle.SPELL_WITCH, p.getLocation().add(0, 2, 0), 20, 0.5, 0.5, 0.5, 0);
        }

        new BukkitRunnable() {
            int ticks = 0;
            int currentIndex = 0;
            double currentSpeed = INITIAL_SPEED;

            @Override
            public void run() {
                // Calcula la progresión de la velocidad
                double progress = (double) ticks / (ANIMATION_DURATION * 20);
                currentSpeed = INITIAL_SPEED + (FINAL_SPEED - INITIAL_SPEED) * progress;

                if (ticks % (int)(currentSpeed * 20) == 0) {
                    // Muestra el reto actual en el título
                    String currentReto = retos[currentIndex];
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendTitle(
                                MessageUtils.colorMessage("&d&l" + currentReto),
                                MessageUtils.colorMessage("&7La ruleta está girando..."),
                                0, 20, 10
                        );

                        // Efectos de sonido que cambian con la velocidad
                        float pitch = 1.0f + (float)progress;
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, pitch);

                        // Partículas que siguen al jugador
                        if (ticks % 5 == 0) {
                            Location loc = p.getLocation();
                            p.spawnParticle(Particle.SPELL_INSTANT,
                                    loc.add(0, 1, 0),
                                    5, 0.5, 0.5, 0.5, 0);
                        }
                    }

                    currentIndex = (currentIndex + 1) % retos.length;
                }

                // Verifica si la animación ha terminado
                if (ticks >= ANIMATION_DURATION * 20) {
                    // Efectos finales
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        p.spawnParticle(Particle.EXPLOSION_LARGE,
                                p.getLocation().add(0, 2, 0),
                                1, 0, 0, 0, 0);
                        p.sendTitle(
                                MessageUtils.colorMessage("&6&l¡RETO SELECCIONADO!"),
                                MessageUtils.colorMessage("&e" + retos[finalIndex]),
                                10, 60, 20
                        );
                    }

                    // Mensaje final con formato especial
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage(MessageUtils.colorMessage("&8&l≫ &d&l&k||&r &6&lRESULTADO FINAL&r &d&l&k||"));
                    Bukkit.broadcastMessage(MessageUtils.colorMessage("&7El reto seleccionado es:"));
                    Bukkit.broadcastMessage(MessageUtils.colorMessage("&d&l" + retos[finalIndex]));
                    Bukkit.broadcastMessage("");

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