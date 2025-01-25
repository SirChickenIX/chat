package org.sirchickenix.chat;

import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.java.JavaPlugin;
import org.sirchickenix.chat.commands.ChatCommand;
import org.sirchickenix.chat.listeners.ChatListener;

public final class Main extends JavaPlugin {

    private LuckPerms luckPerms;
    private ChatCommand chatCommand;

    @Override
    public void onEnable() {
        //Instances of classes
        this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
        this.chatCommand = new ChatCommand(this, luckPerms);

        //Commands
        getCommand("chat").setExecutor(chatCommand);

        //Listeners
        getServer().getPluginManager().registerEvents(chatCommand, this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        //Success Message
        System.out.println("Chat plugin has started successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Chat plugin has stopped successfully");
    }
}
