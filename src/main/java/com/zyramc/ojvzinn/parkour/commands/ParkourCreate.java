package com.zyramc.ojvzinn.parkour.commands;

import com.zyramc.ojvzinn.parkour.lobby.LeaderboardNPC;
import com.zyramc.ojvzinn.parkour.parkour.ParkourManager;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ParkourCreate extends Commands {

    public static Map<String, Creating> IS_CREATING = new HashMap<>();

    protected ParkourCreate(String name, String... aliases) {
        super(name, aliases);
    }

    @Override
    void performace(CommandSender sender, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }
        Player player = (Player) sender;
        Profile profile = Profile.getProfile(player.getName());

        if (!player.hasPermission("parkour.cmd.admin")) {
            player.sendMessage("§cEste comando é exclusivo para o grupo §4Gerente §cou superior.");
            return;
        }

        if (args.length < 1) {
            player.sendMessage("\n \n§aAjuda - 1/1\n \n§6/pk create [key]\n§6/pk delete [key]\n§6/pk lb [adicionar/remover] [id] [posição]\n \n");
            return;
        }

        String action = args[0];

        switch (action) {

            case "create": {
                if (args.length < 2) {
                    player.sendMessage("§c/pk create [key]");
                    return;
                }

                String key = args[1];
                player.sendMessage("§aFoi dado itens para você para você setar os locais.");
                player.getInventory().clear();
                Hotbar hotbar = profile.getHotbar();
                profile.setHotbar(null);
                Creating isCreating = new Creating(key, hotbar);
                IS_CREATING.put(player.getName(), isCreating);
                player.getInventory().setItem(2, BukkitUtils.deserializeItemStack("351:10 : 1 : nome>&aConfirmar Localizações"));
                player.getInventory().setItem(4, BukkitUtils.deserializeItemStack("369 : 1 : nome>&aAdicionar Localização"));
                player.getInventory().setItem(6, BukkitUtils.deserializeItemStack("351:1 : 1 : nome>&cCancelar Localizações"));
                break;
            }

            case "delete": {
                if (args.length < 2) {
                    player.sendMessage("§c/pk delete [key]");
                    return;
                }

                String key = args[1];
                if (ParkourManager.getParkour(key) == null) {
                    player.sendMessage("§cNão existe nenhum parkour com essa key.");
                    break;
                }

                ParkourManager.getParkour(key).destroy();
                ParkourManager.deleteParkour(key);
                player.sendMessage("§aParkour deletado com sucesso!");
                break;
            }

            case "lb": {
                if (args.length < 3) {
                    player.sendMessage("§c/pk lb [adicionar/remover] [id] [posição]");
                    break;
                }

                String type = args[1];

                switch (type) {

                    case "adicionar": {
                        if (args.length < 4) {
                            player.sendMessage("§c/pk lb [adicionar/remover] [id] [posição]");
                            break;
                        }
                        String id = args[2];
                        String posicao = args[3];

                        LeaderboardNPC.add(id, player.getLocation(), Integer.valueOf(posicao));
                        break;
                    }

                    case "remove": {
                        String id = args[2];
                        LeaderboardNPC.remove(LeaderboardNPC.getById(id));
                        break;
                    }
                }
            }
        }
    }
}
