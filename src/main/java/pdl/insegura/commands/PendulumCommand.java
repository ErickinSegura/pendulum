package pdl.insegura.commands;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pdl.insegura.utils.MessageUtils;

import static org.bukkit.Bukkit.getServer;

public class PendulumCommand implements CommandExecutor {
    public static String reto = "Placeholder";
    public static String recom = "Placeholder";
    public static String castigo = "Placeholder";


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
            sender.sendMessage(MessageUtils.colorMessage("      &l[&d&lPendulum&r&l]"));
            sender.sendMessage(MessageUtils.colorMessage("      Dia: &d" + getServer().getWorld("world").getFullTime() / 24000));
            sender.sendMessage(MessageUtils.colorMessage("      Jugadores: &d" + getServer().getOnlinePlayers().size()));
            sender.sendMessage(MessageUtils.colorMessage("      Equipo: &dPlaceholder"));
            sender.sendMessage(MessageUtils.colorMessage("&d&m                                          "));




            return true;
        }


        if (args[0].equalsIgnoreCase("reto")) {
            sender.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
            sender.sendMessage(MessageUtils.colorMessage("&lReto: &d"+reto));
            sender.sendMessage(MessageUtils.colorMessage("&lRecompensa: &d"+recom));
            sender.sendMessage(MessageUtils.colorMessage("&lCastigo: &d"+castigo));
            sender.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
            return true;
        }

        if (args[0].equalsIgnoreCase("entregar")) {
            if(player.getInventory().contains(Material.SUGAR_CANE, 320) && player.getScoreboard().getObjective("reto").getScore(player.getName()).getScore() == 0){
                sender.sendMessage(MessageUtils.colorMessage("Has entregado el &dreto!"));
                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                player.getInventory().removeItem(new ItemStack(Material.SUGAR_CANE, 320));
                getServer().broadcastMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l] &d"+player.getName()+"&r ha cumplido el reto!"));
                player.getScoreboard().getObjective("reto").getScore(player.getName()).setScore(1);
                player.getInventory().addItem(new ItemStack(Material.TOTEM_OF_UNDYING, 2));

            } else if (player.getScoreboard().getObjective("reto").getScore(player.getName()).getScore() == 1) {
                sender.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r Ya has entregado el reto!"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);

            } else{
                sender.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r No tienes los materiales, no puedes entregar el reto!"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("check")) {
            ((Player) sender).performCommand("ptl check");
            return true;
        }

        if (args[0].equalsIgnoreCase("time")) {
            ((Player) sender).performCommand("ptl info");
            return true;
        }


        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
            sender.sendMessage(MessageUtils.colorMessage("&lComandos de &d&lPendulum&r&l:\n"));
            sender.sendMessage(MessageUtils.colorMessage("&l> reto &d- &rMuestra el reto actual."));
            sender.sendMessage(MessageUtils.colorMessage("&l> entregar &d- &rEntrega el reto actual."));
            sender.sendMessage(MessageUtils.colorMessage("&l> check &d- &rMuestra tu tiempo restante."));
            sender.sendMessage(MessageUtils.colorMessage("&l> time &d- &rMuestra datos del reinicio de tiempo."));
            sender.sendMessage(MessageUtils.colorMessage("&l> help &d- &rMuestra este mensaje."));
            sender.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
            return true;
        }




        return true;
    }
}
