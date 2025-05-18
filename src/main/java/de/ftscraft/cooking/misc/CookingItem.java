package de.ftscraft.cooking.misc;

import org.bukkit.inventory.ItemStack;

public enum CookingItem {

    //Non-Craftable
    COOKING_POT(CookingDeviceType.NONE, -1),
    OVEN(CookingDeviceType.NONE, -1),
    SALT(CookingDeviceType.NONE, -1),

    //Craftable
    SUGAR(CookingDeviceType.COOKING_POT, 3),
    CLEAR_WATER(CookingDeviceType.NONE, -1),

    //Default food
    COOKED_COD(CookingDeviceType.OVEN, 5),
    COOKED_SALMON(CookingDeviceType.OVEN, 5),
    COOKED_PORKCHOP(CookingDeviceType.OVEN, 5),
    COOKED_BEEF(CookingDeviceType.OVEN, 5),
    COOKED_CHICKEN(CookingDeviceType.OVEN, 5),
    COOKED_MUTTON(CookingDeviceType.OVEN, 5),
    COOKED_RABBIT(CookingDeviceType.OVEN, 5),
    BAKED_POTATO(CookingDeviceType.OVEN, 5),
    MUSHROOM_STEW(CookingDeviceType.COOKING_POT, 5),
    RABBIT_STEW(CookingDeviceType.COOKING_POT, 30),
    BEETROOT_SOUP(CookingDeviceType.COOKING_POT, 30),
    PUMPKIN_PIE(CookingDeviceType.OVEN, 10),
    COOKIE(CookingDeviceType.OVEN, 10),

    //Plugin food/items
    CANDIED_PUMPKIN(CookingDeviceType.NONE, -1),
    BLACK_CHOCOLATE(CookingDeviceType.NONE, -1),
    FLOUR(CookingDeviceType.NONE, -1),
    BREAD(CookingDeviceType.OVEN, 5),
    COOKIE_DOUG(CookingDeviceType.NONE, -1),
    PUMPKIN_PIE_DOUG(CookingDeviceType.NONE, -1),
    SWEET_FISH(CookingDeviceType.OVEN, 20),
    SWEET_JAM(CookingDeviceType.COOKING_POT, 45),
    FISH_SOUP(CookingDeviceType.COOKING_POT, 30),
    MISO_SOUP(CookingDeviceType.COOKING_POT, 30),
    SUSHI(CookingDeviceType.NONE, -1),
    CACTUS_JUICE(CookingDeviceType.COOKING_POT, 5),
    PUMPKIN_SOUP(CookingDeviceType.NONE, -1),
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
