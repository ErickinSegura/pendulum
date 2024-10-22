package pdl.insegura.listeners.mobs.customs;

import org.bukkit.*;
import org.bukkit.entity.Villager;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.scheduler.BukkitRunnable;
import pdl.insegura.PendulumPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExplosivePenguinVillager {

    private static final double SPAWN_CHANCE = 1;
    private static final double EXPLOSION_CHANCE = 0.15;
    private static final int EXPLOSION_POWER = 3;
    private static final int CHECK_INTERVAL = 100;

    public static void trySpawnPenguinVillager(Location location) {
        if (Math.random() < SPAWN_CHANCE) {
            spawnPenguinVillager(location);
        }
    }

    private static void spawnPenguinVillager(Location location) {
        Villager villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);

        // Configurar el aldeano
        villager.setCustomName("§bAldeano Pingüino");
        villager.setCustomNameVisible(true);
        villager.setProfession(Villager.Profession.NITWIT);
        villager.setVillagerType(Villager.Type.SNOW);

        // Añadir tradeos customizados
        setCustomTrades(villager);

        // Iniciar el temporizador para verificar explosiones
        startExplosionCheck(villager);
    }

    private static void setCustomTrades(Villager villager) {
        List<MerchantRecipe> recipes = new ArrayList<>();
        Random random = new Random();

        // Tradeo 1: 32 Snowballs por 1 Diamond
        MerchantRecipe recipe1 = new MerchantRecipe(
                new ItemStack(Material.DIAMOND, 1),
                10); // máximo de usos
        recipe1.addIngredient(new ItemStack(Material.SNOWBALL, 32));
        recipes.add(recipe1);

        // Tradeo 2: 1 Diamond + 16 Ice por 1 Packed Ice
        MerchantRecipe recipe2 = new MerchantRecipe(
                new ItemStack(Material.PACKED_ICE, 1),
                5);
        recipe2.addIngredient(new ItemStack(Material.DIAMOND, 1));
        recipe2.addIngredient(new ItemStack(Material.ICE, 16));
        recipes.add(recipe2);

        // Tradeo 3: 5 Diamonds por 1 Blue Ice
        MerchantRecipe recipe3 = new MerchantRecipe(
                new ItemStack(Material.BLUE_ICE, 1),
                3);
        recipe3.addIngredient(new ItemStack(Material.DIAMOND, 5));
        recipes.add(recipe3);

        // Tradeo 4: 64 Snow Blocks por 1 Enchanted Golden Apple
        MerchantRecipe recipe4 = new MerchantRecipe(
                new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1),
                1);
        recipe4.addIngredient(new ItemStack(Material.SNOW_BLOCK, 64));
        recipes.add(recipe4);

        // Tradeo 5: 32 Ice + 1 Diamond por 1 Frost Walker Book
        ItemStack frostWalkerBook = new ItemStack(Material.ENCHANTED_BOOK);
        frostWalkerBook.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.FROST_WALKER, 2);
        MerchantRecipe recipe5 = new MerchantRecipe(frostWalkerBook, 2);
        recipe5.addIngredient(new ItemStack(Material.ICE, 32));
        recipe5.addIngredient(new ItemStack(Material.DIAMOND, 1));
        recipes.add(recipe5);

        // Asignar los tradeos al aldeano
        villager.setRecipes(recipes);
    }

    private static void startExplosionCheck(Villager villager) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!villager.isValid() || villager.isDead()) {
                    this.cancel();
                    return;
                }

                if (Math.random() < EXPLOSION_CHANCE) {
                    explodePenguin(villager);
                    this.cancel();
                }
            }
        }.runTaskTimer(PendulumPlugin.getInstance(), CHECK_INTERVAL, CHECK_INTERVAL);
    }

    private static void explodePenguin(Villager villager) {
        Location loc = villager.getLocation();

        // Efectos de pre-explosión
        villager.getWorld().spawnParticle(Particle.SNOWBALL, loc, 50, 0.5, 1, 0.5, 0.1);
        villager.getWorld().playSound(loc, Sound.BLOCK_SNOW_BREAK, 1.0f, 1.0f);

        // Pequeño retraso antes de la explosión
        new BukkitRunnable() {
            @Override
            public void run() {
                // Crear la explosión
                villager.getWorld().createExplosion(loc, EXPLOSION_POWER, false, false);

                // Efectos adicionales
                villager.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                villager.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 1);

                // Generar bloques de nieve alrededor
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        Location snowLoc = loc.clone().add(x, 0, z);
                        if (snowLoc.getBlock().getType() == Material.AIR) {
                            snowLoc.getBlock().setType(Material.SNOW);
                        }
                    }
                }

                // Eliminar al aldeano
                villager.remove();
            }
        }.runTaskLater(PendulumPlugin.getInstance(), 20L);
    }
}