package de.ftscraft.cooking.manager;

import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.cooking.misc.CookingItem;
import de.ftscraft.cooking.misc.CustomCookingRecipe;
import de.ftscraft.ftsutils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;

public class CookingItemManager {

    private static final ArrayList<CustomCookingRecipe> customCookingRecipes = new ArrayList<>();

    public CookingItemManager() {
        initItems();
        initCrafting();
    }

    private void initItems() {

        CookingItem.COOKING_POT.setItem(
                new ItemBuilder(Material.CAULDRON)
                        .name("§6Kochtopf")
                        .sign("COOKING_" + CookingItem.COOKING_POT).build()
        );

        CookingItem.OVEN.setItem(
                new ItemBuilder(Material.FURNACE)
                        .name("§6Ofen")
                        .sign("COOKING_OVEN")
                        .build()
        );

        CookingItem.SALT.setItem(
                new ItemBuilder(Material.SUGAR)
                        .name("§fSalz")
                        .sign("COOKING_SALT")
                        .build()
        );

        CookingItem.FLOUR.setItem(
                new ItemBuilder(Material.BONE_MEAL)
                        .name("§7Mehl")
                        .sign("COOKING_" + CookingItem.FLOUR)
                        .build()
        );

        CookingItem.BREAD.setItem(
                new ItemBuilder(Material.BREAD)
                        .sign("COOKING_" + CookingItem.BREAD)
                        .build()
        );

        CookingItem.HONEY_MELON_JUICE.setItem(
                new ItemBuilder(Material.HONEY_BOTTLE)
                        .name("§6Honigmelonensaft")
                        .sign("COOKING_" + CookingItem.HONEY_MELON_JUICE)
                        .build()
        );

        CookingItem.COOKIE_DOUG.setItem(
                new ItemBuilder(Material.CLAY_BALL)
                        .name("§7Keksteig")
                        .sign("COOKING_" + CookingItem.COOKIE_DOUG)
                        .build()
        );

        CookingItem.COOKIE.setItem(
                new ItemBuilder(Material.COOKIE)
                        .sign("COOKING_COOKIE")
                        .build()
        );

        CookingItem.PUMPKIN_PIE_DOUG.setItem(
                new ItemBuilder(Material.CLAY_BALL)
                        .name("§7Kürbiskuchenteig")
                        .sign("COOKING_" + CookingItem.PUMPKIN_PIE_DOUG)
                        .build()
        );

        CookingItem.PUMPKIN_PIE.setItem(
                new ItemBuilder(Material.PUMPKIN_PIE)
                        .sign("COOKING_" + CookingItem.PUMPKIN_PIE)
                        .build()
        );

        CookingItem.SWEET_FISH.setItem(
                new ItemBuilder(Material.TROPICAL_FISH)
                        .name("§6Süßer Fisch")
                        .sign("COOKING_" + CookingItem.SWEET_FISH)
                        .build()
        );

        CookingItem.CLEAN_WATER.setItem(
                new ItemBuilder(Material.POTION)
                        .name("§1Klares Wasser")
                        .sign("COOKING_" + CookingItem.CLEAN_WATER)
                        .build()
        );

        CookingItem.RABBIT_STEW.setItem(
                new ItemBuilder(Material.RABBIT_STEW)
                        .sign("COOKING_" + CookingItem.RABBIT_STEW)
                        .build()
        );

        CookingItem.SWEET_JAM.setItem(
                new ItemBuilder(Material.HONEY_BOTTLE)
                        .name("§6Süße Marmelade")
                        .sign("COOKING_" + CookingItem.SWEET_JAM)
                        .build()
        );

        CookingItem.FISH_SOUP.setItem(
                new ItemBuilder(Material.MUSHROOM_STEW)
                        .name("§6Fischsuppe")
                        .sign("COOKING_" + CookingItem.FISH_SOUP)
                        .build()
        );

        CookingItem.BEETROOT_SOUP.setItem(
                new ItemBuilder(Material.BEETROOT_SOUP)
                        .sign("COOKING_" + CookingItem.BEETROOT_SOUP)
                        .build()
        );

        CookingItem.MISO_SOUP.setItem(
                new ItemBuilder(Material.MUSHROOM_STEM)
                        .name("§6Miso Suppe")
                        .sign("COOKING_" + CookingItem.MISO_SOUP)
                        .build()
        );

        CookingItem.SUSHI.setItem(
                new ItemBuilder(Material.DRIED_KELP)
                        .name("§6Sushi")
                        .sign("COOKING_SUSHI")
                        .build()
        );

    }

