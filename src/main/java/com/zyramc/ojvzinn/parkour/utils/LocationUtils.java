package com.zyramc.ojvzinn.parkour.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

//API retirada do mCore

public class LocationUtils {

    /**
     * Transforma uma {@link Location} em uma {@code String} utilizando o seguinte formato:<br/>
     * {@code "mundo; x; y; z; yaw; pitch"}
     *
     * @param unserialized A {@link Location} para transformar em {@code String}.
     * @return A {@link Location} transformada em uma {@code String}.
     */
    public static String serializeLocation(Location unserialized) {
        return unserialized.getWorld().getName() + "; " + unserialized.getX() + "; " + unserialized.getY() + "; " + unserialized.getZ() + "; " + unserialized
                .getYaw() + "; " + unserialized.getPitch();
    }

    /**
     * Transforma uma {@code String} em uma {@link Location} utilizando o seguinte formato:<br/>
     * {@code "mundo; x; y; z; yaw; pitch"}
     *
     * @param serialized A {@code String} para transformar em {@link Location}.
     * @return A {@code String} transformada em uma {@link Location}.
     */
    public static Location deserializeLocation(String serialized) {
        String[] divPoints = serialized.split("; ");
        Location deserialized = new Location(Bukkit.getWorld(divPoints[0]), parseDouble(divPoints[1]),
                parseDouble(divPoints[2]), parseDouble(divPoints[3]));
        deserialized.setYaw(parseFloat(divPoints[4]));
        deserialized.setPitch(parseFloat(divPoints[5]));
        return deserialized;
    }

}
