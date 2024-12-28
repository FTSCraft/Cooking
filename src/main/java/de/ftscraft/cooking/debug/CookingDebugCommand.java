package de.ftscraft.cooking.debug;

import de.ftscraft.cooking.misc.CookingItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CookingDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!(cs instanceof Player p)) {
            return true;
        }

        if (!cs.hasPermission("cooking.debug")) {
            cs.sendMessage("Dafür hast du keine rechte!");
            return true;
        }

        Inventory inv = Bukkit.createInventory(null, 9 * 5);
        for (CookingItem cookingItem : CookingItem.values()) {
            inv.addItem(cookingItem.getItem().clone());
        }
        p.openInventory(inv);
        return false;
    }
}
