package com.mineassist.event;

import com.mineassist.MineAssist;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class OreBreakListener implements Listener {
	
	private MineAssist plugin;
	
	public OreBreakListener(MineAssist plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onOreBreak(BlockBreakEvent e) {
		if (e.getPlayer().hasPermission("mineassist.use") || e.getPlayer().isOp()) {
			if (plugin.getConfig().getIntegerList("acceptable-blocks").contains(e.getBlock().getTypeId())) {
				List<ItemStack> drops = new ArrayList();
				int radius = 1;
				Location middleBlock = e.getBlock().getLocation();
				Material indicator = e.getBlock().getType();
				for (int x = -(radius); x <= radius; x++) {
					for (int y = -(radius); y <= radius; y++) {
						for (int z = -(radius); z <= radius; z++) {
							Location currentLoc = middleBlock.getBlock().getRelative(x, y, z).getLocation();
							if (currentLoc.getBlock().getType() == indicator /*&& e.getPlayer().getGameMode() != GameMode.CREATIVE*/) {
								for (ItemStack is : currentLoc.getBlock().getDrops()) {
									if (plugin.getConfig().getBoolean("place-drops-in-inventory")) {
										drops.add(is);
										currentLoc.getBlock().setType(Material.AIR);
									} else {
										currentLoc.getBlock().breakNaturally();
									}
								}
								continueSearching(currentLoc, indicator, e.getPlayer());
							}
						}
					}
				}
				for (ItemStack is : drops) {
					Item i = e.getPlayer().getWorld().dropItem(new Location(e.getPlayer().getWorld(),
									e.getPlayer().getLocation().getX(),
									e.getPlayer().getLocation().getY(),
									e.getPlayer().getLocation().getZ()), is);
					i.setVelocity(new Vector(0, 0, 0));
				}
			}
		}
	}
	
	public void continueSearching(Location loc, Material indicator, Player p) {
		List<ItemStack> drops = new ArrayList();
		int radius = 1;
		Location middleBlock = loc;
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					Location currentLoc = middleBlock.getBlock().getRelative(x, y, z).getLocation();
					if (currentLoc.getBlock().getType() == indicator /*&& e.getPlayer().getGameMode() != GameMode.CREATIVE*/) {
						for (ItemStack is : currentLoc.getBlock().getDrops()) {
							if (plugin.getConfig().getBoolean("place-drops-in-inventory")) {
								drops.add(is);
								currentLoc.getBlock().setType(Material.AIR);
							} else {
								currentLoc.getBlock().breakNaturally();
							}
						}
						continueSearching(currentLoc, indicator, p);
					}
				}
			}
		}
		for (ItemStack is : drops) {
			Item i = p.getWorld().dropItem(new Location(p.getWorld(),
							p.getLocation().getX(),
							p.getLocation().getY(),
							p.getLocation().getZ()), is);
			i.setVelocity(new Vector(0, 0, 0));
		}
	}
	
}
