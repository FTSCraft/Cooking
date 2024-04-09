package de.ftscraft.cooking.misc;

import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.ftsutils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class Misc {

    public static ItemStack PROCESS_GREEN, PROCESS_RED, FILL, GRAY_FILL;
    public static NamespacedKey DEVICE_TYPE_KEY;
    public static NamespacedKey DEVICE_DURABILITY_KEY;
    public static final int DURABILITY = 150;

    public static void init() {
        PROCESS_GREEN = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).sign("fill").name(" ").build();
        PROCESS_RED = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).sign("fill").name(" ").build();
        FILL = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).sign("fill").name(" ").build();
        GRAY_FILL = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).sign("fill").name(" ").build();


        DEVICE_TYPE_KEY = new NamespacedKey(Cooking.getInstance(), "DEVICE_TYPE");
        DEVICE_DURABILITY_KEY = new NamespacedKey(Cooking.getInstance(), "DEVICE_DURABILITY");
    }

}
