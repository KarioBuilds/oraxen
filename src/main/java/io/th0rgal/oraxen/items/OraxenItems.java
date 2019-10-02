package io.th0rgal.oraxen.items;

import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.settings.ResourcesManager;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OraxenItems {

    //configuration sections : their OraxenItem wrapper
    private static Map<String, Item> map = new HashMap<>();
    public static final NamespacedKey ITEM_ID = new NamespacedKey(OraxenPlugin.get(), "id");

    public static void loadItems(JavaPlugin plugin) {

        File itemsFolder = new File(plugin.getDataFolder(), "items");
        if (!itemsFolder.exists()) {
            itemsFolder.mkdirs();
            new ResourcesManager(plugin).extractConfigsInFolder("items", "yml");
        }

        for (File file : itemsFolder.listFiles())
            if (file.getName().endsWith(".yml")) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                for (String itemSectionName : config.getKeys(false)) {
                    ConfigurationSection itemSection = config.getConfigurationSection(itemSectionName);
                    map.put(itemSectionName, new ItemParser(itemSection).buildItem());
                }
            }
    }

    public static String getIdByItem(Item item) {
        return item.getCustomTag(ITEM_ID, PersistentDataType.STRING);
    }

    public static String getIdByItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || item.getItemMeta().getPersistentDataContainer().isEmpty())
            return null;
        else
            return item.getItemMeta()
                    .getPersistentDataContainer()
                    .get(ITEM_ID, PersistentDataType.STRING);
    }

    public static boolean isAnItem(String itemID) {
        return map.containsKey(itemID);
    }

    public static Item getItemById(String id) {
        return map.get(id);
    }

    public static Collection<Item> getItems() {
        return map.values();
    }

    public static Set<Map.Entry<String, Item>> getEntries() {
        return map.entrySet();
    }

    public static Set<String> getSectionsNames() {
        return map.keySet();
    }

}