package eu.bidulaxstudio.strangealerts.utils;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class ObjectList {

    private final Map<Material, Object> objects;

    public ObjectList() {
        objects = new HashMap<>();
    }

    public boolean containsMaterial(Material material) {
        return objects.containsKey(material);
    }

    public void addObject(Object object) {
        objects.put(object.getMaterial(), object);
    }

    public Object getObject(Material material) {
        return objects.get(material);
    }

}
