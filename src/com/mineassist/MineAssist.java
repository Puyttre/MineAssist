package com.mineassist;

import com.mineassist.event.OreBreakListener;
import com.mineassist.util.Logger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineAssist extends JavaPlugin {
	
	public Logger log;
	public PluginManager pm;
	private PluginDescriptionFile pdf;
	
	private FileConfiguration config = null;
	private File configFile = null;
	
	@Override
	public void onLoad() {
		log = new Logger(this, "MineAssist", new File(getDataFolder(), "log.txt"));
		checkDataFolder();
	}
	
	@Override
	public void onEnable() {
		pdf = getDescription();
		log.info("Enabling " + pdf.getName() + " v" + pdf.getVersion() + ".");
		getCustomConfig();
		saveCustomConfig();
		pm = Bukkit.getPluginManager();
		pm.registerEvents(new OreBreakListener(this), this);
		log.info("Successfully enabled " + pdf.getName() + " v" + pdf.getVersion() + ".");
	}
	
	@Override
	public void onDisable() {
		log.info("Disabling " + pdf.getName() + " v" + pdf.getVersion() + ".");
		saveConfig();
		log.info("Successfully disabled " + pdf.getName() + " v" + pdf.getVersion() + ".");
	}
	
	private void checkDataFolder() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
	}
	
	public void saveDefaultConfig() {
		if (configFile == null) {
			configFile = new File(getDataFolder(), "config.yml");
		}
		if (!configFile.exists()) {
			this.saveResource("config.yml", false);
		}
	}

	public void reloadCustomConfig() {
		if (configFile == null) {
			configFile = new File(getDataFolder(), "config.yml");
		}
		config = YamlConfiguration.loadConfiguration(configFile);
		InputStream defConfigStream = this.getResource("config.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			config.setDefaults(defConfig);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (config == null) {
			this.reloadCustomConfig();
		}
		return config;
	}

	public void saveCustomConfig() {
		if (config == null || configFile == null) {
			return;
		}
		config.options().copyDefaults(true);
		try {
			getCustomConfig().save(configFile);
		} catch (IOException ex) {
			log.severe("Could not save config file to " + configFile);
		}
	}
	
}
