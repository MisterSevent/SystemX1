package com.system.events;

import com.system.apis.NBTItemAPI;
import com.system.inventorys.ArenasInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerClickEvent implements Listener {

    @EventHandler
    public void aoClicar(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory inventory = e.getInventory();
        ItemStack i = e.getCurrentItem();
        String name = inventory.getName();
        if (i != null) {
            int currentSlot;
            Inventory i2;
            if (name.contains("§1§2§3§8")) {
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 18:
                        currentSlot = this.currentSlot(i);
                        p.closeInventory();
                        i2 = new ArenasInventory(currentSlot - 1, p).getInventory();
                        if (currentSlot == -1) {
                            return;
                        }
                        if (i2 == null) {
                            p.sendMessage("§cOcorreu um erro. Tente novamente mais tarde!");
                            p.closeInventory();
                            return;
                        }
                        p.openInventory(i2);
                        break;
                    case 26:
                        currentSlot = this.currentSlot(i);
                        i2 = new ArenasInventory(currentSlot + 1, p).getInventory();
                        if (currentSlot == -1) {
                            return;
                        }
                        if (i2 == null) {
                            p.sendMessage("§cOcorreu um erro. Tente novamente mais tarde!");
                            p.closeInventory();
                            return;
                        }
                        p.openInventory(i2);
                }
            }

        }
    }
    int currentSlot(ItemStack clicked) {
        String keySlot = NBTItemAPI.getNBTString(clicked.getItemMeta(), "items_inventory");
        if (keySlot != null && !keySlot.isEmpty()) {
            int currentSlot = Integer.parseInt(keySlot);
            return currentSlot;
        } else {
            return -1;
        }
    }
}

