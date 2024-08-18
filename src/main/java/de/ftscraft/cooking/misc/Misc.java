package de.ftscraft.cooking.misc;

import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public class Misc {

    public static ItemStack PROCESS_GREEN, PROCESS_RED, FILL, GRAY_FILL;
    public static NamespacedKey DEVICE_TYPE_KEY;
    public static NamespacedKey DEVICE_DURABILITY_KEY;
    public static final int DURABILITY = 150 * 2;

    public static void init() {
        PROCESS_GREEN = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).sign("fill").name(" ").build();
        PROCESS_RED = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).sign("fill").name(" ").build();
        FILL = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).sign("fill").name(" ").build();
        GRAY_FILL = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).sign("fill").name(" ").build();


        DEVICE_TYPE_KEY = new NamespacedKey(Cooking.getInstance(), "DEVICE_TYPE");
        DEVICE_DURABILITY_KEY = new NamespacedKey(Cooking.getInstance(), "DEVICE_DURABILITY");
    }

    public static boolean isItemSimilar(ItemStack item, ItemStack similar) {
        if (item == null || similar == null) return false;
        if (item == similar) return true;
        if (item.getType() != similar.getType()) return false;
        String sign1 = ItemReader.getSign(item);
        String sign2 = ItemReader.getSign(similar);
        if (sign1 == null && sign2 == null) return true;
        if (sign1 == null || sign2 == null) return false;
        return sign1.equals(sign2);
    }

    public static int getCoalAmount(int itemAmount) {
        return (itemAmount - 1) / 16 + 1;
    }

}
