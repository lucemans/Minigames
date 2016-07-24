package io.github.lucemans.ui;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import io.github.lucemans.immortalized.Main;

public class UIManager implements Listener
{
	private Main plugin;
	private HashMap<Inventory, Menu> openMenus = new HashMap<>();
	
	/**
	 * Create a new instance of the UIManager class
	 * @param plugin The plugin that controls the manager
	 */
	public UIManager(Main plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Get the plugin that controls this manager
	 * @return The plugin that controls this manager
	 */
	public Main getPlugin()
	{
		return plugin;
	}
	
	/**
	 * Show a menu to a player
	 * @param player The player who should see the menu
	 * @param menu The menu to display
	 */
	public void showMenu(Player player, Menu menu)
	{
		Inventory inventory = createInventory(player, menu);
		openMenus.put(inventory, menu);
		player.openInventory(inventory);
	}
	
	/**
	 * Get the menu that is represented by an inventory.
	 * @param inventory The inventory to retrieve the menu from
	 * @return The menu or <code>null</code> if the inventory is not a menu
	 */
	public Menu getMenu(Inventory inventory)
	{
		return openMenus.get(inventory);
	}
	
	/**
	 * Check if an inventory is actually a menu.
	 * @param inventory The inventory to check
	 * @return <code>true</code> if the inventory is a menu, <code>false</code> otherwise
	 */
	public boolean isMenu(Inventory inventory)
	{
		return openMenus.containsKey(inventory);
	}

	/**
	 * Creates an inventory based on a menu
	 * @param player The player who has opened the menu
	 * @param menu The menu to create the inventory from
	 * @return The newly created inventory
	 */
	private Inventory createInventory(Player player, Menu menu)
	{
		Inventory inventory = menu.drawMenu(player);
		return inventory;
	}
	
	/**
	 * Clean up the memory left over by a menu.
	 * @param inventory The inventory that represents the menu to be cleaned up
	 */
	private void cleanupMenu(Inventory inventory)
	{
		if (isMenu(inventory))
		{
			openMenus.remove(inventory);
		}
	}
	
	/**
	 * Call this method when an item has been clicked in the inventory.
	 * @param inventory The inventory that has been clicked
	 * @param index The slot index that has been clicked
	 */
	private void clickButton(Inventory inventory, int index)
	{
		Menu menu = getMenu(inventory);
		menu.buttonClicked(index);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClicked(InventoryClickEvent event)
	{
		if (!event.isCancelled())
		{
			Inventory inventory = event.getInventory();
			if (isMenu(inventory))
			{
				event.setCancelled(true);
				int location = event.getSlot();
				clickButton(inventory, location);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClosed(InventoryCloseEvent event)
	{
		Inventory inventory = event.getInventory();
		if (isMenu(inventory))
		{
			cleanupMenu(inventory);
		}
	}
}


















