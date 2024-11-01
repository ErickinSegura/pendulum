package pdl.insegura.utils;

import org.bukkit.Material;

public class Reto {
    private String titulo;
    private int cantidad;
    private Material material;

    public Reto(String titulo, int cantidad, Material material) {
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.material = material;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Material getMaterial() {
        return material;
    }
}
