//modified version of https://gist.github.com/graywolf336/8153678

package com.github.arcoda.SCSwap.Library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class AdvancementLibrary {
    public static String toString(Player p) throws IllegalStateException, IOException {
    		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    		BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
    		Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
    		while (iterator.hasNext()) {
    			Advancement a = iterator.next();
    			AdvancementProgress progress = p.getAdvancementProgress(a);
    			if (a == null || progress == null)
    				continue;
    			dataOutput.writeObject("@a" + a.getKey().getKey());
    			for (String criteria : progress.getAwardedCriteria()) {
    				dataOutput.writeObject(criteria);
					progress.revokeCriteria(criteria);
				}
    		}
    		dataOutput.close();
    		return Base64Coder.encodeLines(outputStream.toByteArray());
    	
    }
    public static Boolean fromString(String str, Player p) throws IOException, ClassNotFoundException {
    		String b;
    		String c = "";
    		ByteArrayInputStream outputStream = new ByteArrayInputStream(Base64Coder.decodeLines(str));
    		BukkitObjectInputStream dataOutput = new BukkitObjectInputStream(outputStream);
    		Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
    		while (iterator.hasNext()) {
    			Advancement a = iterator.next();
    			AdvancementProgress progress = p.getAdvancementProgress(a);
    			if (a == null || progress == null)
    				continue;
    			//if( ("@a" + a.getKey().getKey()) == dataOutput.readObject() || ("@a" + a.getKey().getKey() == c)) {
    				for(String s : progress.getRemainingCriteria()) {
    					b = (String) dataOutput.readObject();
    					if(b.startsWith("@a")) {
    						c = b;
    						break;
    					}
    					if(b == s) {
    						progress.awardCriteria(b);
    					}
    				}
    			//}
    			
    		}
    		dataOutput.close();
    		return true;
    }
}
