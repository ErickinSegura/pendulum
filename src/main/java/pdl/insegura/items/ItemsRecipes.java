package pdl.insegura.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.items.customs.netherite.AgileNetherite;
import pdl.insegura.items.customs.PendulumItems;
import pdl.insegura.items.customs.netherite.ReinforcedNetherite;
import pdl.insegura.items.customs.voided.AssaultArmor;
import pdl.insegura.items.customs.voided.GuardianArmor;
import pdl.insegura.items.customs.voided.VoidedArmor;
import pdl.insegura.items.customs.voided.VoidedItems;
import org.bukkit.inventory.RecipeChoice;
import pdl.insegura.utils.PendulumSettings;


public class ItemsRecipes implements Listener {
    private final JavaPlugin plugin;

    public ItemsRecipes(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    public void recipes() {
        try {
            registerEGapp();

            registerOroDoble();
            uncraftOroDoble();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (PendulumSettings.getInstance().getDia() >= 5) {
            try {
                registerDirtyHearty();

                registerReinforcedNetheriteHelmet();
                registerReinforcedNetheriteChestplate();
                registerReinforcedNetheriteLeggings();
                registerReinforcedNetheriteBoots();

                registerAgileNetheriteHelmet();
                registerAgileNetheriteChestplate();
                registerAgileNetheriteLeggings();
                registerAgileNetheriteBoots();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (PendulumSettings.getInstance().getDia() >= 10) {
            try {
                registarVoidedIngot();
                registerVoidedApple();
                registarVoidedSword();
                registrarVoidedPickaxe();
                registrarVoidedHelmet();
                registrarVoidedChest();
                registrarVoidedLegg();
                registrarVoidedBoots();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (PendulumSettings.getInstance().getDia() >= 15) {
            try {
                registarAssaultHelmet();
                registarAssaultChestplate();
                registarAssaultLeggings();
                registarAssaultBoots();

                registarGuardianHelmet();
                registarGuardianChestplate();
                registarGuardianLeggings();
                registarGuardianBoots();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registerEGapp() {
        ItemStack s = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("gapp"), s);
        recipe.shape("GGG", "GAG", "GGG");
        recipe.setIngredient('G', Material.POPPED_CHORUS_FRUIT);
        recipe.setIngredient('A', Material.APPLE);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerOroDoble() {
        ItemStack s = PendulumItems.CrearOroDoble();
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("oro_doble"), s);
        recipe.addIngredient(2, Material.GOLD_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void uncraftOroDoble(){
        ItemStack s = new ItemStack(Material.GOLD_BLOCK);
        ShapelessRecipe recipe = new ShapelessRecipe(NamespacedKey.minecraft("bloque_oro"), s);
        recipe.addIngredient(1, Material.POPPED_CHORUS_FRUIT);
        plugin.getServer().addRecipe(recipe);
    }


    // Dia 5
    private void registerDirtyHearty() {
        ItemStack s = PendulumItems.CrearDirtyHearty();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("dirty_hearty"), s);
        recipe.shape("GGG", "GAG", "GGG");
        recipe.setIngredient('G', Material.POPPED_CHORUS_FRUIT);
        recipe.setIngredient('A', Material.PLAYER_HEAD);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerReinforcedNetheriteHelmet() {
        ItemStack s = ReinforcedNetherite.CrearReinHelmet();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("reinforced_netherite_helmet"), s);
        recipe.shape(" G ", "GAG", " G ");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_HELMET);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerReinforcedNetheriteChestplate() {
        ItemStack s = ReinforcedNetherite.CrearReinChestplate();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("reinforced_netherite_chest"), s);
        recipe.shape(" G ", "GAG", " G ");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_CHESTPLATE);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerReinforcedNetheriteLeggings() {
        ItemStack s = ReinforcedNetherite.CrearReinLeggings();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("reinforced_netherite_legg"), s);
        recipe.shape(" G ", "GAG", " G ");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_LEGGINGS);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerReinforcedNetheriteBoots() {
        ItemStack s = ReinforcedNetherite.CrearReinBoots();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("reinforced_netherite_boots"), s);
        recipe.shape(" G ", "GAG", " G ");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_BOOTS);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerAgileNetheriteHelmet() {
        ItemStack s = AgileNetherite.CrearAgiHelmet();
        ShapedRecipe recipe =  new ShapedRecipe(NamespacedKey.minecraft("agile_netherite_helmet"), s);
        recipe.shape("G G", " A ", "G G");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_HELMET);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerAgileNetheriteChestplate() {
        ItemStack s = AgileNetherite.CrearAgiChestplate();
        ShapedRecipe recipe =  new ShapedRecipe(NamespacedKey.minecraft("agile_netherite_chest"), s);
        recipe.shape("G G", " A ", "G G");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_CHESTPLATE);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerAgileNetheriteLeggings() {
        ItemStack s = AgileNetherite.CrearAgiLeggings();
        ShapedRecipe recipe =  new ShapedRecipe(NamespacedKey.minecraft("agile_netherite_leg"), s);
        recipe.shape("G G", " A ", "G G");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_LEGGINGS);
        plugin.getServer().addRecipe(recipe);
    }

    private void registerAgileNetheriteBoots() {
        ItemStack s = AgileNetherite.CrearAgiBoots();
        ShapedRecipe recipe =  new ShapedRecipe(NamespacedKey.minecraft("agile_netherite_boots"), s);
        recipe.shape("G G", " A ", "G G");
        recipe.setIngredient('G', Material.NETHERITE_INGOT);
        recipe.setIngredient('A', Material.NETHERITE_BOOTS);
        plugin.getServer().addRecipe(recipe);
    }

    // Dia 10

    private void registarVoidedIngot(){
        ItemStack s = VoidedItems.CrearVoidedIngot();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_ingot"), s);
        recipe.shape("GGG", "GGG", "GGG");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedShard(1)));
        plugin.getServer().addRecipe(recipe);
    }

    private void registerVoidedApple() {
        ItemStack s = VoidedItems.CrearVoidedApple();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_apple"), s);
        recipe.shape("GGG", "GAG", "GGG");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        recipe.setIngredient('A', Material.APPLE);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarVoidedSword() {
        ItemStack s = VoidedItems.CrearVoidedSword();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_sword"), s);
        recipe.shape(" G ", " G ", " A ");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        recipe.setIngredient('A', Material.STICK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registrarVoidedPickaxe() {
        ItemStack s = VoidedItems.CrearVoidedPick();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_pickaxe"), s);
        recipe.shape("GGG", " A ", " A ");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        recipe.setIngredient('A', Material.STICK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registrarVoidedHelmet() {
        ItemStack s = VoidedArmor.CrearVoidHelmet();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_helmet"),s);
        recipe.shape("GGG","G G");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        plugin.getServer().addRecipe(recipe);
    }

    private void registrarVoidedChest() {
        ItemStack s = VoidedArmor.CrearVoidChestplate();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_chest"),s);
        recipe.shape("G G","GGG","GGG");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        plugin.getServer().addRecipe(recipe);
    }

    private void registrarVoidedLegg() {
        ItemStack s = VoidedArmor.CrearVoidLeggings();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_legg"),s);
        recipe.shape("GGG","G G","G G");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        plugin.getServer().addRecipe(recipe);
    }

    private void registrarVoidedBoots() {
        ItemStack s = VoidedArmor.CrearVoidBoots();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("voided_boots"),s);
        recipe.shape("G G","G G");
        recipe.setIngredient('G', new RecipeChoice.ExactChoice(VoidedItems.CrearVoidedIngot()));
        plugin.getServer().addRecipe(recipe);
    }

    // Dia 15

    private void registarAssaultHelmet() {
        ItemStack s = AssaultArmor.CrearAssaultHelmet();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("assault_helmet"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_HELMET);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarAssaultChestplate() {
        ItemStack s = AssaultArmor.crearAssaultChestplate();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("assault_chestplate"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_CHESTPLATE);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarAssaultLeggings() {
        ItemStack s = AssaultArmor.crearAssaultLeggings();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("assault_leggings"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_LEGGINGS);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarAssaultBoots() {
        ItemStack s = AssaultArmor.crearAssaultBoots();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("assault_boots"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_BOOTS);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarGuardianHelmet() {
        ItemStack s = GuardianArmor.CrearGuardianHelmet();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("guardian_helmet"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_HELMET);
        recipe.setIngredient('R', Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarGuardianChestplate() {
        ItemStack s = GuardianArmor.CrearGuardianChestplate();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("guardian_chestplate"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_CHESTPLATE);
        recipe.setIngredient('R', Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarGuardianLeggings() {
        ItemStack s = GuardianArmor.CrearGuardianLeggings();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("guardian_leggings"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_LEGGINGS);
        recipe.setIngredient('R', Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

    private void registarGuardianBoots() {
        ItemStack s = GuardianArmor.CrearGuardianBoots();
        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("guardian_boots"), s);
        recipe.shape("RGR", "GAG", "RGR");
        recipe.setIngredient('G', Material.NETHER_STAR);
        recipe.setIngredient('A', Material.NETHERITE_BOOTS);
        recipe.setIngredient('R', Material.LAPIS_BLOCK);
        plugin.getServer().addRecipe(recipe);
    }

}
