//Modified version of https://gist.github.com/graywolf336/8153678

//Modified version of https://bukkit.org/threads/serialize-inventory-to-string-remake.429071/ NOTE: UNUSED
package com.github.arcoda.SCSwap.Library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class InventoryLibrary {
    
    /**
     * A method to serialize an inventory to Base64 string.
     * 
     * <p />
     * 
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * 
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     * 
     * @param inventory to serialize
     * @return Base64 string of the provided inventory
     * @throws IllegalStateException
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());
            
            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            
            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    public static String offHandtoBase64(ItemStack item) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);            
            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stack.", e);
        }
    }
    /**
     * 
     * A method to get an {@link Inventory} from an encoded, Base64, string.
     * 
     * <p />
     * 
     * Special thanks to Comphenix in the Bukkit forums or also known
     * as aadnk on GitHub.
     * 
     * <a href="https://gist.github.com/aadnk/8138186">Original Source</a>
     * 
     * @param data Base64 string of data containing an inventory.
     * @return Inventory created from the Base64 string.
     * @throws IOException
     */
    public static void loadInv(String data, Player p) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            // Read the serialized inventory
            int s = dataInput.readInt();
            for (int i = 0; i < (s - 1); i++) {
                p.getInventory().setItem(i, (ItemStack) dataInput.readObject());
            }
            
            dataInput.close();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    public static void loadOffHand(String data, Player p) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            p.getInventory().setItemInOffHand((ItemStack) dataInput.readObject());
            dataInput.close();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    public static ItemStack getDefaultBook() throws IOException {
    	String book = "rO0ABXcEAAAAKXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA0JdAAMV1JJVFRFTl9CT09Lc3EAfgAAc3EAfgADdXEAfgAGAAAABXEAfgAIdAAJbWV0YS10eXBldAAFdGl0bGV0AAZhdXRob3J0AAVwYWdlc3VxAH4ABgAAAAV0AAhJdGVtTWV0YXQAC0JPT0tfU0lHTkVEdAAXU01QIEluZm9ybWF0aW9uIC0gMDgvMDR0AAhNb3ZpZXMyMnNyADZjb20uZ29vZ2xlLmNvbW1vbi5jb2xsZWN0LkltbXV0YWJsZUxpc3QkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAVsACGVsZW1lbnRzdAATW0xqYXZhL2xhbmcvT2JqZWN0O3hwdXEAfgAGAAAABXQBSFsiIix7InRleHQiOiJDYXNoY3JhZnQgU01QICIsImJvbGQiOnRydWUsInVuZGVybGluZWQiOnRydWUsImNvbG9yIjoiYXF1YSJ9LHsidGV4dCI6IlxuXG5CZWNhdXNlIHRoaXMgaXMgeW91ciBmaXJzdCB0aW1lIG9uIHRoZSBTTVAsIHlvdSd2ZSBiZWVuIGdpdmVuICIsImNvbG9yIjoicmVzZXQifSx7InRleHQiOiJTYXR1cmF0aW9uICIsImJvbGQiOnRydWUsIml0YWxpYyI6dHJ1ZX0seyJ0ZXh0IjoiZm9yIDUgbWludXRlcy4gQWZ0ZXIgdGhvc2UgNSBtaW51dGVzIHJ1biBvdXQsIHlvdSdsbCBoYXZlIHRvIGdldCBmb29kIGJ5IHlvdXJzZWxmLiIsImNvbG9yIjoicmVzZXQifV10AQpbIiIseyJ0ZXh0IjoiL3NtcCBhbmQgL2NtcDoiLCJ1bmRlcmxpbmVkIjp0cnVlLCJjb2xvciI6ImdvbGQifSx7InRleHQiOiJcbkRvIC9zbXAgdG8gZ28gdG8gdGhlIFNNUCBhbmQgL2NtcCB0byBnbyBiYWNrLiBJdHMgYXMgc2ltcGxlIGFzIHRoYXQuXG5XaGVuIHlvdSBnbyB0byB0aGUgU01QLCBpdCB3aWxsIHJlc3RvcmUgeW91ciBwcmV2aW91cyBsb2NhdGlvbiwgaW52ZW50b3J5LCBhZHZhbmNlbWVudHMgYW5kIG90aGVyIGRhdGEuIiwiY29sb3IiOiJyZXNldCJ9XXQBIFsiIix7InRleHQiOiJTTVAgUnVsZXM6IiwidW5kZXJsaW5lZCI6dHJ1ZSwiY29sb3IiOiJnb2xkIn0seyJ0ZXh0IjoiXG4xKSBObyAoc2V2ZXJlKSBncmllZmluZy5cbjIpIE5vIHN0ZWFsaW5nLlxuMykgRG9uJ3Qga2lsbCBlYWNoIG90aGVyIHRvbyBtdWNoLlxuNCkgRm9sbG93IHRoZSBDYXNoY3JhZnQgcnVsZXMuXG5cbihUaGUgcnVsZXMgYWJvdmUgZXhpc3QgYmVjYXVzZSBvZiB3aGF0IGhhcHBlbmVkIG9uIHRoZSBmaXJzdCBkYXlzIG9mIHRoZSBvcmlnaW5hbCBTTVApIiwiY29sb3IiOiJyZXNldCJ9XXQA9lsiIix7InRleHQiOiJUaGUgZm9sbG93aW5nIHdpbGwgYmUgc2F2ZWQgd2hlbiB5b3UgbGVhdmUgdGhlIFNNUDoiLCJ1bmRlcmxpbmVkIjp0cnVlLCJjb2xvciI6ImdvbGQifSx7InRleHQiOiJcbiAtIEludmVudG9yeVxuIC0gRW5kZXIgQ2hlc3QocylcbiAtIExvY2F0aW9uL0RpbWVuc2lvblxuIC0gQXJtb3JcbiAtIFdhbGsvRmx5IHNwZWVkXG4gLSBIZWFsdGgvSHVuZ2VyXG4gLSBFZmZlY3RzXG4gIiwiY29sb3IiOiJyZXNldCJ9XXQAglsiIix7InRleHQiOiJJZiB5b3UgaGF2ZSBhbnkgaXNzdWVzLCIsInVuZGVybGluZWQiOnRydWUsImNvbG9yIjoiZ29sZCJ9LHsidGV4dCI6IiBwbGVhc2Ugb3BlbiBhIHRpY2tldC5cblx1MjAwYiIsImNvbG9yIjoicmVzZXQifV1wcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBw";
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(book));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            // Read the serialized inventory
            dataInput.readInt();
            ItemStack b = (ItemStack) dataInput.readObject();
            dataInput.close();
            return b;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    public static void loadEnderChest(String data, Player p) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            // Read the serialized inventory
            int s = dataInput.readInt();
            for (int i = 0; i < (s - 1); i++) {
                p.getEnderChest().setItem(i, (ItemStack) dataInput.readObject());
            }
            
            dataInput.close();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
    
    /**
     * Gets an array of ItemStacks from Base64 string.
     * 
     * <p />
     * 
     * Base off of {@link #fromBase64(String)}.
     * 
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    public static void loadArmor(String data, Player p) throws IOException {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
    
            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
            	items[i] = (ItemStack) dataInput.readObject();
            }
            p.getInventory().setArmorContents(items);
            dataInput.close();
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}