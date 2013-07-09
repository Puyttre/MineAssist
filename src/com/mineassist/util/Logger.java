package com.mineassist.util;

import com.mineassist.MineAssist;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Logger {
	
	private MineAssist plugin;
	private final String name;
	private File outputFile;
	
	public Logger(MineAssist plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}
	
	public Logger(MineAssist plugin, String name, File outputFile) {
		this.plugin = plugin;
		this.name = name;
		this.outputFile = outputFile;
	}
	
	public void info(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN
						+ "[" + name + "]" + ChatColor.GRAY
						+ "[INFO] " + ChatColor.WHITE + message);
		output(message);
	}
	
	public void warn(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN
						+ "[" + name + "]" + ChatColor.YELLOW
						+ "[WARNING] " + ChatColor.WHITE + message);
		output(message);
	}
	
	public void severe(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN
						+ "[" + name + "]" + ChatColor.DARK_RED
						+ "[SEVERE] " + ChatColor.WHITE + message);
		output(message);
	}
	
	public void error(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN
						+ "[" + name + "]" + ChatColor.RED
						+ "[ERROR] " + ChatColor.WHITE + message);
		output(message);
	}
	
	public void debug(String message) {
		if (plugin.getConfig().getBoolean("debug")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GREEN
						+ "[" + name + "]" + ChatColor.GREEN
						+ "[DEBUG] " + ChatColor.WHITE + message);
		}
		output(message);
	}
	
	public void output(String message) {
		if (outputFile == null) return;
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
			out.println(getTimeDate() + message);
			out.flush();
			out.close();
		} catch (IOException e) { /* Don't care... */ }
	}
	
	private String getTimeDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return "[" + dateFormat.format(cal.getTime()) + "] ";
	}
	
}
