package com.zyramc.ojvzinn.parkour.parkour.config;

import com.zyramc.ojvzinn.parkour.utils.LocationUtils;
import dev.slickcollections.kiwizin.libraries.holograms.HologramLibrary;
import dev.slickcollections.kiwizin.libraries.holograms.api.Hologram;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkourConfig {

    private final String key;
    private final YamlConfiguration CONFIG;
    private final List<Hologram> HOLOGRAMS = new ArrayList<>();
    private final Map<String, Location> LOCATIONS = new HashMap<>();

    public ParkourConfig(String key, YamlConfiguration CONFIG) {
        this.key = key;
        this.CONFIG = CONFIG;
    }

    public void setupConfig() {
        for (int i = 0; i < CONFIG.getStringList("parkours." + key + ".locations").size(); i++) {
            Location location = LocationUtils.deserializeLocation(CONFIG.getStringList("parkours." + key + ".locations").get(i));
            Hologram hologram;
            Location loc = location.clone().add(0.5, -0.6, 0.5);
            if (LOCATIONS.size() < 1) {
                hologram = HologramLibrary.createHologram(loc, "§aInício", "§b§lParkour");
            } else if (LOCATIONS.size() + 1 == CONFIG.getStringList("parkours." + key + ".locations").size()) {
                hologram = HologramLibrary.createHologram(loc, "§cFim", "§b§lParkour");
            } else {
                int a = CONFIG.getStringList("parkours." + key + ".locations").size() - 2;
                hologram = HologramLibrary.createHologram(loc, "§e#" + i + "/" + a, "§b§lParkour");
            }

            HOLOGRAMS.add(hologram);
            LOCATIONS.put(String.valueOf(i), location);
        }
    }

    public String getKeyForLocation(Location location) {
        for (String keys : LOCATIONS.keySet()) {
            if (LOCATIONS.get(keys).equals(location)) {
                return keys;
            }
        }
        return null;
    }

    public Location getLocationForCheckPoint(String checkpoint) {
        return LOCATIONS.get(checkpoint);
    }

    public String getKey() {
        return key;
    }

    public YamlConfiguration getCONFIG() {
        return CONFIG;
    }

    public List<Hologram> getHOLOGRAMS() {
        return HOLOGRAMS;
    }

    public Map<String, Location> getLOCATIONS() {
        return LOCATIONS;
    }

    public void destroy() {

    }
}
