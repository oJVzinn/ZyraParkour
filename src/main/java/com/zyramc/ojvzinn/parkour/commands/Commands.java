package com.zyramc.ojvzinn.parkour.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public abstract class Commands extends Command {

    protected Commands(String name, String... aliases) {
        super(name);
        this.setAliases(Arrays.asList(aliases));

        CommandMap commandMap;
        try {
            commandMap = (CommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
            commandMap.register("zParkour", this);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        performace(commandSender, s, strings);
        return true;
    }

    abstract void performace(CommandSender sender, String s, String[] args);

    public static void setupCommands() {
        new ParkourCreate("parkour", "pk");
        new CheckpoinCommand();
        new RemoveParkourCommand();
    }
}
