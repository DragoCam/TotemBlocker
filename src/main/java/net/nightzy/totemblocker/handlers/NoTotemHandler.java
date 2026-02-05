package net.nightzy.totemblocker.handlers;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import net.nightzy.totemblocker.TotemBlockerPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class NoTotemHandler implements Listener {

    private final TotemBlockerPlugin plugin;

    public NoTotemHandler(TotemBlockerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTotem(EntityResurrectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.getHand() == null) return;

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();

        boolean denied = query.testState(
                BukkitAdapter.adapt(player.getLocation()),
                null,
                TotemBlockerPlugin.NO_TOTEM_FLAG
        ) == false;

        if (denied) {
            event.setCancelled(true);
            player.sendMessage(plugin.getConfig().getString("message"));
        }
    }
}
