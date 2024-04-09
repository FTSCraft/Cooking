package de.ftscraft.cooking.misc;

import org.bukkit.inventory.ItemStack;

public enum CookingItem {

    //Non-Craftable
    COOKING_POT(CookingDeviceType.NONE, -1),
    OVEN(CookingDeviceType.NONE, -1),
    SALT(CookingDeviceType.NONE, -1),


    FLOUR(CookingDeviceType.NONE, -1),
    BREAD(CookingDeviceType.OVEN, 5),
    COOKIE_DOUG(CookingDeviceType.NONE, -1),
    COOKIE(CookingDeviceType.OVEN, 10),
    PUMPKIN_PIE_DOUG(CookingDeviceType.NONE, -1),
    PUMPKIN_PIE(CookingDeviceType.OVEN, 10),
    SWEET_FISH(CookingDeviceType.OVEN, 20),
    CLEAN_WATER(CookingDeviceType.COOKING_POT, 5),
    RABBIT_STEW(CookingDeviceType.COOKING_POT, 30),
    SWEET_JAM(CookingDeviceType.COOKING_POT, 45),
    FISH_SOUP(CookingDeviceType.COOKING_POT, 30),
    BEETROOT_SOUP(CookingDeviceType.COOKING_POT, 30),
    MISO_SOUP(CookingDeviceType.COOKING_POT, 30),
    SUSHI(CookingDeviceType.NONE, -1),
    HONEY_MELON_JUICE(CookingDeviceType.COOKING_POT, 5);

    private ItemStack item;
    private final CookingDeviceType cookingDeviceType;

    //represented in seconds
    private final int cookingTime;

    CookingItem(CookingDeviceType cookingDeviceType, int cookingTime) {
        this.cookingDeviceType = cookingDeviceType;
        this.cookingTime = cookingTime;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public CookingDeviceType getCookingDeviceType() {
        return cookingDeviceType;
    }

    public int getCookingTime() {
        return cookingTime;
    }
}
