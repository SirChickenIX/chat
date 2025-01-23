package org.sirchickenix.chat.commands;

import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.sirchickenix.chat.Main;

import java.util.HashMap;

public class ChatCommand implements CommandExecutor, Listener {
    private final LuckPerms luckPerms;
    private final Main plugin;
    //HashMap of (player, inventory) so that there is one inventory per player - avoid memory leaks
    private HashMap<OfflinePlayer, Inventory> guis = new HashMap<OfflinePlayer, Inventory>();

    public ChatCommand(Main plugin, LuckPerms luckperms) {
        this.plugin = plugin;
        this.luckPerms = luckperms;
    }

    //Chat Command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String test = "group.default";
        if(!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player)sender;
        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(player.getName());
        if(!guis.containsKey(offlinePlayer)) {
            guis.put(offlinePlayer, makeInventory());
        }
        player.openInventory(guis.get(offlinePlayer));


        System.out.println(player);
        return true;
    }

    //Creates an inventory - this is one per player per server instance to prevent memory leaks
    public Inventory makeInventory() {
        Inventory inv = Bukkit.createInventory(null, 27, "Name");


        ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blankMeta = blank.getItemMeta();
        blankMeta.setDisplayName(" ");
        blank.setItemMeta(blankMeta);
        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null) {
                inv.setItem(i, blank);
                System.out.println(blank.getItemMeta().getDisplayName());
            }
        }
        return inv;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //checks that clicked inventory is in our list of chat command guis
        for(OfflinePlayer offlinePlayer : guis.keySet()) {
            if(event.getClickedInventory().equals(guis.get(offlinePlayer))) {
                break;
            }
            return;
        }
        event.setCancelled(true);
        switch(event.getSlot()) {
            default:
                break;
        }

    }
}
