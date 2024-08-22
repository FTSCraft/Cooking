package de.ftscraft.cooking.manager;

import de.ftscraft.cooking.main.Cooking;
import de.ftscraft.cooking.misc.CookingItem;
import de.ftscraft.cooking.misc.CustomCookingRecipe;
import de.ftscraft.ftsutils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.HashSet;
import java.util.Set;

public class CookingItemManager {

    private static final Set<CustomCookingRecipe> customCookingRecipes = new HashSet<>();

    public CookingItemManager() {
        initItems();
        initCrafting();
    }

    private void initItems() {

        initVanillaReplaceItem(CookingItem.SUGAR);
        initVanillaReplaceItem(CookingItem.BREAD);
        initVanillaReplaceItem(CookingItem.COOKIE);
        initVanillaReplaceItem(CookingItem.COOKED_COD);
        initVanillaReplaceItem(CookingItem.COOKED_SALMON);
        initVanillaReplaceItem(CookingItem.COOKED_PORKCHOP);
        initVanillaReplaceItem(CookingItem.COOKED_BEEF);
        initVanillaReplaceItem(CookingItem.COOKED_CHICKEN);
        initVanillaReplaceItem(CookingItem.COOKED_MUTTON);
        initVanillaReplaceItem(CookingItem.COOKED_RABBIT);
        initVanillaReplaceItem(CookingItem.BAKED_POTATO);
        initVanillaReplaceItem(CookingItem.MUSHROOM_STEW);
        initVanillaReplaceItem(CookingItem.PUMPKIN_PIE);
        initVanillaReplaceItem(CookingItem.BEETROOT_SOUP);
        initVanillaReplaceItem(CookingItem.RABBIT_STEW);

        CookingItem.COOKING_POT.setItem(
                new ItemBuilder(Material.CAULDRON)
                        .name("§6Kochtopf")
                        .sign("COOKING_" + CookingItem.COOKING_POT).build()
        );

        CookingItem.OVEN.setItem(
                new ItemBuilder(Material.FURNACE)
                        .name("§r§6Ofen")
                        .sign("COOKING_OVEN")
                        .build()
        );

        CookingItem.SALT.setItem(
                new ItemBuilder(Material.SUGAR)
                        .name("§r§fSalz")
                        .sign("COOKING_SALT")
                        .build()
        );

        CookingItem.FLOUR.setItem(
                new ItemBuilder(Material.BONE_MEAL)
                        .name("§r§7Mehl")
                        .sign("COOKING_" + CookingItem.FLOUR)
                        .build()
        );

        CookingItem.HONEY_MELON_JUICE.setItem(
                new ItemBuilder(Material.HONEY_BOTTLE)
                        .name("§r§6Honigmelonensaft")
                        .sign("COOKING_" + CookingItem.HONEY_MELON_JUICE)
                        .build()
        );

        CookingItem.COOKIE_DOUG.setItem(
                new ItemBuilder(Material.CLAY_BALL)
                        .name("§r§7Keksteig")
                        .sign("COOKING_" + CookingItem.COOKIE_DOUG)
                        .build()
        );

        CookingItem.PUMPKIN_PIE_DOUG.setItem(
                new ItemBuilder(Material.CLAY_BALL)
                        .name("§r§7Kürbiskuchenteig")
                        .sign("COOKING_" + CookingItem.PUMPKIN_PIE_DOUG)
                        .build()
        );

        CookingItem.SWEET_FISH.setItem(
                new ItemBuilder(Material.TROPICAL_FISH)
                        .name("§r§6Süßer Fisch")
                        .sign("COOKING_" + CookingItem.SWEET_FISH)
                        .build()
        );

        CookingItem.SWEET_JAM.setItem(
                new ItemBuilder(Material.HONEY_BOTTLE)
                        .name("§r§6Süße Marmelade")
                        .sign("COOKING_" + CookingItem.SWEET_JAM)
                        .build()
        );

        CookingItem.FISH_SOUP.setItem(
                new ItemBuilder(Material.MUSHROOM_STEW)
                        .name("§r§6Fischsuppe")
                        .sign("COOKING_" + CookingItem.FISH_SOUP)
                        .build()
        );

        CookingItem.MISO_SOUP.setItem(
                new ItemBuilder(Material.MUSHROOM_STEM)
                        .name("§r§6Miso Suppe")
                        .sign("COOKING_" + CookingItem.MISO_SOUP)
                        .build()
        );

        CookingItem.SUSHI.setItem(
                new ItemBuilder(Material.DRIED_KELP)
                        .name("§r§6Sushi")
                        .sign("COOKING_SUSHI")
                        .build()
        );

        CookingItem.SUGAR.setItem(
                new ItemBuilder(Material.SUGAR)
                        .sign("COOKING_SUGAR")
                        .build()
        );

    }

