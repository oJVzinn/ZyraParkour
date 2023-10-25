package com.zyramc.ojvzinn.parkour.commands;

import com.zyramc.ojvzinn.parkour.database.DataBase;
import com.zyramc.ojvzinn.parkour.database.databases.MySQL;
import com.zyramc.ojvzinn.parkour.database.databases.SQLite;
import com.zyramc.ojvzinn.parkour.lobby.LeaderboardNPC;
import dev.slickcollections.kiwizin.database.Database;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class RemoveParkourCommand extends Commands {

    protected RemoveParkourCommand() {
        super("pkr");
    }

    @Override
    void performace(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("parkour.cmd.remover")) {
            sender.sendMessage("§cEste comando é exclusivo para §4Gerente §cou superior.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUse /pkr remover [NICK]");
            return;
        }

        String action = args[0];
        String player = args[1];

        if (action.equalsIgnoreCase("remover")) {
            try {
                if (!Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).conteinsPlayer(player, "ProfileTimer")) {
                    sender.sendMessage("§cEste jogador não está registrado em nosso banco de dados.");
                    return;
                }
            } catch (Exception e) {
                sender.sendMessage("§cEste jogador não está registrado em nosso banco de dados.");
                return;
            }

            sender.sendMessage("§aJogador removido do top com sucesso!");
            Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).updateStatusPlayer(player, "ProfileTimer", "TIME", 999999999);
            Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
            LeaderboardNPC.listNPCs().forEach(LeaderboardNPC::update);
        }
    }

}
