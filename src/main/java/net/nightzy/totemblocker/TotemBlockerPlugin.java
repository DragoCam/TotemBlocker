package net.nightzy.totemblocker;

import net.nightzy.totemblocker.handlers.NoTotemHandler;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import org.bukkit.plugin.java.JavaPlugin;

public class TotemBlockerPlugin extends JavaPlugin {

    public static StateFlag NO_TOTEM_FLAG;

    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        try {
            NO_TOTEM_FLAG = new StateFlag("no-totem", false);
            registry.register(NO_TOTEM_FLAG);
        } catch (FlagConflictException e) {
            var existing = registry.get("no-totem");
            if (existing instanceof StateFlag) {
                NO_TOTEM_FLAG = (StateFlag) existing;
            }
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new GithubUpdateChecker(this, "DragoCam/TotemBlocker").check();
        getServer().getPluginManager().registerEvents(
                new NoTotemHandler(this),
                this
        );
    }
}


