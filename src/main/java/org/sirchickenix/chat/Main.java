package org.sirchickenix.chat;

import net.luckperms.api.LuckPerms;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.sirchickenix.chat.commands.ChatCommand;
import org.sirchickenix.chat.listeners.ChatListener;
import org.sirchickenix.chat.utils.DataAccessor;

import java.io.File;

public final class Main extends JavaPlugin {

    private LuckPerms luckPerms;
    private ChatCommand chatCommand;
    private DataAccessor dataAccessor;

    @Override
    public void onEnable() {
        //Instances of classes
        this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
        this.dataAccessor = new DataAccessor(this, "data.yml");
        if(!dataAccessor.getDataFile().exists()) {
            dataAccessor.saveDefaultData();
        }
        loadData();
        this.chatCommand = new ChatCommand(this, luckPerms, dataAccessor);

        //Commands
        getCommand("chat").setExecutor(chatCommand);
        getCommand("chat").setTabCompleter(chatCommand);

        //Listeners
        getServer().getPluginManager().registerEvents(chatCommand, this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        //Success Message
        System.out.println("Chat plugin has started successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveData();
        System.out.println("Chat plugin has stopped successfully");
    }

    public void loadData() {
        ChatCommand.isChatOn = dataAccessor.getData().getBoolean("chat.on");
    }
    public void saveData() {
        dataAccessor.getData().set("chat.on", ChatCommand.isChatOn);
        dataAccessor.saveData();
    }
}
