package pdl.insegura.listeners.mobs;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pdl.insegura.utils.PendulumSettings;

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
}
