package de.ftscraft.cooking.misc;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CustomCookingRecipe {

    Map<Character, ItemStack> items = new HashMap<>();
    String shapeString;
    CookingItem result;

    public CustomCookingRecipe(CookingItem result) {
        this.result = result;
    }

    public CookingItem getResult() {
        return result;
    }

    public Map<Character, ItemStack> getIngredientMap() {
        return items;
    }

    public void setIngredient(char c, ItemStack item) {
        items.put(c, item);
    }
    public void setIngredient(char c, Material mat) {
        items.put(c, new ItemStack(mat));
    }

    public void shape(String... shape) {
        this.shapeString = shape[0] + shape[1] + shape[2];
    }

    public String getShapeString() {
        return shapeString;
    }
}
