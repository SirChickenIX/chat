package org.sirchickenix.chat;

import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.sirchickenix.chat.commands.ChatCommand;

public final class Main extends JavaPlugin {

    private LuckPerms luckPerms;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
        getCommand("chat").setExecutor(new ChatCommand(this, this.luckPerms));
        System.out.println("Chat plugin has started successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Chat plugin has stopped successfully");
    }
}
