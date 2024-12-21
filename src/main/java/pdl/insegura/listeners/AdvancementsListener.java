package pdl.insegura.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getLogger;

public class AdvancementsListener implements Listener {

    private final Plugin plugin;

    public AdvancementsListener(Plugin plugin) {
        this.plugin = plugin;
    }


    public void obtainAdvancement(Player player, String advancementName) {
        NamespacedKey key = new NamespacedKey(plugin, advancementName);
        Advancement advancement = Bukkit.getAdvancement(key);
        if (advancement == null) {
            getLogger().warning("Advancement no encontrado: " + advancementName);
            return;
        }
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                progress.awardCriteria(criterion);
            }
        }
    }


}
