package org.sirchickenix.chat.commands;

import net.luckperms.api.LuckPerms;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.sirchickenix.chat.Main;

public class ChatCommand implements CommandExecutor {
    private final LuckPerms luckPerms;
    private final Main plugin;

    public ChatCommand(Main plugin, LuckPerms luckperms) {
        this.plugin = plugin;
        this.luckPerms = luckperms;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String test = "group.default";
        if(!(sender instanceof Player)) {
            return false;
        }
        String playername;
        Player player = (Player)sender;
        player.sendMessage("Do you have permission " + test + "?" + player.hasPermission(test));
        //OfflinePlayer player = this.plugin.getServer().getOfflinePlayer(playername);

        System.out.println(player);
        return true;
    }
}
