package pdl.insegura.items.customs.voided;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;

import java.util.UUID;

public class AssaultArmor {

    public static String helmetName = MessageUtils.colorMessage("&d&lVoided Assault Helmet");
    public static String chestplateName = MessageUtils.colorMessage("&d&lVoided Assault Chestplate");
    public static String leggingsName = MessageUtils.colorMessage("&d&lVoided Assault Leggings");
    public static String bootsName = MessageUtils.colorMessage("&d&lVoided Assault Boots");


    public static ItemStack CrearAssaultHelmet() {
        ItemStack s = new ItemStack(Material.NETHERITE_HELMET, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.HEAD;

        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier1);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);

        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(), "generic.movement_speed", 0.006, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);

        AttributeModifier modifier5 = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", 2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier5);

        meta.setUnbreakable(true);
        meta.setDisplayName(helmetName);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack crearAssaultChestplate() {
        ItemStack s = new ItemStack(Material.NETHERITE_CHESTPLATE, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.CHEST;

        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.armor", 11, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier1);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);

        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(), "generic.movement_speed", 0.006, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);

        AttributeModifier modifier5 = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", 2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier5);

        meta.setUnbreakable(true);
        meta.setDisplayName(chestplateName);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack crearAssaultLeggings() {
        ItemStack s = new ItemStack(Material.NETHERITE_LEGGINGS, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.LEGS;

        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.armor", 9, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier1);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);

        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(), "generic.movement_speed", 0.006, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);

        AttributeModifier modifier5 = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", 2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier5);

        meta.setUnbreakable(true);
        meta.setDisplayName(leggingsName);
        s.setItemMeta(meta);

        return s;
    }

    public static ItemStack crearAssaultBoots() {
        ItemStack s = new ItemStack(Material.NETHERITE_BOOTS, 1);
        ItemMeta meta = s.getItemMeta();
        EquipmentSlot slot = EquipmentSlot.FEET;

        AttributeModifier modifier1 = new AttributeModifier(UUID.randomUUID(), "generic.armor", 6, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, modifier1);

        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 5, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, modifier2);

        AttributeModifier modifier3 = new AttributeModifier(UUID.randomUUID(), "generic.knockback_resistance", 0.2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, modifier3);

        AttributeModifier modifier4 = new AttributeModifier(UUID.randomUUID(), "generic.movement_speed", 0.006, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, modifier4);

        AttributeModifier modifier5 = new AttributeModifier(UUID.randomUUID(), "generic.maxHealth", 2, AttributeModifier.Operation.ADD_NUMBER, slot);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, modifier5);

        meta.setUnbreakable(true);
        meta.setDisplayName(bootsName);
        s.setItemMeta(meta);

        return s;
    }
}
