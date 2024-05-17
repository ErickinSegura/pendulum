package pdl.insegura.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class ItemsRecipes {

    public void recipes() {

        // Oro Doble

        ItemStack oroDoble = new ItemStack(Material.SPAWNER);
        ItemMeta oroDobleMeta = oroDoble.getItemMeta();
        oroDobleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        oroDobleMeta.setDisplayName(MessageUtils.colorMessage("&6&lOro Doble"));
        oroDobleMeta.addEnchant(Enchantment.MENDING, 1, false);
        oroDobleMeta.setCustomModelData(1);
        oroDoble.setItemMeta(oroDobleMeta);

        ShapelessRecipe roroDoble = new ShapelessRecipe(NamespacedKey.minecraft("oro_doble"), oroDoble);

        roroDoble.addIngredient(2, Material.GOLD_BLOCK);
        getServer().addRecipe(roroDoble);


        // Gapp

        ItemStack egapp = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ShapedRecipe regapp = new ShapedRecipe(NamespacedKey.minecraft("gapp"), egapp);

        regapp.shape("GGG", "GAG", "GGG");
        regapp.setIngredient('G', Material.TARGET,2);
        regapp.setIngredient('A', Material.APPLE);

        getServer().addRecipe(regapp);

        // Dirty Hearty

        ItemStack dirtyHearty = new ItemStack(Material.POPPED_CHORUS_FRUIT);
        ItemMeta dirtyHeartyMeta = dirtyHearty.getItemMeta();
        dirtyHeartyMeta.setDisplayName(MessageUtils.colorMessage("&c&lDirty Hearty"));
        dirtyHeartyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        dirtyHeartyMeta.setCustomModelData(1);
        dirtyHeartyMeta.addEnchant(Enchantment.MENDING, 1, false);

        dirtyHearty.setItemMeta(dirtyHeartyMeta);

        ShapedRecipe rdirtyHearty = new ShapedRecipe(NamespacedKey.minecraft("dirty_hearty"), dirtyHearty);
        rdirtyHearty.shape("GGG", "GAG", "GGG");
        rdirtyHearty.setIngredient('G', Material.SPAWNER);
        rdirtyHearty.setIngredient('A', Material.PLAYER_HEAD);

        getServer().addRecipe(rdirtyHearty);


        // Netherite Armor

        ItemStack netheriteChestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta netheriteChestplateMeta = netheriteChestplate.getItemMeta();
        netheriteChestplateMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.01, AttributeModifier.Operation.ADD_NUMBER));
        netheriteChestplateMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 10, AttributeModifier.Operation.ADD_NUMBER));
        netheriteChestplateMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 5, AttributeModifier.Operation.ADD_NUMBER));
        netheriteChestplateMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        netheriteChestplateMeta.setDisplayName(MessageUtils.colorMessage("&6&lReinforced Netherite Chestplate"));
        ArrayList<String> loreChest = new ArrayList<>();

        loreChest.add("");
        loreChest.add(MessageUtils.colorMessage("&7When on Body:"));
        loreChest.add(MessageUtils.colorMessage("&9+10 Armor"));
        loreChest.add(MessageUtils.colorMessage("&9+5 Armor Toughness"));
        loreChest.add(MessageUtils.colorMessage("&9+1 Knockback Resistance"));
        loreChest.add(MessageUtils.colorMessage("&9-1% Movement Speed"));

        netheriteChestplateMeta.setLore(loreChest);
        netheriteChestplate.setItemMeta(netheriteChestplateMeta);

        ShapedRecipe rnetheriteChestplate = new ShapedRecipe(NamespacedKey.minecraft("netherite_chestplate"), netheriteChestplate);
        rnetheriteChestplate.shape("GGG", "GAG", "GGG");
        rnetheriteChestplate.setIngredient('G', Material.SPAWNER);
        rnetheriteChestplate.setIngredient('A', Material.NETHERITE_CHESTPLATE);
        getServer().addRecipe(rnetheriteChestplate);

        // Casco de Netherite
        ItemStack netheriteHelmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta netheriteHelmetMeta = netheriteHelmet.getItemMeta();
        netheriteHelmetMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.01, AttributeModifier.Operation.ADD_NUMBER));
        netheriteHelmetMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 5, AttributeModifier.Operation.ADD_NUMBER));
        netheriteHelmetMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 5, AttributeModifier.Operation.ADD_NUMBER));
        netheriteHelmetMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        netheriteHelmetMeta.setDisplayName(MessageUtils.colorMessage("&6&lReinforced Netherite Helmet"));
        ArrayList<String> loreHelmet = new ArrayList<>();
        loreHelmet.add("");
        loreHelmet.add(MessageUtils.colorMessage("&7When on Head:"));
        loreHelmet.add(MessageUtils.colorMessage("&9+5 Armor"));
        loreHelmet.add(MessageUtils.colorMessage("&9+5 Armor Toughness"));
        loreHelmet.add(MessageUtils.colorMessage("&9+1 Knockback Resistance"));
        loreHelmet.add(MessageUtils.colorMessage("&9-1% Movement Speed"));
        netheriteHelmetMeta.setLore(loreHelmet);
        netheriteHelmet.setItemMeta(netheriteHelmetMeta);

        ShapedRecipe rnetheriteHelmet = new ShapedRecipe(NamespacedKey.minecraft("netherite_helmet"), netheriteHelmet);
        rnetheriteHelmet.shape("GGG", "GAG", "GGG");
        rnetheriteHelmet.setIngredient('G', Material.SPAWNER);
        rnetheriteHelmet.setIngredient('A', Material.NETHERITE_HELMET);
        getServer().addRecipe(rnetheriteHelmet);

        // Grebas de Netherite
        ItemStack netheriteLeggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta netheriteLeggingsMeta = netheriteLeggings.getItemMeta();
        netheriteLeggingsMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.01, AttributeModifier.Operation.ADD_NUMBER));
        netheriteLeggingsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 8, AttributeModifier.Operation.ADD_NUMBER));
        netheriteLeggingsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 5, AttributeModifier.Operation.ADD_NUMBER));
        netheriteLeggingsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        netheriteLeggingsMeta.setDisplayName(MessageUtils.colorMessage("&6&lReinforced Netherite Leggings"));
        ArrayList<String> loreLeggings = new ArrayList<>();
        loreLeggings.add("");
        loreLeggings.add(MessageUtils.colorMessage("&7When on Legs:"));
        loreLeggings.add(MessageUtils.colorMessage("&9+8 Armor"));
        loreLeggings.add(MessageUtils.colorMessage("&9+5 Armor Toughness"));
        loreLeggings.add(MessageUtils.colorMessage("&9+1 Knockback Resistance"));
        loreLeggings.add(MessageUtils.colorMessage("&9-1% Movement Speed"));
        netheriteLeggingsMeta.setLore(loreLeggings);
        netheriteLeggings.setItemMeta(netheriteLeggingsMeta);

        ShapedRecipe rnetheriteLeggings = new ShapedRecipe(NamespacedKey.minecraft("netherite_leggings"), netheriteLeggings);
        rnetheriteLeggings.shape("GGG", "GAG", "GGG");
        rnetheriteLeggings.setIngredient('G', Material.SPAWNER);
        rnetheriteLeggings.setIngredient('A', Material.NETHERITE_LEGGINGS);
        getServer().addRecipe(rnetheriteLeggings);

        // Botas de Netherite
        ItemStack netheriteBoots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta netheriteBootsMeta = netheriteBoots.getItemMeta();
        netheriteBootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(), "generic.armor", 5, AttributeModifier.Operation.ADD_NUMBER));
        netheriteBootsMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armorToughness", 5, AttributeModifier.Operation.ADD_NUMBER));
        netheriteBootsMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(), "generic.movementSpeed", -0.01, AttributeModifier.Operation.ADD_NUMBER));
        netheriteBootsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        netheriteBootsMeta.setDisplayName(MessageUtils.colorMessage("&6&lReinforced Netherite Boots"));
        ArrayList<String> loreBoots = new ArrayList<>();
        loreBoots.add("");
        loreBoots.add(MessageUtils.colorMessage("&7When on Feet:"));
        loreBoots.add(MessageUtils.colorMessage("&9+5 Armor"));
        loreBoots.add(MessageUtils.colorMessage("&9+5 Armor Toughness"));
        loreBoots.add(MessageUtils.colorMessage("&9+1 Knockback Resistance"));
        loreBoots.add(MessageUtils.colorMessage("&9-1% Movement Speed"));
        netheriteBootsMeta.setLore(loreBoots);
        netheriteBoots.setItemMeta(netheriteBootsMeta);

        ShapedRecipe rnetheriteBoots = new ShapedRecipe(NamespacedKey.minecraft("netherite_boots"), netheriteBoots);
        rnetheriteBoots.shape("GGG", "GAG", "GGG");
        rnetheriteBoots.setIngredient('G', Material.SPAWNER);
        rnetheriteBoots.setIngredient('A', Material.NETHERITE_BOOTS);
        getServer().addRecipe(rnetheriteBoots);
    }

}
