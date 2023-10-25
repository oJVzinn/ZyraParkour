package com.zyramc.ojvzinn.parkour.utils;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class FileUtils {

    private final String fileName;
    private final String pluginName;
    private final Class<?> classPlugin;

    public FileUtils(String fileName, String pluginName, Class<?> classPlugin) {
        this.fileName = fileName;
        this.pluginName = pluginName;
        this.classPlugin = classPlugin;
    }

    public void loadFileYaml() {
        try {
            File file = new File("plugins/" + this.pluginName + "/" + this.fileName + ".yml");
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                Files.copy(Objects.requireNonNull(this.classPlugin.getResourceAsStream("/" + this.fileName + ".yml")), file.toPath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();;
        }
    }

    public void loadFileYaml(String folderName) {
        try {
            File file = new File("plugins/" + this.pluginName +  "/" + folderName + "/" + this.fileName + ".yml");
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                Files.copy(Objects.requireNonNull(this.classPlugin.getResourceAsStream("/" + this.fileName + ".yml")), file.toPath());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public File loadFileYamlTermination(String termination) {
        File file = new File("plugins/" + this.pluginName + "/" + this.fileName + "." + termination);
        if (!file.exists()) {
            File folder = file.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
        return file;
    }

    public File loadFileYamlTermination(String folderName, String termination) {
        File file = new File("plugins/" + this.pluginName + "/" + folderName + "/" + this.fileName + "." + termination);
        return file;
    }

    public YamlConfiguration getYamlConfiguration() {
        return YamlConfiguration.loadConfiguration(new File("plugins/" + this.pluginName + "/" + this.fileName + ".yml"));
    }

    public YamlConfiguration getYamlConfiguration(String folderName) {
        return YamlConfiguration.loadConfiguration(new File("plugins/" + this.pluginName + "/" + folderName + "/" + this.fileName + ".yml"));
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPluginName() {
        return pluginName;
    }

    public File getFile() {
        return new File("plugins/" + this.pluginName + "/" + this.fileName + ".yml");
    }

    public File getFile(String folderName) {
        return new File("plugins/" + this.pluginName + "/" + folderName + "/" + this.fileName + ".yml");
    }
}
