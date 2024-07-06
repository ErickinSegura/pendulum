package pdl.insegura.items.customs.voided;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;

import java.util.UUID;

public class VoidedItems {
    public static String appleName = MessageUtils.colorMessage("&d&lVoided Apple");
    public static String swordName = MessageUtils.colorMessage("&d&lVoided Sword");
    public static String pickName = MessageUtils.colorMessage("&d&lVoided Pickaxe");
    public static String shardName = MessageUtils.colorMessage("&d&lVoided Shard");
    public static String ingotName = MessageUtils.colorMessage("&d&lVoided Ingot");

    public static ItemStack CrearVoidedShard() {
        ItemStack s = new ItemStack(new ItemStack(Material.NETHERITE_SCRAP,1));
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(shardName);
        meta.setCustomModelData(1);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidedIngot() {
        ItemStack s = new ItemStack(new ItemStack(Material.NETHERITE_INGOT,1));
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(ingotName);
        meta.setCustomModelData(1);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidedApple() {
        ItemStack s = new ItemStack(new ItemStack(Material.APPLE,1));
        ItemMeta meta = s.getItemMeta();
        meta.setDisplayName(appleName);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setCustomModelData(1);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidedSword() {
        ItemStack s = new ItemStack(new ItemStack(Material.NETHERITE_SWORD));
        ItemMeta meta = s.getItemMeta();

        meta.setDisplayName(swordName);
        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(),"generic.movement_speed", 0.02, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", 1.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", 12.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);

        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier1);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier3);
        meta.setUnbreakable(true);

        meta.setCustomModelData(1);

        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidedPick() {
        ItemStack s = new ItemStack(new ItemStack(Material.NETHERITE_PICKAXE));
        ItemMeta meta = s.getItemMeta();

        meta.setDisplayName(pickName);
        meta.setUnbreakable(true);

        meta.setCustomModelData(1);

        s.setItemMeta(meta);
        return s;
    }


}
