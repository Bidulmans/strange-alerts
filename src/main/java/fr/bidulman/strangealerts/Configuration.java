package fr.bidulman.strangealerts;

import fr.bidulman.strangealerts.utils.Object;
import fr.bidulman.strangealerts.utils.ObjectList;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class Configuration {

    public final int CHAT_COOLDOWN;

    public final String CHAT_ALERT;
    public final String CHAT_HOVER;
    public final String LOCATION_DISPLAY;
    
    public final String BREAK_MESSAGE;
    public final ObjectList BREAK_OBJECTS;

    public final String PLACE_MESSAGE;
    public final ObjectList PLACE_OBJECTS;

    public final String EMPTY_MESSAGE;
    public final ObjectList EMPTY_OBJECTS;

    public final String RIGHT_CLICK_MESSAGE;
    public final ObjectList RIGHT_CLICK_OBJECTS;

    public Configuration(StrangeAlerts plugin) {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        CHAT_COOLDOWN = config.getInt("chat-cooldown") * 1000;

        CHAT_ALERT = config.getString("strings.chat-alert");
        CHAT_HOVER = config.getString("strings.chat-hover");
        LOCATION_DISPLAY = config.getString("strings.location-display");

        BREAK_MESSAGE = config.getString("actions.break.message");
        BREAK_OBJECTS = new ObjectList();
        for (Map<?, ?> object : config.getMapList("actions.break.objects")) {
            BREAK_OBJECTS.addObject(new Object(Material.valueOf((String) object.get("material")), (String) object.get("nick")));
        }

        PLACE_MESSAGE = config.getString("actions.place.message");
        PLACE_OBJECTS = new ObjectList();
        for (Map<?, ?> object : config.getMapList("actions.place.objects")) {
            PLACE_OBJECTS.addObject(new Object(Material.valueOf((String) object.get("material")), (String) object.get("nick")));
        }

        EMPTY_MESSAGE = config.getString("actions.empty.message");
        EMPTY_OBJECTS = new ObjectList();
        for (Map<?, ?> object : config.getMapList("actions.empty.objects")) {
            EMPTY_OBJECTS.addObject(new Object(Material.valueOf((String) object.get("material")), (String) object.get("nick")));
        }

        RIGHT_CLICK_MESSAGE = config.getString("actions.right-click.message");
        RIGHT_CLICK_OBJECTS = new ObjectList();
        for (Map<?, ?> object : config.getMapList("actions.right-click.objects")) {
            RIGHT_CLICK_OBJECTS.addObject(new Object(Material.valueOf((String) object.get("material")), (String) object.get("nick")));
        }
    }
    
}
