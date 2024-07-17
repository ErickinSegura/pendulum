package pdl.insegura.items.customs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import pdl.insegura.listeners.mobs.customs.VoidedKnight;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.PendulumPlugin;

import java.util.Objects;

public class PendulumItems implements Listener {

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

    public static ItemStack crearKnightSpawner() {
        ItemStack spawner = new ItemStack(Material.SPAWNER);
        ItemMeta meta = spawner.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Voided Knight Spawner");
        spawner.setItemMeta(meta);
        return spawner;
    }





}
