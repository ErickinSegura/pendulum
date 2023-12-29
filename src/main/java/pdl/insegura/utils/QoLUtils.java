package pdl.insegura.utils;

import org.bukkit.Bukkit;

public class QoLUtils {
    public void comando(String comando) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), comando);
    }

}