    private void initVanillaReplaceItem(CookingItem item) {
        Material material;
        try {
            material = Material.valueOf(item.name());
        } catch (IllegalArgumentException e) {
            Cooking.getInstance().disable("Wasn't able to find material when initializing item " + item + ".");
            throw new RuntimeException(e);
        }
        item.setItem(new ItemBuilder(material).sign("COOKING_" + item).build());
        for (Recipe recipe : Bukkit.getRecipesFor(new ItemStack(material))) {
            if (recipe instanceof Keyed keyed)
                Bukkit.removeRecipe(keyed.getKey());
        }
    }

    private void initCrafting() {

        {
            ShapedRecipe cookingPotRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_POT"), CookingItem.COOKING_POT.getItem());
            cookingPotRecipe.shape("CAC", "CIC", "AAA");
            cookingPotRecipe.setIngredient('C', Material.COPPER_INGOT);
            cookingPotRecipe.setIngredient('I', Material.IRON_BLOCK);
            Bukkit.addRecipe(cookingPotRecipe);
        }

        {
            ShapedRecipe ovenRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_OVEN"), CookingItem.OVEN.getItem());
            ovenRecipe.shape("IAI", "ICI", "AAA");
            ovenRecipe.setIngredient('I', Material.IRON_INGOT);
            ovenRecipe.setIngredient('C', Material.COPPER_BLOCK);
            Bukkit.addRecipe(ovenRecipe);
        }

        /* ----------------------------------------------- */

        {
            CustomCookingRecipe sugarRecipe = new CustomCookingRecipe(CookingItem.SUGAR);
            sugarRecipe.shape("AAA", "ASA", "AAA");
            sugarRecipe.setIngredient('A', Material.AIR);
            sugarRecipe.setIngredient('S', Material.SUGAR_CANE);
            customCookingRecipes.add(sugarRecipe);
        }

        /* ----------------------------------------------- */

        {
            ShapedRecipe flourRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_FLOUR"), CookingItem.FLOUR.getItem());
            flourRecipe.shape("SWS", "WWW", "SWS");
            flourRecipe.setIngredient('S', Material.WHEAT_SEEDS);
            flourRecipe.setIngredient('W', Material.WHEAT);
            Bukkit.addRecipe(flourRecipe);
        }

        {
            ShapedRecipe cookieDougRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_COOKIE_DOUG"), CookingItem.COOKIE_DOUG.getItem());
            cookieDougRecipe.shape("CCC", "AFA", "SPS");
            cookieDougRecipe.setIngredient('C', Material.COCOA_BEANS);
            cookieDougRecipe.setIngredient('F', CookingItem.FLOUR.getItem());
            cookieDougRecipe.setIngredient('S', Material.SUGAR);
            cookieDougRecipe.setIngredient('P', Material.POTION);
            Bukkit.addRecipe(cookieDougRecipe);
        }

        {
            ShapedRecipe pumpkinPieRecipe = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_PUMPKIN_PIE_DOUG"), CookingItem.PUMPKIN_PIE_DOUG.getItem());
            pumpkinPieRecipe.shape("PBP", "EWE", "SWS");
            pumpkinPieRecipe.setIngredient('P', CookingItem.FLOUR.getItem());
            pumpkinPieRecipe.setIngredient('B', Material.BONE_MEAL);
            pumpkinPieRecipe.setIngredient('E', Material.EGG);
            pumpkinPieRecipe.setIngredient('S', Material.SUGAR);
            pumpkinPieRecipe.setIngredient('W', Material.POTION);
            Bukkit.addRecipe(pumpkinPieRecipe);
        }

        /* ----------------------------------------------- */

        {
            CustomCookingRecipe breadRecipe = new CustomCookingRecipe(CookingItem.BREAD);
            breadRecipe.shape("AAA", "FFF", "AAA");
            breadRecipe.setIngredient('A', Material.AIR);
            breadRecipe.setIngredient('F', CookingItem.FLOUR.getItem());
            customCookingRecipes.add(breadRecipe);
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
            honeyMelonJuiceRecipe.setIngredient('W', Material.POTION);
            honeyMelonJuiceRecipe.setIngredient('A', Material.AIR);
            customCookingRecipes.add(honeyMelonJuiceRecipe);
        }

        {
            CustomCookingRecipe sweetFishRecipe = new CustomCookingRecipe(CookingItem.SWEET_FISH);
            sweetFishRecipe.shape("AAA", "SSS", "XSY");
            sweetFishRecipe.setIngredient('Y', Material.SALMON);
            sweetFishRecipe.setIngredient('X', Material.COD);
            sweetFishRecipe.setIngredient('S', Material.SUGAR);
            sweetFishRecipe.setIngredient('A', Material.AIR);
            customCookingRecipes.add(sweetFishRecipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.PUMPKIN_PIE);
            recipe.shape("AAA", "ADA", "AAA");
            recipe.setIngredient('D', CookingItem.PUMPKIN_PIE_DOUG.getItem());
            recipe.setIngredient('A', Material.AIR);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.RABBIT_STEW);
            recipe.shape("SRS", "CPC", "XWY");
            recipe.setIngredient('S', CookingItem.SALT.getItem());
            recipe.setIngredient('R', Material.RABBIT);
            recipe.setIngredient('C', Material.CARROT);
            recipe.setIngredient('P', Material.POTATO);
            recipe.setIngredient('X', Material.BROWN_MUSHROOM);
            recipe.setIngredient('Y', Material.RED_MUSHROOM);
            recipe.setIngredient('W', Material.POTION);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.SWEET_JAM);
            recipe.shape("BGB", "BSB", "SWS");
            recipe.setIngredient('B', Material.SWEET_BERRIES);
            recipe.setIngredient('G', Material.GLOW_BERRIES);
            recipe.setIngredient('S', Material.SUGAR);
            recipe.setIngredient('W', Material.POTION);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.FISH_SOUP);
            recipe.shape("CKS", "CKS", "TWT");
            recipe.setIngredient('C', Material.COD);
            recipe.setIngredient('K', Material.KELP);
            recipe.setIngredient('S', Material.SALMON);
            recipe.setIngredient('T', CookingItem.SALT.getItem());
            recipe.setIngredient('W', Material.POTION);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.BEETROOT_SOUP);
            recipe.shape("BBB", "BBB", "SWS");
            recipe.setIngredient('B', Material.BEETROOT);
            recipe.setIngredient('S', CookingItem.SALT.getItem());
            recipe.setIngredient('W', Material.POTION);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.MISO_SOUP);
            recipe.shape("AKA", "SWS", "WPW");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('S', CookingItem.SALT.getItem());
            recipe.setIngredient('W', Material.SEAGRASS);
            recipe.setIngredient('P', Material.POTION);
            recipe.setIngredient('K', Material.KELP);
            customCookingRecipes.add(recipe);
        }

        {
            ShapedRecipe sushi = new ShapedRecipe(new NamespacedKey(Cooking.getInstance(), "COOKING_SUSHI"), CookingItem.SUSHI.getItem());
            sushi.shape("ASK", "XKY", "KAA");
            sushi.setIngredient('S', CookingItem.SALT.getItem());
            sushi.setIngredient('K', Material.DRIED_KELP);
            sushi.setIngredient('Y', Material.SALMON);
            sushi.setIngredient('X', Material.COD);
            Bukkit.addRecipe(sushi);
        }

        /* ----------------------------------------------- */

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_COD);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.COD);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_SALMON);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.SALMON);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_PORKCHOP);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.PORKCHOP);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_BEEF);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.BEEF);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_CHICKEN);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.CHICKEN);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_MUTTON);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.MUTTON);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.BAKED_POTATO);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.POTATO);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.COOKED_RABBIT);
            recipe.shape("AAA", "AXA", "AAA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('X', Material.RABBIT);
            customCookingRecipes.add(recipe);
        }

        {
            CustomCookingRecipe recipe = new CustomCookingRecipe(CookingItem.MUSHROOM_STEW);
            recipe.shape("AAA", "RBA", "ASA");
            recipe.setIngredient('A', Material.AIR);
            recipe.setIngredient('R', Material.RED_MUSHROOM);
            recipe.setIngredient('B', Material.BROWN_MUSHROOM);
            recipe.setIngredient('S', Material.BOWL);
            customCookingRecipes.add(recipe);
        }

    }

    public static Set<CustomCookingRecipe> getCustomCookingRecipes() {
        return customCookingRecipes;
    }
}
