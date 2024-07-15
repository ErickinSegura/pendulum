package pdl.insegura.structures;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import pdl.insegura.PendulumPlugin;
import pdl.insegura.listeners.mobs.customs.VoidedKnight;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

public class StructureGenerator implements Listener {

    private final PendulumPlugin plugin;

    public StructureGenerator(PendulumPlugin plugin) {
        this.plugin = plugin;
    }
    private final Random random = new Random();

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        // Check if the world is the Overworld
        if (event.getWorld().getEnvironment() != Environment.NORMAL) {
            return; // Do nothing if it's not the Overworld
        }

        Chunk chunk = event.getChunk();

        // Probability of 1/50 to generate a floating island
        if (random.nextInt(500) == 0) {
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


        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }


}
