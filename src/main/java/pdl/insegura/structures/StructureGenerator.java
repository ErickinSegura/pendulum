package pdl.insegura.structures;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.commands.PendulumCommand;
import pdl.insegura.utils.MessageUtils;
import pdl.insegura.utils.PendulumSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class StructureGenerator implements Listener {

    private final PendulumPlugin plugin;
    private final Random random = new Random();

    public StructureGenerator(PendulumPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        if (PendulumSettings.getInstance().getDia() <= 10) {
            return;
        }
        // Check if the world is the Overworld
        if (event.getWorld().getEnvironment() != Environment.NORMAL) {
            return; // Do nothing if it's not the Overworld
        }

        Chunk chunk = event.getChunk();

        // Probability of 1/500 to generate a floating island
        if (random.nextInt(1000) == 0) {
            generateFloatingIsland(chunk);
        }
    }

    private void generateFloatingIsland(Chunk chunk) {
        // Get the central coordinates of the chunk
        int x = chunk.getX() << 4; // chunk.getX() * 16
        int z = chunk.getZ() << 4; // chunk.getZ() * 16
        int y = 100 + random.nextInt(50); // Height between 100 and 150

        Location location = new Location(chunk.getWorld(), x + 8, y, z + 8);

        // Load and paste the floating island structure
        File schemsDir = new File("plugins/Pendulum/Schems");
        if (!schemsDir.exists()) {
            schemsDir.mkdirs(); // Create the directory if it doesn't exist
        }

        File schematic = new File(PendulumPlugin.getInstance().getDataFolder(), "void.schem");
        if (!schematic.exists()) {
            System.out.println("Schematic file not found: " + schematic.getPath());
            return;
        }

        try (ClipboardReader reader = ClipboardFormats.findByFile(schematic).getReader(new FileInputStream(schematic))) {
            Clipboard clipboard = reader.read();
            World adaptedWorld = BukkitAdapter.adapt(chunk.getWorld());
            BlockVector3 pasteLocation = BlockVector3.at(location.getX(), location.getY(), location.getZ());

            ClipboardHolder holder = new ClipboardHolder(clipboard);
            Operations.complete(holder.createPaste(adaptedWorld).to(pasteLocation).ignoreAirBlocks(false).build());

            Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dFloating island generated at " + location));

            // Buscamos la Blackstone Brick Slab para spawnear al Voided Knight
            Location spawnLocation = findBlackstoneBrickSlabLocation(location, clipboard);
            if (spawnLocation != null) {
                PendulumCommand voidedKnight = new PendulumCommand();
                voidedKnight.spawnVoidedKnightCMD(spawnLocation);
                Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dVoided Knight spawned at " + spawnLocation));
            } else {
                Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dNo Blackstone Brick Slab found"));
            }

        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }

    private Location findBlackstoneBrickSlabLocation(Location baseLocation, Clipboard clipboard) {
        BlockVector3 clipboardOrigin = clipboard.getOrigin();
        Region region = clipboard.getRegion();
        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dBase Location: " + baseLocation));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dClipboard Origin: " + clipboardOrigin));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dRegion Min: " + min));
        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dRegion Max: " + max));

        for (int y = min.getY(); y <= max.getY(); y++) {
            for (int x = min.getX(); x <= max.getX(); x++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    BlockVector3 checkVector = BlockVector3.at(x, y, z);
                    BlockVector3 relativeLocationVector = checkVector.subtract(clipboardOrigin);
                    Location relativeLocation = baseLocation.clone().add(relativeLocationVector.getX(), relativeLocationVector.getY(), relativeLocationVector.getZ());

                    Block block = relativeLocation.getBlock();

                    // Skip checking if the block is air
                    if (block.getType() == Material.AIR) {
                        continue;
                    }

                    if (block.getType() == Material.BLACKSTONE_SLAB) {
                        Block above = relativeLocation.clone().add(0, 1, 0).getBlock();

                        if (above.getType() == Material.AIR) {
                            Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dFound BLACKSTONE_SLAB at: " + relativeLocation));
                            return above.getLocation().add(0.5, 0, 0.5); // Centro del bloque
                        }
                    }
                }
            }
        }

        Bukkit.getConsoleSender().sendMessage(MessageUtils.colorMessage("&dNo BLACKSTONE_SLAB found in clipboard."));
        return null;
    }



}
