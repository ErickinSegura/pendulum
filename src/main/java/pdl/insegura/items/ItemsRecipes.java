package pdl.insegura.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;

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
        rdirtyHearty.setIngredient('G', Material.TARGET);
        rdirtyHearty.setIngredient('A', Material.PLAYER_HEAD);

        getServer().addRecipe(rdirtyHearty);


        // Netherite Armor

        ItemStack netheriteHelmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta netheriteHelmetMeta = netheriteHelmet.getItemMeta();
        netheriteHelmetMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier("generic.movementSpeed", -0.2, AttributeModifier.Operation.ADD_NUMBER));
        netheriteHelmetMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        netheriteHelmetMeta.setDisplayName(MessageUtils.colorMessage("&6&lNetherite Helmet"));
        netheriteHelmet.setItemMeta(netheriteHelmetMeta);

        SmithingRecipe rnetheriteHelmet = new SmithingRecipe(NamespacedKey.minecraft("netherite_helmet"), netheriteHelmet, new RecipeChoice.MaterialChoice(Material.NETHERITE_HELMET), new RecipeChoice.MaterialChoice(Material.GOLD_BLOCK));
        getServer().addRecipe(rnetheriteHelmet);





    }




}
