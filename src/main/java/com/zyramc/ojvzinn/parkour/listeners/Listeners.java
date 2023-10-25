package com.zyramc.ojvzinn.parkour.listeners;

import com.zyramc.ojvzinn.parkour.Main;
import com.zyramc.ojvzinn.parkour.commands.Creating;
import com.zyramc.ojvzinn.parkour.commands.ParkourCreate;
import com.zyramc.ojvzinn.parkour.parkour.ParkourManager;
import com.zyramc.ojvzinn.parkour.player.PlayerTimer;
import com.zyramc.ojvzinn.parkour.utils.LocationUtils;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class Listeners implements Listener {

    public static void setupListeners() {
        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.getInstance());
    }

    @EventHandler
    public void onPlayerQuitListeners(PlayerQuitEvent event) {
        PlayerTimer.destroyPlayerTimer(event.getPlayer().getName());
        ParkourCreate.IS_CREATING.remove(event.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerInteractListeners(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (ParkourCreate.IS_CREATING.containsKey(player.getName())) {
            ItemStack item = player.getItemInHand();
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String name = item.getItemMeta().getDisplayName();
                Creating creating = ParkourCreate.IS_CREATING.get(player.getName());
                Profile profile = Profile.getProfile(player.getName());

                switch (name) {

                    case "§aConfirmar Localizações": {
                        if (creating.getLOCATIONS().size() < 3) {
                            player.sendMessage("§cPara confirmar uma seleção, é necessário conter 3 localizações no minimo.");
                            return;
                        }

                        player.getInventory().clear();
                        profile.setHotbar(creating.getHotbar());
                        profile.getHotbar().apply(profile);
                        ParkourManager.createParkour(creating.getLOCATIONS(), creating.getKey());
                        ParkourCreate.IS_CREATING.remove(player.getName());
                        player.sendMessage("§aParkour criado com sucesso!");
                    }

                    case "§cCancelar Localizações": {
                        player.getInventory().clear();
                        profile.setHotbar(creating.getHotbar());
                        profile.getHotbar().apply(profile);
                        ParkourCreate.IS_CREATING.remove(player.getName());
                        player.sendMessage("§cSeleção cancelada.");
                    }

                    case "§aAdicionar Localização": {
                        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            Block block = event.getClickedBlock();
                            if (!block.getType().equals(Material.GOLD_PLATE)) {
                                player.sendMessage("§cSó é possível setar um checkpoint em uma placa de pressão de ouro.");
                                return;
                            }
                            Location location = block.getLocation();
                            String loc = LocationUtils.serializeLocation(location);
                            creating.addLocation(loc);
                            player.sendMessage("§aLocalização adicionada com sucesso!");
                        }
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
