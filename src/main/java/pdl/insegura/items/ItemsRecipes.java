package pdl.insegura.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import pdl.insegura.utils.MessageUtils;

import static org.bukkit.Bukkit.getServer;

public class ItemsRecipes {

    public void recipes() {

        // Diana

        ShapedRecipe rdiana = new ShapedRecipe(NamespacedKey.minecraft("diana"), new ItemStack(Material.REDSTONE_BLOCK));
        rdiana.shape(" I ", "IHI", " I ");
        rdiana.setIngredient('I', Material.REDSTONE);
        rdiana.setIngredient('H', Material.HAY_BLOCK);
        ShapelessRecipe bloqueOro = new ShapelessRecipe(NamespacedKey.minecraft("bloque_oro"), new ItemStack(Material.GOLD_BLOCK, 2));
        bloqueOro.addIngredient(Material.TARGET);
        getServer().addRecipe(bloqueOro);

        getServer().addRecipe(rdiana);

        // Oro Doble

        ItemStack oroDoble = new ItemStack(Material.TARGET);
        ItemMeta oroDobleMeta = oroDoble.getItemMeta();
        oroDobleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        oroDobleMeta.setDisplayName(MessageUtils.colorMessage("&6&lOro Doble"));
        oroDobleMeta.addEnchant(Enchantment.MENDING, 1, false);
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




    }
}
