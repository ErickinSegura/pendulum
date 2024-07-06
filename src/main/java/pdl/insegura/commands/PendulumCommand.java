package pdl.insegura.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.items.customs.PendulumItems;
import pdl.insegura.items.customs.netherite.AgileNetherite;
import pdl.insegura.items.customs.netherite.ReinforcedNetherite;
import pdl.insegura.items.customs.voided.VoidedArmor;
import pdl.insegura.items.customs.voided.VoidedItems;
import pdl.insegura.utils.PendulumSettings;
import pdl.insegura.utils.MessageUtils;

import static org.bukkit.Bukkit.getServer;

public class PendulumCommand implements CommandExecutor {
    public static String reto = PendulumSettings.getInstance().getDesafio();
    public static String recom = PendulumSettings.getInstance().getPremio();
    public static String castigo = PendulumSettings.getInstance().getCastigo();
    public static final Material RETO_MATERIAL = PendulumSettings.getInstance().getMaterialDesafio();
    public static final int RETO_CANTIDAD = PendulumSettings.getInstance().getCantidadDesafio();
    public static final ItemStack RECOMPENSA = PendulumSettings.getInstance().getStackPremio();
    public static String[] RETOS = PendulumSettings.getInstance().getRetos();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.colorMessage("&cEste comando solo puede ser ejecutado por un jugador."));
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            mostrarInformacionGeneral(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "reto":
                mostrarReto(player);
                break;
            case "entregar":
                entregarReto(player);
                break;
            case "check":
                player.performCommand("ptl check");
                break;
            case "time":
                player.performCommand("ptl info");
                break;
            case "help":
                mostrarAyuda(player);
                break;
            case "resetcontador":
                resetContador(player);
                break;
            case "ruleta":
                if (player.getName().equals("iPancrema")) {
                    ruleta();
                } else {
                    player.sendMessage("&cNo puedes ejecutar este comando");
                }
                break;
            case "give":
                give(args, player);
                break;
            default:
                sender.sendMessage(MessageUtils.colorMessage("&cComando no reconocido. Usa /pendulum help para ver los comandos disponibles."));
                return true;
        }

        return true;
    }

    private void mostrarInformacionGeneral(Player player) {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        String equipo = (team != null) ? team.getPrefix() : "Sin equipo";
        Score retoScore = player.getScoreboard().getObjective("reto").getScore(player.getName());
        boolean retoCumplido = retoScore.getScore() > 0;

        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        player.sendMessage(MessageUtils.colorMessage("      &l[&d&lPendulum&r&l]"));
        player.sendMessage(MessageUtils.colorMessage("      Bloque de días: &d" +  PendulumSettings.getInstance().getDia()));
        player.sendMessage(MessageUtils.colorMessage("      Jugadores: &d" + getServer().getOnlinePlayers().size()));
        player.sendMessage(MessageUtils.colorMessage("      Equipo: " + equipo));
        player.sendMessage(MessageUtils.colorMessage("      Reto: &d" + (retoCumplido ? "Cumplido" : "No cumplido")));
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    private void mostrarReto(Player player) {
        Score retoScore = player.getScoreboard().getObjective("reto").getScore(player.getName());
        boolean retoCumplido = retoScore.getScore() > 0;

        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        player.sendMessage(MessageUtils.colorMessage("&lReto: &d" + reto));
        player.sendMessage(MessageUtils.colorMessage("&lRecompensa: &d" + recom));
        player.sendMessage(MessageUtils.colorMessage("&lCastigo: &d" + castigo));
        player.sendMessage(MessageUtils.colorMessage("&lEstado: &d" + (retoCumplido ? "Cumplido" : "No cumplido")));
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    private void entregarReto(Player player) {
        Score retoScore = player.getScoreboard().getObjective("reto").getScore(player.getName());

        if (retoScore.getScore() == 0 && player.getInventory().contains(RETO_MATERIAL, RETO_CANTIDAD)) {
            player.getInventory().removeItem(new ItemStack(RETO_MATERIAL, RETO_CANTIDAD));
            player.getInventory().addItem(RECOMPENSA);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            retoScore.setScore(1);
            getServer().broadcastMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l] &d" + player.getName() + "&r ha cumplido el reto!"));
        } else if (retoScore.getScore() > 0) {
            player.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r Ya has entregado el reto!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        } else {
            player.sendMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r No tienes los materiales necesarios para entregar el reto!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    private void mostrarAyuda(Player player) {
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
        player.sendMessage(MessageUtils.colorMessage("&lComandos de &d&lPendulum&r&l:\n"));
        player.sendMessage(MessageUtils.colorMessage("&l> reto &d- &rMuestra el reto actual."));
        player.sendMessage(MessageUtils.colorMessage("&l> entregar &d- &rEntrega el reto actual."));
        player.sendMessage(MessageUtils.colorMessage("&l> check &d- &rMuestra tu tiempo restante."));
        player.sendMessage(MessageUtils.colorMessage("&l> time &d- &rMuestra datos del reinicio de tiempo."));
        player.sendMessage(MessageUtils.colorMessage("&l> help &d- &rMuestra este mensaje."));
        player.sendMessage(MessageUtils.colorMessage("&d&m                                          "));
    }

    private void ruleta() {
        // Random int del 1 al 10
        int random = (int) (Math.random() * 10) + 1;
        getServer().broadcastMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r Seleccionando reto del bloque"));

        for (int i = 0; i <= 10; i++) {
            try {
                for (Player players : Bukkit.getOnlinePlayers())
                {
                    //Player the sound for  player
                    players.playSound(players.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getServer().broadcastMessage(MessageUtils.colorMessage("&l[&d&lPendulum&r&l]&r El reto es &l"+ RETOS[random]));
    }

    private void resetContador(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(PendulumPlugin.getInstance(), "DirtyCount");
        data.remove(key);
        player.sendMessage(MessageUtils.colorMessage("&aTu contador de Dirty Hearthy ha sido reiniciado."));
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);

    }

    private void give(String[] args, Player player) {
        String subCommand = "";
        if (!player.getName().equals("iPancrema")) {
            player.sendMessage(MessageUtils.colorMessage("&cNo puedes usar este comando"));
            return;
        }

        try {
            subCommand = args[1].toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }


        switch (subCommand) {
            case "nether_armors":
                player.getInventory().addItem(AgileNetherite.CrearAgiHelmet());
                player.getInventory().addItem(AgileNetherite.CrearAgiChestplate());
                player.getInventory().addItem(AgileNetherite.CrearAgiLeggings());
                player.getInventory().addItem(AgileNetherite.CrearAgiBoots());

                player.getInventory().addItem(ReinforcedNetherite.CrearReinHelmet());
                player.getInventory().addItem(ReinforcedNetherite.CrearReinChestplate());
                player.getInventory().addItem(ReinforcedNetherite.CrearReinLeggings());
                player.getInventory().addItem(ReinforcedNetherite.CrearReinBoots());

                player.sendMessage("Se te añadieron Netherite Armors");
                break;
            case "voided_armor":
                player.getInventory().addItem(VoidedArmor.CrearVoidHelmet());
                player.getInventory().addItem(VoidedArmor.CrearVoidChestplate());
                player.getInventory().addItem(VoidedArmor.CrearVoidLeggings());
                player.getInventory().addItem(VoidedArmor.CrearVoidBoots());

                player.sendMessage("Se te añadio Voided Armor");
                break;
            case "voided_items":
                player.getInventory().addItem(VoidedItems.CrearVoidedShard());
                player.getInventory().addItem(VoidedItems.CrearVoidedIngot());
                player.getInventory().addItem(VoidedItems.CrearVoidedApple());
                player.getInventory().addItem(VoidedItems.CrearVoidedPick());
                player.getInventory().addItem(VoidedItems.CrearVoidedSword());

                player.sendMessage("Se te añadio Voided Items");
                break;

            case "pendu_items":
                player.getInventory().addItem(PendulumItems.CrearDirtyHearty());
                player.getInventory().addItem(PendulumItems.CrearOroDoble());

                player.sendMessage("Se te añadio Pendulum Items");
                break;

            default:
                player.sendMessage("Las opciones son");
                player.sendMessage("nether_armors");
                player.sendMessage("voided_armor");
                player.sendMessage("voided_items");
                player.sendMessage("pendu_items");
                player.sendMessage("");
                break;
        }
    }
}
