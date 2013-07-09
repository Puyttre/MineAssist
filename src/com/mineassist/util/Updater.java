package com.mineassist.util;

import com.mineassist.MineAssist;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Updater {

	private static MineAssist plugin;

	public Updater(MineAssist instance) {
		plugin = instance;
	}

	public Boolean updateCheck() {
		URLConnection conn = null;
		try {
			URL url = new URL(plugin.address);
			conn = url.openConnection();
			File localfile = new File(plugin.updatePath);
			long lastmodifiedurl = conn.getLastModified();
			long lastmodifiedfile = localfile.lastModified();
			if (lastmodifiedurl > lastmodifiedfile) {
				plugin.log.info("Update found!");
				download();
				return true;
			} else {
				plugin.log.info("No updates found.");
				return false;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		plugin.getServer().notify();
		return false;
	}

	public void download() {
		plugin.log.info("Downloading update...");
		OutputStream out = null;
		URLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(plugin.address);
			out = new BufferedOutputStream(new FileOutputStream(plugin.updatePath));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
}

