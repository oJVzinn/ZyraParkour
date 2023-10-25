package com.zyramc.ojvzinn.parkour.player;

import com.zyramc.ojvzinn.parkour.Main;
import com.zyramc.ojvzinn.parkour.database.DataBase;
import com.zyramc.ojvzinn.parkour.database.databases.SQLite;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("ALL")
public class PlayerTimer {

    private static final ConcurrentHashMap<String, PlayerTimer> PLAYER_CACHE = new ConcurrentHashMap<>();
    private final String name;
    private String time = "";
    private BukkitTask task;
    public Long currentTime = 0L;

    public static PlayerTimer createPlayerTimer(String name) {
        PlayerTimer playerTimer = new PlayerTimer(name);
        PLAYER_CACHE.put(name, playerTimer);
        playerTimer.syncDB();
        return playerTimer;
    }

    public static PlayerTimer loadPlayerTimerProfiler(String name) {
        return PLAYER_CACHE.get(name);
    }

    public static void destroyPlayerTimer(String name) {
        if (PLAYER_CACHE.containsKey(name)) {
            PLAYER_CACHE.get(name).saveDB();
            PLAYER_CACHE.get(name).stopCountTime();
            PLAYER_CACHE.remove(name);
        }
    }

    public static ConcurrentHashMap<String, PlayerTimer> getCache() {
        return PLAYER_CACHE;
    }

    public PlayerTimer(String name) {
        this.name = name;
    }

    public void syncDB() {
        SQLite db = DataBase.getDatabase(SQLite.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!db.conteinsPlayer(name, "ProfileTimer")) {
                    db.addStatusDefaultPlayer(name, "ProfileTimer");
                }

                time = db.getStatusForPlayerString(name, "TIME", "ProfileTimer");
                Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 0L);
    }

    public void saveDB() {
        SQLite db = DataBase.getDatabase(SQLite.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                db.updateStatusPlayer(name, "ProfileTimer", "TIME", time);
                Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 0L);
    }

    public void startCountTime() {
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                currentTime++;
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0L, 20L);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(name);
    }
    public Long getCurrentTime() {
        return currentTime;
    }

    public Long getTime() {
        return Long.valueOf(time);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isNull() {
        try {
            getTime();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public String getTimeFormated() {
        return TimeUtils.getTime(getTime());
    }

    public void stopCountTime() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    public void resetCurrentTime() {
        this.currentTime = 0L;
    }

    public static String formatedTime(Long time) {
        String segundos = "";
        String minutos = "";
        DecimalFormat df = new DecimalFormat("00");
        Long finalTime = time;
        if (finalTime < 60) {
            segundos = df.format(finalTime);
            minutos = "00";
        } else {
            Long min = 0L;
            while (finalTime > 60) {
                finalTime = finalTime - 60;
                min++;
            }
            minutos = df.format(min);
            segundos = df.format(finalTime);
        }

        return minutos + ":" + segundos;
    }
}
