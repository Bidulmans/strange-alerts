package fr.bidulman.strangealerts;

import fr.bidulman.strangealerts.utils.Object;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StrangeAlerts extends JavaPlugin implements Listener {

    private Configuration config;
    private Map<UUID, Long> lastAlerts;

    @Override
    public void onEnable() {
        config = new Configuration(this);
        lastAlerts = new HashMap<>();
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void alert(String baseMessage, Object object, Player player, Location location) {
        boolean canChat = lastAlerts.get(player.getUniqueId()) == null || System.currentTimeMillis() - lastAlerts.get(player.getUniqueId()) > config.CHAT_COOLDOWN;

        BaseComponent[] titleMessage = TextComponent.fromLegacyText(formatMessage(baseMessage, object, player, location));

        TextComponent chatMessage = new TextComponent(config.CHAT_ALERT
                .replace("{PLAYER}", player.getName())
                .replace("{LOCATION}", formatLocation(location))
        );
        chatMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getUniqueId()));
        chatMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(config.CHAT_HOVER)));

        getServer().getOnlinePlayers().forEach(target -> {
            if (!target.hasPermission("strangealerts.view")) {
                return;
            }

            target.spigot().sendMessage(ChatMessageType.ACTION_BAR, titleMessage);

            if (!canChat) {
                return;
            }

            target.spigot().sendMessage(chatMessage);
        });

        lastAlerts.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public String formatMessage(String baseMessage, Object object, Player player, Location location) {
        return (baseMessage
                .replace("{MATERIAL}", object.getNick())
                .replace("{PLAYER}", player.getName())
                .replace("{LOCATION}", formatLocation(location))
        );
    }

    public String formatLocation(Location location) {
        return (config.LOCATION_DISPLAY
                .replace("{WORLD}", location.getWorld().getName())
                .replace("{X}",String.valueOf(location.getX()))
                .replace("{Y}", String.valueOf(location.getY()))
                .replace("{Z}", String.valueOf(location.getZ()))
        );
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (!config.BREAK_OBJECTS.containsMaterial(event.getBlock().getType())) {
            return;
        }

        alert(config.BREAK_MESSAGE, config.BREAK_OBJECTS.getObject(event.getBlock().getType()), event.getPlayer(), event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (!config.PLACE_OBJECTS.containsMaterial(event.getBlock().getType())) {
            return;
        }

        alert(config.PLACE_MESSAGE, config.PLACE_OBJECTS.getObject(event.getBlock().getType()), event.getPlayer(), event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!config.EMPTY_OBJECTS.containsMaterial(event.getBucket())) {
            return;
        }

        alert(config.EMPTY_MESSAGE, config.EMPTY_OBJECTS.getObject(event.getBucket()), event.getPlayer(), event.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null || !config.RIGHT_CLICK_OBJECTS.containsMaterial(event.getItem().getType())) {
            return;
        }

        alert(config.RIGHT_CLICK_MESSAGE, config.RIGHT_CLICK_OBJECTS.getObject(event.getItem().getType()), event.getPlayer(), event.getClickedBlock().getLocation());
    }

}
