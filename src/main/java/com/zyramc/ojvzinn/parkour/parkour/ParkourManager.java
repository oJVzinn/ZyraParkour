package com.zyramc.ojvzinn.parkour.parkour;

import com.zyramc.ojvzinn.parkour.Main;
import com.zyramc.ojvzinn.parkour.parkour.config.ParkourConfig;
import com.zyramc.ojvzinn.parkour.parkour.objects.PlayerTimerCache;
import com.zyramc.ojvzinn.parkour.player.PlayerTimer;
import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.collectibles.api.CosmeticsAPI;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.utils.TimeUtils;
import dev.slickcollections.kiwizin.utils.enums.EnumSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ParkourManager implements Listener {

    private static final List<ParkourManager> PARKOURS_CACHE = new ArrayList<>();
    private static final List<String> COOLDOWN = new ArrayList<>();
    private final String key;
    private final ParkourConfig config;
    private final Set<PlayerTimerCache> PLAYERS_PARKOUR;

    public static void setupParkours() {
        KConfig CONFIG = Main.getInstance().getConfig("parkour");

        if (CONFIG.getFile().length() > 1) {
            for (String key : CONFIG.getSection("parkours").getKeys(false)) {
                ParkourManager parkourManager = new ParkourManager(key);
                PARKOURS_CACHE.add(parkourManager);
            }
        }
    }

    public static ParkourManager getParkour(String key) {
        return PARKOURS_CACHE.stream().filter(pakourManager -> pakourManager.getKey().equals(key)).findFirst().orElse(null);
    }

    public static ParkourManager getParkourWithPlayer(Player  player) {
        return PARKOURS_CACHE.stream().filter(pakourManager -> pakourManager.isPlayerParkour(player.getName())).findFirst().orElse(null);
    }

    public static void createParkour(List<String> locations, String key) {
        KConfig CONFIG = Main.getInstance().getConfig("parkour");
        CONFIG.set("parkours." + key + ".locations", locations);
        CONFIG.save();
        ParkourManager parkourManager = new ParkourManager(key);
        PARKOURS_CACHE.add(parkourManager);
    }

    public static void deleteParkour(String key) {
        KConfig CONFIG = Main.getInstance().getConfig("parkour");
        CONFIG.set("parkours." + key + ".locations", null);
        CONFIG.save();
        ParkourManager parkourManager = ParkourManager.getParkour(key);
        parkourManager.destroy();
        PARKOURS_CACHE.remove(parkourManager);
    }

    public ParkourManager(String key) {
        KConfig CONFIG = Main.getInstance().getConfig("parkour");
        config = new ParkourConfig(key, CONFIG.getRawConfig());
        config.setupConfig();
        PLAYERS_PARKOUR = new HashSet<>();
        this.key = key;

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlayerParkourStart(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation().getBlock().getLocation();

        if (loc.getWorld().equals(Core.getLobby().getWorld())) {
            if (player.getAllowFlight()) {
                if (isPlayerParkour(player.getName())) {
                    removePlayer(player.getName());
                    player.sendMessage("§cNão é permitido usar fly enquanto faz o parkour.");
                    CosmeticsAPI.enable(player);
                    return;
                }
            }
            if (this.config.getLOCATIONS().containsValue(loc)) {
                if (!isPlayerParkour(player.getName())) {
                    if (Objects.equals(this.config.getKeyForLocation(loc), "0")) {
                        player.sendMessage("§aVocê acaba de iniciar o parkour!");
                        addPlayer(player.getName());
                        PlayerTimer playerTimer = PlayerTimer.createPlayerTimer(player.getName());
                        playerTimer.startCountTime();
                        player.setAllowFlight(false);
                        CosmeticsAPI.disable(player);
                        COOLDOWN.add(player.getName());
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                COOLDOWN.remove(player.getName());
                            }
                        }.runTaskLaterAsynchronously(Main.getInstance(), 20 * 3L);
                    }
                    return;
                }

                if (isPlayerParkour(player.getName()) && !COOLDOWN.contains(player.getName())) {
                    if (Objects.equals(this.config.getKeyForLocation(loc), "0")) {
                        removePlayer(player.getName());
                        EnumSound.ORB_PICKUP.play(player, 1.0F, 1.0F);
                    }
                }

                int checkpoint = Integer.parseInt(getConfig().getKeyForLocation(loc));
                if (getPlayerTimeCache(player.getName()) != null) {
                    int checkpointNow = Integer.parseInt(getPlayerTimeCache(player.getName()).getCheckPoint());
                    if (checkpointNow + 1 == checkpoint) {
                        if (getConfig().getLOCATIONS().size() - 1 == checkpoint) {
                            PlayerTimer timer = PlayerTimer.loadPlayerTimerProfiler(player.getName());
                            Long currentTime = timer.getCurrentTime();
                            if (timer.isNull()) {
                                player.sendMessage("§e§lNOVO RECORDE! §aParabéns por completar o parkour, tempo gasto: " + timer.formatedTime(currentTime));
                                timer.setTime(String.valueOf(currentTime));
                            } else {
                                if (currentTime < timer.getTime()) {
                                    player.sendMessage("§e§lNOVO RECORDE! §aParabéns por completar o parkour, tempo gasto: " + timer.formatedTime(currentTime));
                                    timer.setTime(String.valueOf(currentTime));
                                } else {
                                    player.sendMessage("§aParabéns por completar o Parkour! Você levou " + timer.formatedTime(currentTime));
                                }
                            }
                            timer.resetCurrentTime();
                            removePlayer(player.getName());
                            EnumSound.LEVEL_UP.play(player, 1.0F, 1.0F);
                            CosmeticsAPI.enable(player);
                            if (player.hasPermission("kcore.fly")) {
                                player.setAllowFlight(true);
                            }
                            return;
                        }

                        getPlayerTimeCache(player.getName()).setCheckPoint(String.valueOf(checkpoint));
                        player.sendMessage("§aVocê conquistou o §b§lCheckpoint #" + checkpoint + "\n§aUtilize /checkpoint (ou /cp) para voltar até aqui.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (getPlayerTimeCache(player.getName()) != null) {
            removePlayer(player.getName());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (getPlayerTimeCache(player.getName()) != null) {
            removePlayer(player.getName());
        }
    }

    public PlayerTimerCache getPlayerTimeCache(String name) {
        return PLAYERS_PARKOUR.stream().filter(playerTimerCache -> playerTimerCache.getName().equals(name)).findFirst().orElse(null);
    }

    public void addPlayer(String name) {
        PlayerTimerCache playerTimerCache = new PlayerTimerCache(name);
        PLAYERS_PARKOUR.add(playerTimerCache);
    }

    public void removePlayer(String name) {
        if (isPlayerParkour(name)) {
            PLAYERS_PARKOUR.remove(getPlayerTimeCache(name));
        }
    }

    public boolean isPlayerParkour(String name) {
        return getPlayerTimeCache(name) != null;
    }

    public ParkourConfig getConfig() {
        return config;
    }

    public String getKey() {
        return key;
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        this.config.destroy();
    }
}