    private void initCrafting() {

        // Delete all recipes which need to be overwritten
        Bukkit.removeRecipe(Material.BREAD.getKey());
        Bukkit.removeRecipe(Material.COOKIE.getKey());
        Bukkit.removeRecipe(Material.PUMPKIN_PIE.getKey());
        Bukkit.removeRecipe(Material.RABBIT_STEW.getKey());
        Bukkit.removeRecipe(Material.BEETROOT_SOUP.getKey());

        {
            ShapedRecipe cookingPotRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_POT"), CookingItem.COOKING_POT.getItem());
            cookingPotRecipe.shape("CAC", "CIC", "AAA");
            cookingPotRecipe.setIngredient('A', Material.AIR);
            cookingPotRecipe.setIngredient('C', Material.COPPER_INGOT);
            cookingPotRecipe.setIngredient('I', Material.IRON_BLOCK);
            Bukkit.addRecipe(cookingPotRecipe);
        }

        {
            ShapedRecipe ovenRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_OVEN"), CookingItem.OVEN.getItem());
            ovenRecipe.shape("IAI", "ICI", "AAA");
            ovenRecipe.setIngredient('A', Material.AIR);
            ovenRecipe.setIngredient('I', Material.IRON_INGOT);
            ovenRecipe.setIngredient('C', Material.COPPER_INGOT);
            Bukkit.addRecipe(ovenRecipe);
        }

        {
            ShapedRecipe flourRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_FLOUR"), CookingItem.FLOUR.getItem());
            flourRecipe.shape("SWS", "WWW", "SWS");
            flourRecipe.setIngredient('S', Material.WHEAT_SEEDS);
            flourRecipe.setIngredient('W', Material.WHEAT);
            Bukkit.addRecipe(flourRecipe);
        }

        {
            CustomCookingRecipe breadRecipe = new CustomCookingRecipe(CookingItem.BREAD);
            breadRecipe.shape("AAA", "FFF", "AAA");
            breadRecipe.setIngredient('A', Material.AIR);
            breadRecipe.setIngredient('F', CookingItem.FLOUR.getItem());
            customCookingRecipes.add(breadRecipe);
        }

        {
            ShapedRecipe cookieDougRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_COOKIE_DOUG"), CookingItem.COOKIE_DOUG.getItem());
            cookieDougRecipe.shape("CCC", "AFA", "SPS");
            cookieDougRecipe.setIngredient('C', Material.COCOA_BEANS);
            cookieDougRecipe.setIngredient('A', Material.AIR);
            cookieDougRecipe.setIngredient('F', CookingItem.FLOUR.getItem());
            cookieDougRecipe.setIngredient('S', Material.SUGAR);
            cookieDougRecipe.setIngredient('P', CookingItem.CLEAN_WATER.getItem());
            Bukkit.addRecipe(cookieDougRecipe);
        }

        {
            CustomCookingRecipe cookieRecipe = new CustomCookingRecipe(CookingItem.COOKIE);
            cookieRecipe.shape("AAA", "ADA", "AAA");
            cookieRecipe.setIngredient('A', Material.AIR);
            cookieRecipe.setIngredient('D', CookingItem.COOKIE_DOUG.getItem());
            customCookingRecipes.add(cookieRecipe);
        }

        {
            CustomCookingRecipe honeyMelonJuiceRecipe = new CustomCookingRecipe(CookingItem.HONEY_MELON_JUICE);
            honeyMelonJuiceRecipe.shape("MMM", "HWH", "AAA");
            honeyMelonJuiceRecipe.setIngredient('M', Material.MELON_SLICE);
            honeyMelonJuiceRecipe.setIngredient('H', Material.HONEYCOMB);
            honeyMelonJuiceRecipe.setIngredient('W', Material.HONEY_BOTTLE);
            honeyMelonJuiceRecipe.setIngredient('A', Material.AIR);
            customCookingRecipes.add(honeyMelonJuiceRecipe);
        }

    }

    public static ArrayList<CustomCookingRecipe> getCustomCookingRecipes() {
        return customCookingRecipes;
    }
}
