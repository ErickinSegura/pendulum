package pdl.insegura.items.customs.voided;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;

import java.util.UUID;

public class VoidedArmor {

    public static String helmetName = MessageUtils.colorMessage("&d&lVoided Helmet");
    public static String chestplateName = MessageUtils.colorMessage("&d&lVoided Chestplate");
    public static String leggingsName = MessageUtils.colorMessage("&d&lVoided Leggings");
    public static String bootsName = MessageUtils.colorMessage("&d&lVoided Boots");

    public static ItemStack CrearVoidHelmet() {
        ItemStack s = new ItemStack(Material.NETHERITE_HELMET, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.HEAD;
        meta.setDisplayName(helmetName);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"generic.armor", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(),"generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);
        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(),"generic.movement_speed", 0.005, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);
        meta.addEnchant(Enchantment.DURABILITY, 6, true);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidChestplate() {
        ItemStack s = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.CHEST;
        meta.setDisplayName(chestplateName);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"generic.armor", 10, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(),"generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);
        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(),"generic.movement_speed", 0.005, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);
        meta.addEnchant(Enchantment.DURABILITY, 6, true);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidLeggings() {
        ItemStack s = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.LEGS;
        meta.setDisplayName(leggingsName);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(),"generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);
        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(),"generic.movement_speed", 0.005, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);
        meta.addEnchant(Enchantment.DURABILITY, 6, true);
        s.setItemMeta(meta);
        return s;
    }

    public static ItemStack CrearVoidBoots() {
        ItemStack s = new ItemStack(Material.NETHERITE_BOOTS, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.FEET;
        meta.setDisplayName(bootsName);
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(),"generic.armor", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier);
        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(),"generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);
        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(),"generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);
        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(),"generic.movement_speed", 0.005, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);
        meta.addEnchant(Enchantment.DURABILITY, 6, true);
        s.setItemMeta(meta);
        return s;
    }
}


