package com.zyramc.ojvzinn.parkour.commands;

import dev.slickcollections.kiwizin.player.hotbar.Hotbar;

import java.util.ArrayList;
import java.util.List;

public class Creating {

    private final List<String> LOCATIONS = new ArrayList<>();
    private final String key;
    private final Hotbar hotbar;

    public Creating(String key, Hotbar hotbar) {
        this.hotbar = hotbar;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public Hotbar getHotbar() {
        return hotbar;
    }

    public List<String> getLOCATIONS() {
        return LOCATIONS;
    }

    public void addLocation(String loc) {
        LOCATIONS.add(loc);
    }
}
