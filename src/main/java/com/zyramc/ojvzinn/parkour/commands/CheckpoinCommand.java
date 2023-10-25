package com.zyramc.ojvzinn.parkour.commands;

import com.zyramc.ojvzinn.parkour.parkour.ParkourManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckpoinCommand extends Commands {

    protected CheckpoinCommand() {
        super("cp", "checkpoint");
    }

    @Override
    void performace(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;

        if (ParkourManager.getParkourWithPlayer(player) != null) {
            player.teleport(ParkourManager.getParkourWithPlayer(player).getConfig().getLocationForCheckPoint(ParkourManager.getParkourWithPlayer(player).getPlayerTimeCache(player.getName()).getCheckPoint()));
        } else {
            sender.sendMessage("§cVocê não está em um parkour para fazer isto!");
        }
    }
}
