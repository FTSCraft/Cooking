package de.ftscraft.cooking.main;

import de.ftscraft.cooking.debug.CookingDebugCommand;
import de.ftscraft.cooking.gui.CookingDevice;
import de.ftscraft.cooking.listeners.BlockListener;
import de.ftscraft.cooking.listeners.CraftingListener;
import de.ftscraft.cooking.manager.CookingItemManager;
import de.ftscraft.cooking.misc.Misc;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Cooking extends JavaPlugin {

    public static final boolean DEBUG = false;

    private static Cooking instance;

    private final HashMap<Location, CookingDevice> cookingDevices = new HashMap<>();

    private CoreProtectAPI coreProtectAPI;

    @Override
    public void onEnable() {
        instance = this;

        Misc.init();
        new CookingItemManager();

        new CraftingListener(this);
        new BlockListener(this);

        if (DEBUG) {
            getCommand("cooking").setExecutor(new CookingDebugCommand());
        }

        hookIntoCoreProtect();
    }

    private void hookIntoCoreProtect() {
        Plugin plugin = getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (!(plugin instanceof CoreProtect coreProtect)) {
            coreProtectAPI = null;
            return;
        }

        // Check that the API is enabled
        CoreProtectAPI coreProtectAPI = coreProtect.getAPI();
        if (!coreProtectAPI.isEnabled()) {
            this.coreProtectAPI = null;
            return;
        }

        this.coreProtectAPI = coreProtectAPI;
    }

    @Override
    public void onDisable() {
        for (CookingDevice device : cookingDevices.values()) {
            device.dropItems();
        }
    }

    public void disable(String reason) {
        getLogger().severe("Disabling plugin because of " + reason);
        getServer().getPluginManager().disablePlugin(this);
    }

    public static Cooking getInstance() {
        return instance;
    }

    public HashMap<Location, CookingDevice> getCookingDevices() {
        return cookingDevices;
    }

    public CoreProtectAPI getCoreProtectAPI() {
        return coreProtectAPI;
    }
}
