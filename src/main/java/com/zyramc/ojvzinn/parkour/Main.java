package com.zyramc.ojvzinn.parkour;

import com.zyramc.ojvzinn.parkour.commands.Commands;
import com.zyramc.ojvzinn.parkour.database.DataBase;
import com.zyramc.ojvzinn.parkour.database.DataTypes;
import com.zyramc.ojvzinn.parkour.listeners.Listeners;
import com.zyramc.ojvzinn.parkour.lobby.LeaderboardNPC;
import com.zyramc.ojvzinn.parkour.parkour.ParkourManager;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import org.bukkit.Bukkit;

public class Main extends KPlugin {

    private static Main plugin;

    @Override
    public void start() {

    }

    @Override
    public void load() {
        plugin = this;
    }

    @Override
    public void enable() {
        DataBase.setupDataBases(DataTypes.SQLITE, this);
        Listeners.setupListeners();
        Commands.setupCommands();
        ParkourManager.setupParkours();
        LeaderboardNPC.setupNPCs();

        sendMessage("O plugin iniciou com sucesso!");
    }

    @Override
    public void disable() {

    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage("§a[" + getDescription().getName() + "] " + message);
    }

    public void sendMessage(String message, String color) {
        Bukkit.getConsoleSender().sendMessage("§" + color + "[" + getDescription().getName() + "] " + message);
    }

    public static Main getInstance() {
        return plugin;
    }
}
