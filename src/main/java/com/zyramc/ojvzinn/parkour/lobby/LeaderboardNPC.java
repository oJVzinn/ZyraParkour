package com.zyramc.ojvzinn.parkour.lobby;

import com.zyramc.ojvzinn.parkour.Main;
import com.zyramc.ojvzinn.parkour.database.DataBase;
import com.zyramc.ojvzinn.parkour.database.databases.SQLite;
import com.zyramc.ojvzinn.parkour.nms.NMS;
import com.zyramc.ojvzinn.parkour.nms.entity.EntityTopAmorStand;
import com.zyramc.ojvzinn.parkour.player.PlayerTimer;
import dev.slickcollections.kiwizin.database.Database;
import dev.slickcollections.kiwizin.libraries.holograms.HologramLibrary;
import dev.slickcollections.kiwizin.libraries.holograms.api.Hologram;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeaderboardNPC {

    private static final KConfig CONFIG = Main.getInstance().getConfig("npcs");
    private static final List<LeaderboardNPC> NPCS = new ArrayList<>();
    private String id;
    private Integer position;
    private Location location;
    private EntityTopAmorStand npc;
    private Hologram hologram;

    public LeaderboardNPC(Location location, String id, Integer position) {
        this.location = location;
        this.id = id;
        this.position = position;
        if (!this.location.getChunk().isLoaded()) {
            this.location.getChunk().load(true);
        }

        this.spawn();
    }

    public static void setupNPCs() {
        if (!CONFIG.contains("leaderboard")) {
            CONFIG.set("leaderboard", new ArrayList<>());
        }

        for (String serialized : CONFIG.getStringList("leaderboard")) {
            if (serialized.split("; ").length > 7) {
                String id = serialized.split("; ")[6];
                Integer position = Integer.valueOf(serialized.split("; ")[7]);

                NPCS.add(new LeaderboardNPC(BukkitUtils.deserializeLocation(serialized), id, position));
            }
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> listNPCs().forEach(LeaderboardNPC::update), 0L, 20 * (60 * 5));
    }

    public static void add(String id, Location location, Integer position) {
        NPCS.add(new LeaderboardNPC(location, id, position));
        List<String> list = CONFIG.getStringList("leaderboard");
        list.add(BukkitUtils.serializeLocation(location) + "; " + id + "; " + position);
        CONFIG.set("leaderboard", list);
    }

    public static void remove(LeaderboardNPC npc) {
        NPCS.remove(npc);
        List<String> list = CONFIG.getStringList("leaderboard");
        list.remove(BukkitUtils.serializeLocation(npc.getLocation()) + "; " + npc.getId() + "; " + npc.getPosition());
        CONFIG.set("leaderboard", list);

        npc.destroy();
    }

    public static LeaderboardNPC getById(String id) {
        return NPCS.stream().filter(npc -> npc.getId().equals(id)).findFirst().orElse(null);
    }

    public static Collection<LeaderboardNPC> listNPCs() {
        return NPCS;
    }

    public void spawn() {
        if (this.npc != null) {
            this.npc.kill();
            this.npc = null;
        }

        if (this.hologram != null) {
            HologramLibrary.removeHologram(this.hologram);
            this.hologram = null;
        }

        Location finalLoc = this.getLocation().clone().getBlock().getLocation().add(0.5, position == 2 ? -0.3 : position == 4 ? -0.3 : 0.3, 0.5);
        this.hologram = HologramLibrary.createHologram(finalLoc);
        Map<String, Long> a = new HashMap<>(Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).getListPlayers("ProfileTimer"));
        Map<String, Long> sorted = a.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<String> keys = new ArrayList<>(sorted.keySet());
        String player = "Ninguém";
        String time = "00:00";
        if (a.size() >= this.position) {
            time = PlayerTimer.formatedTime(a.get(keys.get(this.position - 1)));
            player = Role.getColored(keys.get(this.position - 1));
        }
        this.hologram.withLine("§7" + time);
        this.hologram.withLine(player);
        ItemStack head = BukkitUtils.deserializeItemStack("397:3");
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(StringUtils.stripColors(player));
        head.setItemMeta(meta);
        finalLoc = this.getLocation().clone().getBlock().getLocation().add(0.5, 0, 0.5);
        finalLoc.setY(this.location.getY());
        finalLoc.setDirection(this.location.getDirection());
        this.hologram.withLine("§f§l" + this.position + "° §f§lLugar");
        this.npc = NMS.createAmorStandTop(finalLoc, this.getPosition(), head);
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
    }

    public void update() {
        Map<String, Long> a = new HashMap<>(Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).getListPlayers("ProfileTimer"));
        Map<String, Long> sorted = a.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(10).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        List<String> keys = new ArrayList<>(sorted.keySet());
        String player = "Ninguém";
        String time = "00:00";
        if (a.size() >= this.position) {
            player = Role.getColored(keys.get(this.position - 1));
            time = PlayerTimer.formatedTime(a.get(keys.get(this.position - 1)));
        }
        this.hologram.updateLine(1, "§7" + time);
        this.hologram.updateLine(2, player);
        this.hologram.updateLine(3, "§f§l" + this.position + "° §f§lLugar");
        ItemStack head = BukkitUtils.deserializeItemStack("397:3");
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwner(StringUtils.stripColors(player));
        head.setItemMeta(meta);
        this.npc.setEquipment(4, CraftItemStack.asNMSCopy(head));
        Objects.requireNonNull(DataBase.getDatabase(SQLite.class)).closeConnection();
    }

    public void destroy() {
        this.id = null;
        this.location = null;
        this.npc.kill();
        this.npc = null;
        HologramLibrary.removeHologram(this.hologram);
        this.hologram = null;
        this.position = 0;
    }

    public EntityTopAmorStand getNpc() {
        return this.npc;
    }

    public String getId() {
        return id;
    }

    public Integer getPosition() {
        return this.position;
    }

    public Location getLocation() {
        return this.location;
    }
}
