package pdl.insegura.items.customs;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.PendulumPlugin;

public class PendulumItems implements Listener {

    private final NamespacedKey spawnerKey = new NamespacedKey(PendulumPlugin.getInstance(), "specialSpawner");

    public static ItemStack CrearOroDoble() {
        ItemStack s = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(MessageUtils.colorMessage("&6&lOro Doble"));
        meta.setCustomModelData(1);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearDirtyHearty() {
        ItemStack s = new ItemStack(Material.BEETROOT);
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(MessageUtils.colorMessage("&c&lDirty Hearthy"));
        meta.setCustomModelData(1);
        meta.addEnchant(Enchantment.MENDING, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        s.setItemMeta(meta);
        return s;
    }
}
