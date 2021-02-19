package com.system.inventorys;

import com.system.SystemX1;
import com.system.managers.DatabasePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class DadosInventory {
    private DatabasePlayer playerGame;
    private SystemX1 plugin;

    public DadosInventory(SystemX1 systemX1) {
        this.plugin = systemX1;
    }

    public void menuDados(OfflinePlayer jogador, Player p) {
        this.playerGame = this.plugin.game.getPlayerGame(jogador.getUniqueId());

        Inventory inventory = Bukkit.createInventory(null, 9*3, "Dados de "+jogador.getName());
        ItemStack cabeca = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta cabecaMeta = (SkullMeta) cabeca.getItemMeta();
        cabecaMeta.setOwner(jogador.getName());
        cabecaMeta.setDisplayName("§a" + jogador.getName());
        ArrayList<String> loreCabeca = new ArrayList<>();
        loreCabeca.add("§7Perfil de §f" + jogador.getName() + "§7.");
        cabecaMeta.setLore(loreCabeca);
        cabeca.setItemMeta(cabecaMeta);

        ItemStack vitorias = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta vitoriasMeta = vitorias.getItemMeta();
        vitoriasMeta.setDisplayName("§eVítorias");
        ArrayList<String> loreVitorias = new ArrayList<>();
        loreVitorias.add("§7Vítorias: §a" + this.playerGame.getVitorias());
        vitoriasMeta.setLore(loreVitorias);
        vitorias.setItemMeta(vitoriasMeta);

        ItemStack derrotas = new ItemStack(Material.SKULL_ITEM, 1, (short)1);
        ItemMeta derrotasMetas = derrotas.getItemMeta();
        derrotasMetas.setDisplayName("§eDerrotas");
        ArrayList<String> loreDerrotas = new ArrayList<>();
        loreDerrotas.add("§7Derrotas: §a" + this.playerGame.getDerrotas());
        derrotasMetas.setLore(loreDerrotas);
        derrotas.setItemMeta(derrotasMetas);

        ItemStack kills = new ItemStack(Material.MAGMA_CREAM, 1, (short)0);
        ItemMeta killsMetas = kills.getItemMeta();
        killsMetas.setDisplayName("§eAbates");
        ArrayList<String> killDerrotas = new ArrayList<>();
        killDerrotas.add("§7Abates: §a" + this.playerGame.getKills());
        killsMetas.setLore(killDerrotas);
        kills.setItemMeta(killsMetas);

        ItemStack mortes = new ItemStack(Material.FERMENTED_SPIDER_EYE, 1, (short)0);
        ItemMeta mortesMetas = mortes.getItemMeta();
        mortesMetas.setDisplayName("§eMortes");
        ArrayList<String> loreMortes = new ArrayList<>();
        loreMortes.add("§7Mortes: §a" + this.playerGame.getMortes());
        mortesMetas.setLore(loreMortes);
        mortes.setItemMeta(mortesMetas);

        ItemStack status;
        ItemMeta statusMeta;
        if (jogador.isOnline()) {
            status = new ItemStack(Material.EMERALD_BLOCK);
            statusMeta = status.getItemMeta();
            statusMeta.setDisplayName("§2Online");
            status.setItemMeta(statusMeta);
            inventory.setItem(11, status);
        }else {
            status = new ItemStack(Material.REDSTONE_BLOCK);
            statusMeta = status.getItemMeta();
            statusMeta.setDisplayName("§cOffline");
            status.setItemMeta(statusMeta);
            inventory.setItem(11, status);


        }
        inventory.setItem(10, cabeca);
        inventory.setItem(13, vitorias);
        inventory.setItem(14, derrotas);
        inventory.setItem(15, kills);
        inventory.setItem(16, mortes);
        p.openInventory(inventory);
    }
}
