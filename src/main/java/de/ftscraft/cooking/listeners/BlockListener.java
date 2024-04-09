package de.ftscraft.cooking.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.ftscraft.cooking.gui.CookingDevice;
import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.cooking.misc.CookingDeviceType;
import de.ftscraft.cooking.misc.CookingItem;
import de.ftscraft.cooking.misc.Misc;
import de.ftscraft.ftsutils.items.ItemBuilder;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockListener implements Listener {

    private final Cooking plugin;

    public BlockListener(Cooking plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {

        ItemStack placedBlock = event.getPlayer().getInventory().getItem(event.getHand());
        String sign = ItemReader.getSign(placedBlock);

        // Wenn wir ein Block mit haltbarkeit haben (Ein Block, der vorher genutzt wurde und abgebaut wurde), mit alter
        // Haltbarkeit neu setzen
        Integer durability = ItemReader.getPDC(placedBlock, "DURABILITY", PersistentDataType.INTEGER);
        if (durability == null) {
            durability = Misc.DURABILITY;
        }

        if (sign != null && sign.equals("COOKING_" + CookingItem.COOKING_POT)) {

            PersistentDataContainer dc = new CustomBlockData(event.getBlock(), plugin);
            dc.set(Misc.DEVICE_TYPE_KEY, PersistentDataType.STRING, CookingDeviceType.COOKING_POT.name());
            dc.set(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER, durability);

        } else if (sign != null && sign.equals("COOKING_" + CookingItem.OVEN)) {

            PersistentDataContainer dc = new CustomBlockData(event.getBlock(), plugin);
            dc.set(Misc.DEVICE_TYPE_KEY, PersistentDataType.STRING, CookingDeviceType.OVEN.name());
            dc.set(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER, durability);

        }

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {

        if (event.getBlock().getType() == Material.DIORITE) {
            if (plugin.getCoreProtectAPI().blockLookup(event.getBlock(), Integer.MAX_VALUE).isEmpty()) {
                if (Math.random() < 0.05) {
                    event.setCancelled(true);
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), CookingItem.SALT.getItem());
                    event.getBlock().setType(Material.AIR);
                }
            }
            return;
        }

        CustomBlockData blockData = new CustomBlockData(event.getBlock(), plugin);
        if (blockData.has(Misc.DEVICE_TYPE_KEY)) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            String deviceType = blockData.get(Misc.DEVICE_TYPE_KEY, PersistentDataType.STRING);
            CookingItem cookingDeviceItem;

            if (deviceType.equals("OVEN"))
                cookingDeviceItem = CookingItem.OVEN;
            else if (deviceType.equals("COOKING_POT"))
                cookingDeviceItem = CookingItem.COOKING_POT;
            else
                return;

            ItemStack item = new ItemBuilder(cookingDeviceItem.getItem())
                    .addPDC(
                            "DURABILITY",
                            blockData.get(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER),
                            PersistentDataType.INTEGER)
                    .build();

            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), item);

            // Wenn das Inv im Cache ist, Items droppen und aus dem Cache entfernen
            if (plugin.getCookingDevices().containsKey(event.getBlock().getLocation())) {
                plugin.getCookingDevices().remove(event.getBlock().getLocation()).dropItems();
            }

            blockData.clear();
        }

    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }

        PersistentDataContainer dc = new CustomBlockData(event.getClickedBlock(), plugin);
        if (dc.has(Misc.DEVICE_TYPE_KEY)) {
            Location blockLocation = event.getClickedBlock().getLocation();
            if (plugin.getCookingDevices().containsKey(blockLocation)) {
                event.getPlayer().openInventory(plugin.getCookingDevices().get(blockLocation).getInventory());
            } else {
                CookingDevice device = new CookingDevice(CookingDeviceType.valueOf(dc.get(Misc.DEVICE_TYPE_KEY, PersistentDataType.STRING)), blockLocation);
                event.getPlayer().openInventory(device.getInventory());
            }

            event.setCancelled(true);

        }

    }

}
