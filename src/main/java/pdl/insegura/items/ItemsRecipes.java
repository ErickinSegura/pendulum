package pdl.insegura.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import pdl.insegura.utils.MessageUtils;

public class ItemsRecipes implements Listener {

    private final JavaPlugin plugin;
    private ItemStack oroDoble;
    private NamespacedKey spawnerKey;

    public ItemsRecipes(JavaPlugin plugin) {
        this.plugin = plugin;
        this.spawnerKey = new NamespacedKey(plugin, "custom_spawner");
        initializeItems();
    }

    private void initializeItems() {
        // Oro Doble
        oroDoble = new ItemStack(Material.SPAWNER);
        ItemMeta oroDobleMeta = oroDoble.getItemMeta();
        oroDobleMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        oroDobleMeta.setDisplayName(MessageUtils.colorMessage("&6&lOro Doble"));
        oroDobleMeta.addEnchant(Enchantment.MENDING, 1, false);
        oroDobleMeta.setCustomModelData(1);
        oroDoble.setItemMeta(oroDobleMeta);
    }

    public void recipes() {
        // Recetas

        // Oro Doble
        ShapelessRecipe roroDoble = new ShapelessRecipe(NamespacedKey.minecraft("oro_doble"), oroDoble);
        roroDoble.addIngredient(2, Material.GOLD_BLOCK);
        plugin.getServer().addRecipe(roroDoble);


        // Gapple
        ItemStack egapp = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ShapedRecipe regapp = new ShapedRecipe(NamespacedKey.minecraft("gapp"), egapp);
        regapp.shape("GGG", "GAG", "GGG");
        regapp.setIngredient('G', Material.SPAWNER);
        regapp.setIngredient('A', Material.APPLE);
        plugin.getServer().addRecipe(regapp);

        //Bloque de oro
        ItemStack bloqOro = new ItemStack(Material.GOLD_BLOCK, 2);
        ShapelessRecipe rbloqOro = new ShapelessRecipe(NamespacedKey.minecraft("bloque_de_oro"), bloqOro);
        rbloqOro.addIngredient(1, Material.SPAWNER);
        plugin.getServer().addRecipe(rbloqOro);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        if (block.getType() == Material.SPAWNER) {
            ItemStack itemInHand = event.getItemInHand();
            if (itemInHand.hasItemMeta() && itemInHand.getItemMeta().hasCustomModelData()
                    && itemInHand.getItemMeta().getCustomModelData() == 1) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.getPersistentDataContainer().set(spawnerKey, PersistentDataType.BYTE, (byte) 1);
                spawner.setDelay(Integer.MAX_VALUE);

                spawner.setRequiredPlayerRange(0);
                spawner.update();
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SPAWNER) {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            if (spawner.getPersistentDataContainer().has(spawnerKey, PersistentDataType.BYTE)) {
                event.setDropItems(false); // Evita que el spawner normal caiga
                event.setExpToDrop(0);
                block.getWorld().dropItemNaturally(block.getLocation(), oroDoble);
            }
        }
    }
}
