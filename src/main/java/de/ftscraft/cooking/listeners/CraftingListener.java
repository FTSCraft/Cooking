package de.ftscraft.cooking.listeners;

import com.jeff_media.customblockdata.CustomBlockData;
import de.ftscraft.cooking.gui.CookingDevice;
import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.cooking.misc.CookingItem;
import de.ftscraft.cooking.misc.Misc;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.HashMap;

public class CraftingListener implements Listener {

    Cooking plugin;
    private static final int RESULT_SLOT = 25;

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
    public void onCraftItem(CraftItemEvent event) {
        if (event.getInventory().getType() != InventoryType.WORKBENCH)
            return;

        ItemStack result = event.getRecipe().getResult();
        Material type = result.getType();
        if (type == Material.HONEY_BOTTLE) {
            event.setCancelled(true);
            ItemStack cursor = event.getView().getCursor();
            // Wenn die Hand weder leer ist noch genug Platz hat für für ein weiteres Craftingergebnis, skip
            if (!cursor.isEmpty() && (!cursor.isSimilar(result) || cursor.getAmount() + result.getAmount() > result.getMaxStackSize())) {
                return;
            }
            ItemStack[] matrix = event.getInventory().getMatrix();
            for (ItemStack item : matrix) {
                if (item != null) {
                    item.setAmount(item.getAmount() - 1);
                }
            }

            event.getInventory().setMatrix(matrix);
            ItemStack resultClone = result.clone();
            resultClone.setAmount(result.getAmount() + event.getView().getCursor().getAmount());
            event.getView().setCursor(resultClone);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        // Check if inventory is the inv of a cooking device
        if (event.getInventory().getHolder() == null) {
            return;
        }
        if (!(event.getInventory().getHolder() instanceof CookingDevice cookingDevice)) {
            return;
        }
        if (cookingDevice.getStatus() == CookingDevice.CookingStatus.NONE) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> checkForRecipe(cookingDevice), 1);
        } else event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        // Check if inventory is the inv of a cooking device
        CookingDevice cookingDevice = getCookingDevice(event);
        if (cookingDevice == null)
            return;

        {
            String cursorSign = ItemReader.getSign(event.getCursor());
            if (cookingDevice.isDisabled() ||
                    (event.getClick() == ClickType.DOUBLE_CLICK && (cursorSign != null && cursorSign.startsWith("COOKING")))) {
                event.setCancelled(true);
                return;
            }
        }

        // If clicked on a fill item, cancel
        if (checkClickedOnFillItem(event))
            return;

        // If clicked on anything while cooking, cancel.
        // Also, when clicking on something which is not the result after it's finished cooking
        if (clickedWhileCookingOrInWrongSlot(event, cookingDevice))
            return;

        if (event.getSlot() == RESULT_SLOT) {
            // When clicking on the result while it's not cooking nor done, start cooking
            if (cookingDevice.getStatus() == CookingDevice.CookingStatus.NONE) {
                // if clicking on result while it's done cooking, do nothing
                startCooking(event, cookingDevice);
            } else if (cookingDevice.getStatus() == CookingDevice.CookingStatus.DONE) {
                if (event.getAction() != InventoryAction.PICKUP_ALL) {
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage("§7Du musst die Items aus dem Ofen/Kochtopf mit leerer Hand nehmen.");
                    return;
                }
                event.setCancelled(false);
                Bukkit.getScheduler().runTaskLater(Cooking.getInstance(), () -> {
                    cookingDevice.reset();
                    checkForRecipe(cookingDevice);
                    reduceDurability(cookingDevice);
                }, 1);
                return;
            }
        }

        checkForRecipeDelayed(cookingDevice);
    }

    @Nullable
    private CookingDevice getCookingDevice(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() == null) {
            return null;
        }
        if (!(event.getClickedInventory().getHolder() instanceof CookingDevice cookingDevice)) {
            if (event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof CookingDevice) {
                if (event.getClick() == ClickType.DOUBLE_CLICK)
                    event.setCancelled(true);
            }
            return null;
        }
        return cookingDevice;
    }

    private void startCooking(InventoryClickEvent event, CookingDevice cookingDevice) {
        ItemStack result = event.getInventory().getItem(RESULT_SLOT);
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
            event.setCancelled(true);
            return;
        }
        ItemStack coalStack = cookingDevice.getInventory().getItem(CookingDevice.COAL_SLOT);
        if (coalStack == null ||
                coalStack.getType() != Material.COAL ||
                coalStack.getAmount() < Misc.getCoalAmount(result.getAmount())) {
            event.setCancelled(true);
            return;
        }
        cookingDevice.startCooking(item);
        event.setCancelled(true);
    }

    private void checkForRecipeDelayed(CookingDevice cookingDevice) {
        if (cookingDevice.getStatus() == CookingDevice.CookingStatus.NONE) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> checkForRecipe(cookingDevice), 1);
        }
    }

    private boolean checkClickedOnFillItem(InventoryClickEvent event) {
        if (event.getCurrentItem() != null) {
            String sign = ItemReader.getSign(event.getCurrentItem());
            if (sign != null && sign.equals("fill")) {
                event.setCancelled(true);
                return true;
            }
        }
        return false;
    }

    private boolean clickedWhileCookingOrInWrongSlot(InventoryClickEvent event, CookingDevice cookingDevice) {
        if (cookingDevice.getStatus() == CookingDevice.CookingStatus.COOKING ||
                (cookingDevice.getStatus() == CookingDevice.CookingStatus.DONE && event.getSlot() != RESULT_SLOT)) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    private void reduceDurability(CookingDevice cookingDevice) {
        CustomBlockData customBlockData = new CustomBlockData(cookingDevice.getLocation().getBlock(), plugin);
        Integer currentDurability = customBlockData.get(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER);
        if (currentDurability == null) {
            return;
        }
        currentDurability -= 1;
        if (currentDurability == 0) {
            customBlockData.clear();
            customBlockData.getBlock().setType(Material.AIR);
            cookingDevice.dropItems();
            cookingDevice.disable();
            plugin.getCookingDevices().remove(cookingDevice.getLocation());
        } else {
            customBlockData.set(Misc.DEVICE_DURABILITY_KEY, PersistentDataType.INTEGER, currentDurability);
        }
    }

    private void checkForRecipe(CookingDevice cookingDevice) {
        CookingItem item = cookingDevice.checkForRecipe();
        if (item == null) {
            cookingDevice.getInventory().setItem(25, Misc.GRAY_FILL);
            return;
        }
        ItemStack itemStack = item.getItem().clone();
        itemStack.setAmount(cookingDevice.getCookAmount());
        cookingDevice.getInventory().setItem(25, itemStack);
    }

}
