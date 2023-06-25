package fr.bidulman.strangealerts.utils;

import org.bukkit.Material;

public class Object {

    private final Material material;
    private final String nick;

    public Object(Material material, String nick) {
        this.material = material;
        this.nick = nick;
    }

    public Material getMaterial() {
        return material;
    }

    public String getNick() {
        return nick;
    }

}
