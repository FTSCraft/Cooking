package de.ftscraft.cooking.gui;

import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.cooking.manager.CookingItemManager;
import de.ftscraft.cooking.misc.CookingDeviceType;
import de.ftscraft.cooking.misc.CookingItem;
import de.ftscraft.cooking.misc.CustomCookingRecipe;
import de.ftscraft.cooking.misc.Misc;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CookingDevice implements InventoryHolder {

    private final Inventory inventory;
    private final CookingDeviceType deviceType;
    private final Location location;
    private boolean disabled = false;
    private CookingStatus status;

    public CookingDevice(CookingDeviceType type, Location location) {
        this.location = location;
        String name;
        switch (type) {
            case COOKING_POT -> name = "Kochtopf";
            case OVEN -> name = "Ofen";
            default -> name = "???";
        }
        this.inventory = Bukkit.createInventory(this, 9 * 5, Component.text(name));
        this.deviceType = type;
        this.status = CookingStatus.NONE;
        fillInventory();
        Cooking.getInstance().getCookingDevices().put(location, this);
    }

    public void startCooking(CookingItem item) {

        if (status != CookingStatus.NONE)
            return;

        removeItems();

        CookingRunner cookingRunner = new CookingRunner(item);
        cookingRunner.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Cooking.getInstance(), cookingRunner, 0, 20);

    }

    private void removeItems() {
        for (int i = 10; i < 31; i++) {
            ItemStack is = inventory.getItem(i);

            if (is != null)
                is.setAmount(is.getAmount() - 1);

            if (i == 12 || i == 21)
                i += 6;
        }
    }

    public CookingItem checkForRecipe() {

        for (CustomCookingRecipe recipe : CookingItemManager.getCustomCookingRecipes()) {

            if (checkForRightRecipe(recipe)) {
                return recipe.getResult();
            }

        }

        return null;

    }

    private boolean checkForRightRecipe(CustomCookingRecipe recipe) {

        if (recipe.getResult().getCookingDeviceType() != deviceType)
            return false;

        String shape = recipe.getShapeString();

        // 10, 11, 12,       19, 20, 21,        28, 29, 30
        int s = 0;
        for (int i = 10; i < 31; i++) {
            char c = shape.charAt(s++);
            ItemStack is = inventory.getItem(i);
            if (is == null)
                is = new ItemStack(Material.AIR);
            if (!Misc.isItemSimilar(is, recipe.getIngredientMap().get(c)))
                return false;

            if (i == 12 || i == 21)
                i += 6;
        }

        return true;
    }

    public void reset() {
        if (status == CookingStatus.DONE) {
            status = CookingStatus.NONE;
            inventory.setItem(22, Misc.FILL);
            inventory.setItem(23, Misc.FILL);
            inventory.setItem(24, Misc.FILL);
        }
    }

    public void dropItems() {
        location.getChunk().load();
        for (int i = 10; i < 31; i++) {
            ItemStack is = inventory.getItem(i);
            if (is != null)
                location.getWorld().dropItem(location, is);
            if (i == 12 || i == 21)
                i += 6;
        }
    }

    private void fillInventory() {

        for (int i = 0; i < 9 * 5; i++) {
            inventory.setItem(i, Misc.FILL);
        }
        for (int i = 10; i < 31; i++) {
            inventory.setItem(i, null);
            if (i == 12 || i == 21)
                i += 6;
        }
        inventory.setItem(25, Misc.GRAY_FILL);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public CookingStatus getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public void disable() {
        disabled = true;
        for (int i = 0; i < 9 * 5; i++) {
            inventory.setItem(i, Misc.PROCESS_RED);
        }
    }

    public boolean isDisabled() {
        return disabled;
    }

    public enum CookingStatus {
        NONE, COOKING, DONE
    }

    private class CookingRunner implements Runnable {

        private int taskId;

        private final CookingItem result;

        private int currentCookingTime = 0;
        public CookingRunner(CookingItem result) {
            status = CookingStatus.COOKING;
            this.result = result;
        }
        @Override
        public void run() {

            if (currentCookingTime == result.getCookingTime()) {
                Bukkit.getScheduler().cancelTask(taskId);
                status = CookingStatus.DONE;
                inventory.setItem(24, Misc.PROCESS_GREEN);
                return;
            }

            float percentage = ((float) currentCookingTime) / result.getCookingTime();

            if (percentage > .5f) {
                inventory.setItem(23, Misc.PROCESS_GREEN);
            } else {
                inventory.setItem(22, Misc.PROCESS_GREEN);
            }
            currentCookingTime++;
        }

    }

}
