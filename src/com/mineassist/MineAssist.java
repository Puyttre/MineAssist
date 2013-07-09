package com.mineassist;

import com.mineassist.util.Logger;
import com.mineassist.util.Updater;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineAssist extends JavaPlugin {
	
	public Logger log;
	private PluginManager pm;
	private PluginDescriptionFile pdf;
	
	private File configFile;
	private FileConfiguration config;
	
	// Updater settings //
	public String address;
	public String updatePath;
	
	@Override
	public void onLoad() {
		saveResource("log.txt", false);
		log = new Logger(this, "MineAssist", new File(getDataFolder(), "log.txt"));
		checkDataFolder();
		configFile = new File(getDataFolder(), "config.yml");
		saveResource("config.yml", false);
		config = new YamlConfiguration();
	}
	
	@Override
	public void onEnable() {
		pdf = getDescription();
		log.info("Enabling " + pdf.getName() + " v" + pdf.getVersion() + ".");
		pm = Bukkit.getPluginManager();
		setupConfig();
		
		log.info("Successfully enabled " + pdf.getName() + " v" + pdf.getVersion() + ".");
	}
	
	@Override
	public void onDisable() {
		log.info("Disabling " + pdf.getName() + " v" + pdf.getVersion() + ".");
		
		log.info("Successfully disabled " + pdf.getName() + " v" + pdf.getVersion() + ".");
	}
	
	private void checkDataFolder() {
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
	}
	
	public void checkUpdates() {
		Updater upd = new Updater(this);
		address = "http://cloud.github.com/downloads/legendarycraft/MineSiege-2/release/MineWare.jar";
		updatePath = "plugins"+ File.separator + "MineWare.jar";
		log.info("Checking for updates...");
		if (upd.updateCheck()) {
			getServer().getPluginManager().disablePlugin(this);
			getServer().dispatchCommand(Bukkit.getConsoleSender(), "reload");
		}
	}
	
	private void setupConfig() {
		try {
			config.load(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		config.options().copyDefaults(true);
		updateConfig();
		saveConfig();
	}
	
	private void updateConfig() {
		HashMap<String, String> items = new HashMap<String, String>();
		items = loadConfigurables(items);
		int num = 0;
		for (Map.Entry<String, String> item : items.entrySet()) {	
			if (config.get(item.getKey()) == null) {
				if (item.getValue().equalsIgnoreCase("LIST")) {
					List<String> list = Arrays.asList("LIST ITEMS GO HERE");
					config.addDefault(item.getKey(), list);
				} else if (item.getValue().equalsIgnoreCase("true")) {
					config.addDefault(item.getKey(), true);
				} else if (item.getValue().equalsIgnoreCase("false")) {
					config.addDefault(item.getKey(), false);
				} else if (isInteger(item.getValue())) {
					config.addDefault(item.getKey(), Integer.parseInt(item.getValue()));
				} else {
					config.addDefault(item.getKey(), item.getValue());
				}
				num++;
			}
		}
		if (num > 0) {
			log.info("Added " + num + " missing entries to the config file.");
		}
	}
	
	private HashMap<String, String> loadConfigurables(HashMap<String, String> items) {
		// v1.0
		items.put("debug", "false");
		items.put("acceptable-blocks", "[14, 15, 16, 21, 56, 73, 74, 89, 129, 153]");
		return items;
	}
	
	private void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveConfig() {
		try {
			this.config.save(this.configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
