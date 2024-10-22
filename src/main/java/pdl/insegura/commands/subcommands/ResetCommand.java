package pdl.insegura.commands.subcommands;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;

public class ResetCommand implements SubCommand {
    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public void execute(Player player, String[] args) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(PendulumPlugin.getInstance(), "DirtyCount");
        data.remove(key);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.sendMessage(MessageUtils.colorMessage("&aTu contador de Dirty Hearthy ha sido reiniciado."));
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }
}