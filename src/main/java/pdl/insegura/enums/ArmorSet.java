// ArmorSet.java
package pdl.insegura.enums;

import org.bukkit.Material;
import pdl.insegura.items.customs.netherite.AgileNetherite;
import pdl.insegura.items.customs.netherite.ReinforcedNetherite;
import pdl.insegura.items.customs.voided.VoidedArmor;

public enum ArmorSet {
    AGILE(
            Material.NETHERITE_HELMET,      AgileNetherite.helmetName,
            Material.NETHERITE_CHESTPLATE, AgileNetherite.chestplateName,
            Material.NETHERITE_LEGGINGS,   AgileNetherite.leggingsName,
            Material.NETHERITE_BOOTS,      AgileNetherite.bootsName,
            "armors/agile"
    ),
    REINFORCED(
            Material.NETHERITE_HELMET,      ReinforcedNetherite.helmetName,
            Material.NETHERITE_CHESTPLATE, ReinforcedNetherite.chestplateName,
            Material.NETHERITE_LEGGINGS,   ReinforcedNetherite.leggingsName,
            Material.NETHERITE_BOOTS,      ReinforcedNetherite.bootsName,
            "armors/reinforced"
    ),
    VOIDED(
            Material.NETHERITE_HELMET,      VoidedArmor.helmetName,
            Material.NETHERITE_CHESTPLATE, VoidedArmor.chestplateName,
            Material.NETHERITE_LEGGINGS,   VoidedArmor.leggingsName,
            Material.NETHERITE_BOOTS,      VoidedArmor.bootsName,
            "armors/voided"
    );

    // Campos del enum: materiales, nombres y la clave del advancement
    private final Material helmetMaterial;
    private final String helmetName;
    private final Material chestMaterial;
    private final String chestName;
    private final Material leggingsMaterial;
    private final String leggingsName;
    private final Material bootsMaterial;
    private final String bootsName;
    private final String advancementKey;

    ArmorSet(Material helmetMaterial, String helmetName,
             Material chestMaterial, String chestName,
             Material leggingsMaterial, String leggingsName,
             Material bootsMaterial, String bootsName,
             String advancementKey) {
        this.helmetMaterial = helmetMaterial;
        this.helmetName = helmetName;
        this.chestMaterial = chestMaterial;
        this.chestName = chestName;
        this.leggingsMaterial = leggingsMaterial;
        this.leggingsName = leggingsName;
        this.bootsMaterial = bootsMaterial;
        this.bootsName = bootsName;
        this.advancementKey = advancementKey;
    }

    public Material getHelmetMaterial()      { return helmetMaterial; }
    public String   getHelmetName()          { return helmetName; }
    public Material getChestMaterial()       { return chestMaterial; }
    public String   getChestName()           { return chestName; }
    public Material getLeggingsMaterial()    { return leggingsMaterial; }
    public String   getLeggingsName()        { return leggingsName; }
    public Material getBootsMaterial()       { return bootsMaterial; }
    public String   getBootsName()          { return bootsName; }
    public String   getAdvancementKey()      { return advancementKey; }
}
