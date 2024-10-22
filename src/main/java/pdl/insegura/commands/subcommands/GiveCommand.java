package pdl.insegura.commands.subcommands;

import org.bukkit.entity.Player;
import pdl.insegura.items.customs.netherite.*;
import pdl.insegura.items.customs.voided.*;
import pdl.insegura.items.customs.PendulumItems;
import pdl.insegura.utils.MessageUtils;

public class GiveCommand implements SubCommand {
    @Override
    public String getName() {
        return "give";
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(MessageUtils.colorMessage("&cUso: /pendulum give <item>"));
            return;
        }

        String itemType = args[1].toLowerCase();
        switch (itemType) {
            case "agile_netherite" -> giveAgileNetherite(player);
            case "reinforced_netherite" -> giveReinforcedNetherite(player);
            case "voided_armor" -> giveVoidedArmor(player);
            case "assault_armor" -> giveAssaultArmor(player);
            case "guardian_armor" -> giveGuardianArmor(player);
            case "voided_tools" -> giveVoidedTools(player);
            case "voided_ingot" -> giveVoidedIngot(player);
            case "voidad_shard" -> giveVoidedShard(player);
            case "dirty_hearty" -> giveDirtyHearty(player);
            case "oro_doble" -> giveOroDoble(player);
            default -> player.sendMessage(MessageUtils.colorMessage("&cItem no reconocido"));
        }
    }

    private void giveAgileNetherite(Player player) {
        player.getInventory().addItem(
                AgileNetherite.CrearAgiHelmet(),
                AgileNetherite.CrearAgiChestplate(),
                AgileNetherite.CrearAgiLeggings(),
                AgileNetherite.CrearAgiBoots()
        );
        sendGiveMessage(player, "Agile Netherite Armor");
    }

    private void giveReinforcedNetherite(Player player) {
        player.getInventory().addItem(
                ReinforcedNetherite.CrearReinHelmet(),
                ReinforcedNetherite.CrearReinChestplate(),
                ReinforcedNetherite.CrearReinLeggings(),
                ReinforcedNetherite.CrearReinBoots()
        );
        sendGiveMessage(player, "Reinforced Netherite Armor");
    }

    private void giveVoidedArmor(Player player) {
        player.getInventory().addItem(
                VoidedArmor.CrearVoidHelmet(),
                VoidedArmor.CrearVoidChestplate(),
                VoidedArmor.CrearVoidLeggings(),
                VoidedArmor.CrearVoidBoots()
        );
        sendGiveMessage(player, "Voided Armor");
    }

    private void giveAssaultArmor(Player player) {
        player.getInventory().addItem(
                AssaultArmor.CrearAssaultHelmet(),
                AssaultArmor.crearAssaultChestplate(),
                AssaultArmor.crearAssaultLeggings(),
                AssaultArmor.crearAssaultBoots()
        );
        sendGiveMessage(player, "Voided Assault Armor");
    }

    private void giveGuardianArmor(Player player) {
        player.getInventory().addItem(
                GuardianArmor.CrearGuardianHelmet(),
                GuardianArmor.CrearGuardianChestplate(),
                GuardianArmor.CrearGuardianLeggings(),
                GuardianArmor.CrearGuardianBoots()
        );
        sendGiveMessage(player, "Voided Guardian Armor");
    }

    private void giveVoidedTools(Player player) {
        player.getInventory().addItem(
                VoidedItems.CrearVoidedPick(),
                VoidedItems.CrearVoidedSword()
        );
        sendGiveMessage(player, "Voided Tools");
    }

    private void giveVoidedIngot(Player player) {
        player.getInventory().addItem(VoidedItems.CrearVoidedIngot());
        sendGiveMessage(player, "Voided Ingot");
    }

    private void giveVoidedShard(Player player) {
        player.getInventory().addItem(VoidedItems.CrearVoidedShard());
        sendGiveMessage(player, "Voided Shard");
    }

    private void giveDirtyHearty(Player player) {
        player.getInventory().addItem(PendulumItems.CrearDirtyHearty());
        sendGiveMessage(player, "Dirty Hearty");
    }

    private void giveOroDoble(Player player) {
        for (int i = 0; i < 64; i++) {
            player.getInventory().addItem(PendulumItems.CrearOroDoble());
        }
        sendGiveMessage(player, "Oro Doble");
    }

    private void sendGiveMessage(Player player, String itemName) {
        player.sendMessage(MessageUtils.colorMessage("&aSe te añadió x1 " + itemName));
    }

    @Override
    public boolean requiresPermission() {
        return true;
    }
}