package com.system.inventorys;

import com.system.apis.ItemBuilder;
import com.system.apis.NBTItemAPI;
import com.system.apis.PaginationAPI;
import com.system.apis.SkullManager;
import com.system.enums.GameStatus;
import com.system.managers.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static com.system.SystemX1.*;

public class ArenasInventory {

    Inventory inventory;
    private static List<Integer> slots;
    private Arena arena;

    List<ItemStack> items = new ArrayList<>();

    public ArenasInventory(int page, Player p) {

        for (String locations : getLocations().getConfig().getConfigurationSection("Arenas").getKeys(false)) {
            ItemStack itemStack = SkullManager.getSkull(getConfiguration().getConfig().getString("Skull-url"));
            SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
            skullMeta.setDisplayName("§aArena §e"+getLocations().getConfig().getString("Arenas."+locations+".Name"));
            List<String> lore =  new ArrayList<>();
            String arena1 = getLocations().getConfig().getString("Arenas."+locations+".Name");
            getInstance().getGame().registrarArena(arena1);
            arena = getInstance().getGame().getArena(arena1);
            if (arena.getStatus() == GameStatus.IN_GAME) {
                lore.add("");
                lore.add("§7Status: §cEm Andamento!");
                lore.add("");
                lore.add("§aClique para ver mais informações");
            }else if (arena.getStatus() == GameStatus.PENDING) {
                lore.add("");
                lore.add("§7Status: §eSolicitação Pendente!");
                lore.add("");
            }else{
                lore.add("");
                lore.add("§7Status: §aDisponível!");
                lore.add("");
            }
            skullMeta.setLore(lore);
            itemStack.setItemMeta(skullMeta);
            items.add(itemStack);

        }
        int slots = items.size() <= 5 ? 3 : (items.size() <= 10 ? 4 : (items.size() <= 15 ? 5 : 6));
        PaginationAPI pagination = new PaginationAPI(slots == 3 ? 5 : (slots == 4 ? 10 : (slots == 5 ? 15 : 20)), items);
        inventory = Bukkit.createInventory(null, slots * 9, "§1§2§3§8 §8Arenas #" + String.valueOf(page + 1));

        if (pagination.exists(page)) {
            int currentSlot = 10;
            Iterator var1 = pagination.getPage(page).iterator();
            while (var1.hasNext()) {
                ItemStack itemStack;
                for (itemStack = (ItemStack) var1.next(); !getSlots().contains(currentSlot); ++currentSlot) {
                }
                inventory.setItem(currentSlot++, itemStack);
            }
            ItemStack nextpage = (new ItemBuilder(Material.ARROW, 1, (short) 0).setDisplayName("§aPróxima página").setLore("§7Clique para ir para a " + (page + 2) + "° página").addEnchantment(Enchantment.DAMAGE_ALL, 1).hideAllFlags()).build();
            ItemStack backpage = (new ItemBuilder(Material.ARROW, 1, (short) 0).setDisplayName("§aPágina anterior").setLore("§7Clique para ir para a " + page + "° página").addEnchantment(Enchantment.DAMAGE_ALL, 1).hideAllFlags()).build();
            if (pagination.exists(page - 1)) {
                NBTItemAPI.setNBTString(backpage.getItemMeta(), "items_inventory", String.valueOf(page));
                inventory.setItem(18, backpage);
            }
            if (pagination.exists(page + 1)) {
                NBTItemAPI.setNBTString(nextpage.getItemMeta(), "items_inventory", String.valueOf(page));
                inventory.setItem(26, nextpage);
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    private static List<Integer> getSlots() {
        if (slots == null) {
            slots = new ArrayList();
            int[] var1 = new int[]{11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                int i = var1[var3];
                slots.add(i);
            }
        }
        return slots;
    }

    public Inventory informacoesArena(Player p, String nome) {
        Inventory inv = Bukkit.createInventory(null, 9, "§7Informações Arena: §e"+nome);
        getInstance().getGame().registrarArena(nome);
        this.arena = getInstance().getGame().getArena(nome);
        Player desafiador = Bukkit.getPlayer((UUID)this.arena.getDesafiador().get(0)).getPlayer();
        Player desafiado = Bukkit.getPlayer((UUID)this.arena.getDesafiado().get(0)).getPlayer();
        ItemStack cabecaDesafiador = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta cabecaMeta = (SkullMeta)cabecaDesafiador.getItemMeta();
        cabecaMeta.setOwner(desafiador.getName());
        cabecaMeta.setDisplayName(desafiador.getName());
        ArrayList<String> loreCabecaDesafiador = new ArrayList();
        loreCabecaDesafiador.add("§aDesafiador");
        cabecaMeta.setLore(loreCabecaDesafiador);
        cabecaDesafiador.setItemMeta(cabecaMeta);
        ItemStack cabecaDesafiado = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta cabecaMetaD = (SkullMeta)cabecaDesafiado.getItemMeta();
        cabecaMetaD.setOwner(desafiado.getName());
        cabecaMetaD.setDisplayName(desafiado.getName());
        ArrayList<String> loreCabecaDesafiado = new ArrayList();
        loreCabecaDesafiado.add("§aDesafiado");
        cabecaMetaD.setLore(loreCabecaDesafiado);
        cabecaDesafiado.setItemMeta(cabecaMetaD);
        ItemStack versus = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta versusMeta = versus.getItemMeta();
        versusMeta.setDisplayName("§cContra");
        versus.setItemMeta(versusMeta);
        inv.addItem(new ItemStack[]{cabecaDesafiador});
        inv.setItem(4, versus);
        inv.setItem(8, cabecaDesafiado);
        p.openInventory(inv);
        return inv;
    }
}
