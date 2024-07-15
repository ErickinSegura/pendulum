package pdl.insegura.listeners.mobs;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pdl.insegura.utils.PendulumSettings;

import java.util.Random;

import static org.bukkit.Bukkit.getServer;

public class MobListener implements Listener{
    @EventHandler
    public void ZombieHit(EntityDamageByEntityEvent event){
        if (PendulumSettings.getInstance().getDia() >= 5) {
            if (event.getDamager() instanceof Zombie) {
                Player player = (Player) event.getEntity();
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 220, 1));
            }
        }
    }

    // En la muerte de un shulker box soltar algo
    private final Random random = new Random();

    @EventHandler
    public void onShulkerDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Shulker) {
            event.getDrops().clear();
            double rand = random.nextDouble();
            getServer().broadcastMessage("Probabiilidad: " +  rand);
            if (rand < 0.20) {
                event.getDrops().add(new ItemStack(Material.SHULKER_SHELL, 1));
            }
        }
    }
}
