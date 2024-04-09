package de.ftscraft.cooking.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.ftscraft.cooking.gui.CookingDevice;
import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.cooking.misc.CookingItem;
import de.ftscraft.cooking.misc.Misc;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CraftingListener implements Listener {

    Cooking plugin;

    public CraftingListener(Cooking plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraftingPrepare(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null)
            return;
        if (event.getInventory().getType() != InventoryType.WORKBENCH)
            return;
        ItemStack result = event.getRecipe().getResult();
        String sign = ItemReader.getSign(result);
        if (sign == null)
            return;
        if (sign.startsWith("COOKING")) {
            if (!event.getViewers().get(0).hasPermission("cooking.skill")) {
                event.getInventory().setResult(null);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Check if inventory is the inv of a cooking device
        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() == null) {
            return;
        }
        if (!(event.getClickedInventory().getHolder() instanceof CookingDevice cookingDevice)) {
            if (event.getClick() == ClickType.DOUBLE_CLICK)
                event.setCancelled(true);
            return;
        }
        {
            String cursorSign = ItemReader.getSign(event.getCursor());
            if (cookingDevice.isDisabled() || (event.getClick() == ClickType.DOUBLE_CLICK && (cursorSign != null && cursorSign.startsWith("COOKING")))) {
                event.setCancelled(true);
                return;
            }
        }
        // If clicked on a fill item, cancel
        {
            if (event.getCurrentItem() != null) {
                String sign = ItemReader.getSign(event.getCurrentItem());
                if (sign != null && sign.equals("fill")) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        // If clicked on anything while cooking, cancel. Also, when clicking on something which is not the result after it's finished cooking
        if (cookingDevice.getStatus() == CookingDevice.CookingStatus.COOKING || (cookingDevice.getStatus() == CookingDevice.CookingStatus.DONE && event.getSlot() != 25)) {
            event.setCancelled(true);
            return;
        }

        if (event.getSlot() == 25) {
            // When clicking on the result while it is not cooking nor done, start cooking
            if (cookingDevice.getStatus() == CookingDevice.CookingStatus.NONE) {
                ItemStack result = event.getInventory().getItem(25);
                if (result == null) {
                    event.setCancelled(true);
                    return;
                }
                String sign = ItemReader.getSign(result);
                if (sign == null || !sign.startsWith("COOKING")) {
                    event.setCancelled(true);
                    return;
                }
                //Das Item holen
                sign = sign.replaceFirst("COOKING_", "");
                CookingItem item;
                try {
                    item = CookingItem.valueOf(sign);
                } catch (IllegalArgumentException e) {
                    return;
                }
                cookingDevice.startCooking(item);
                event.setCancelled(true);
                // if clicking on result while it's done cooking, don't do anything
            } else if (cookingDevice.getStatus() == CookingDevice.CookingStatus.DONE) {
                event.setCancelled(false);
                Bukkit.getScheduler().runTaskLater(Cooking.getInstance(), () -> {
                    cookingDevice.reset();
                    checkForRecipe(cookingDevice);
                    CustomBlockData customBlockData = new CustomBlockData(cookingDevice.getLocation().getBlock(), plugin);
                    int dur = customBlockData.get(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER) - 1;
                    if (dur == 0) {
                        customBlockData.clear();
                        customBlockData.getBlock().setType(Material.AIR);
                        cookingDevice.dropItems();
                        cookingDevice.disable();
                        plugin.getCookingDevices().remove(cookingDevice.getLocation());
                    } else {
                        customBlockData.set(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER, dur);
                    }
                }, 1);
                return;
            }
        }

        //Das hier ist nur für das Crafting für das Ergebnis muss man dafür eine if Abfrage über den Slot machen
        if (cookingDevice.getStatus() == CookingDevice.CookingStatus.NONE) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> checkForRecipe(cookingDevice), 1);
        }
    }

    private void checkForRecipe(CookingDevice cookingDevice) {
        CookingItem item = cookingDevice.checkForRecipe();
        if (item == null) {
            cookingDevice.getInventory().setItem(25, Misc.GRAY_FILL);
            return;
        }
        cookingDevice.getInventory().setItem(25, item.getItem());
    }

}
