package pdl.insegura.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.utils.MessageUtils;

public class ArmorListener implements Listener {

    private static final String ASSAULT_HELMET_NAME = MessageUtils.colorMessage("&d&lVoided Assault Helmet");
    private static final String ASSAULT_CHESTPLATE_NAME = MessageUtils.colorMessage("&d&lVoided Assault Chestplate");
    private static final String ASSAULT_LEGGINGS_NAME = MessageUtils.colorMessage("&d&lVoided Assault Leggings");
    private static final String ASSAULT_BOOTS_NAME = MessageUtils.colorMessage("&d&lVoided Assault Boots");

    private static final String GUARDIAN_HELMET_NAME = MessageUtils.colorMessage("&d&lVoided Guardian Helmet");
    private static final String GUARDIAN_CHESTPLATE_NAME = MessageUtils.colorMessage("&d&lVoided Guardian Chestplate");
    private static final String GUARDIAN_LEGGINGS_NAME = MessageUtils.colorMessage("&d&lVoided Guardian Leggings");
    private static final String GUARDIAN_BOOTS_NAME = MessageUtils.colorMessage("&d&lVoided Guardian Boots");


    PendulumPlugin plugin = PendulumPlugin.getInstance();
    AdvancementsListener advancementsListener = new AdvancementsListener(plugin);


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getSlotType() == InventoryType.SlotType.ARMOR || event.isShiftClick()) {
            Player player = (Player) event.getWhoClicked();
            // Ejecutar la comprobación en el siguiente tick para asegurar que el inventario se ha actualizado
            new BukkitRunnable() {
                @Override
                public void run() {
                    updatePlayerEffects(player);
                }
            }.runTask(PendulumPlugin.getInstance());
        }
    }

    private void updatePlayerEffects(Player player) {
        if (isWearingFullAssaultArmor(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        } else if (isWearingFullGuardianArmor(player)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);

        }
    }

    private boolean isWearingFullAssaultArmor(Player player) {
        return isCustomArmor(player.getInventory().getHelmet(), ASSAULT_HELMET_NAME) &&
                isCustomArmor(player.getInventory().getChestplate(), ASSAULT_CHESTPLATE_NAME) &&
                isCustomArmor(player.getInventory().getLeggings(), ASSAULT_LEGGINGS_NAME) &&
                isCustomArmor(player.getInventory().getBoots(), ASSAULT_BOOTS_NAME);
    }

    private boolean isWearingFullGuardianArmor(Player player) {
        return isCustomArmor(player.getInventory().getHelmet(), GUARDIAN_HELMET_NAME) &&
                isCustomArmor(player.getInventory().getChestplate(), GUARDIAN_CHESTPLATE_NAME) &&
                isCustomArmor(player.getInventory().getLeggings(), GUARDIAN_LEGGINGS_NAME) &&
                isCustomArmor(player.getInventory().getBoots(), GUARDIAN_BOOTS_NAME);
    }

    private boolean isCustomArmor(ItemStack item, String name) {
        return item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
                item.getItemMeta().getDisplayName().equals(name);
    }
}