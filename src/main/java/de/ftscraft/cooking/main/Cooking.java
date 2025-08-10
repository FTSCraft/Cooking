package de.ftscraft.cooking.main;

import de.ftscraft.cooking.debug.CookingDebugCommand;
import de.ftscraft.cooking.gui.CookingDevice;
import de.ftscraft.cooking.listeners.BlockListener;
import de.ftscraft.cooking.listeners.CraftingListener;
import de.ftscraft.cooking.manager.CookingItemManager;
import de.ftscraft.cooking.misc.Misc;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Cooking extends JavaPlugin {

    private static Cooking instance;

    private final HashMap<Location, CookingDevice> cookingDevices = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        Misc.init();
        new CookingItemManager();

        new CraftingListener(this);
        new BlockListener(this);

        getCommand("cooking").setExecutor(new CookingDebugCommand());
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

}
