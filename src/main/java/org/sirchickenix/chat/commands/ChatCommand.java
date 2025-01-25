package org.sirchickenix.chat.commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.sirchickenix.chat.utils.DataAccessor;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ChatCommand implements CommandExecutor, Listener {
    public static boolean isChatOn = true;

    private final LuckPerms luckPerms;
    private final Main plugin;
    private final ItemStack onButton;
    private final ItemStack offButton;
    private final DataAccessor dataAccessor;

    //HashMap of (player, inventory) so that there is one inventory per player - avoid memory leaks
    private HashMap<OfflinePlayer, Inventory> guis = new HashMap<OfflinePlayer, Inventory>();

    public ChatCommand(Main plugin, LuckPerms luckperms, DataAccessor dataAccessor) {
        this.plugin = plugin;
        this.luckPerms = luckperms;
        this.dataAccessor = dataAccessor;

        ItemStack on = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta onButtonMeta = on.getItemMeta();
        onButtonMeta.setDisplayName(ChatColor.RED + "Click to turn chat off!");
        on.setItemMeta(onButtonMeta);
        this.onButton = on;

        ItemStack off = new ItemStack(Material.RED_CONCRETE);
        ItemMeta offButtonMeta = off.getItemMeta();
        offButtonMeta.setDisplayName(ChatColor.RED + "Click to turn chat back on!");
        off.setItemMeta(offButtonMeta);
        this.offButton = off;
    }

    //Chat Command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player player)) {
            return false;
        }
        if(!player.hasPermission("chat.chat")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to run /" + label);
            return true;
        }

        OfflinePlayer offlinePlayer = this.plugin.getServer().getOfflinePlayer(player.getName());
        if(!guis.containsKey(offlinePlayer)) {
            guis.put(offlinePlayer, makeInventory());
        }
        updateInventory(guis.get(offlinePlayer));
        player.openInventory(guis.get(offlinePlayer));

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
            }
        }
        return inv;
    }

    //Updates an inventory with the latest correct settings
    public void updateInventory(Inventory inv) {

        if(ChatCommand.isChatOn) {
            inv.setItem(13, onButton);
        } else {
            inv.setItem(13, offButton);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getClickedInventory() == null) return;
        //checks that clicked inventory is in our list of chat command guis
        for(OfflinePlayer offlinePlayer : guis.keySet()) {
            if(event.getClickedInventory().equals(guis.get(offlinePlayer))) {
                break;
            }
            return;
        }
        event.setCancelled(true);

        Inventory inv = event.getInventory();
        switch(event.getSlot()) {
            case 13:
                if(ChatCommand.isChatOn) {
                    inv.setItem(13, offButton);
                } else {
                    inv.setItem(13, onButton);
                }
                isChatOn = !isChatOn;
                break;
            default:
                break;
        }

    }

}
