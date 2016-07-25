package io.github.lucemans.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.lucemans.main.Main;

public abstract class Menu
{
	private List<Button> buttons = new ArrayList<>();
	private String title = "<TO BE SET>";
	private HashMap<Integer, Button> buttonMap = new HashMap<>();
	protected Main plugin;
	protected Inventory inventory;
	
	protected Menu(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public Main getPlugin()
	{
		return plugin;
	}
	
	/**
	 * Get the buttons that are displayed by this menu
	 * @return A list of buttons that are displayed
	 */
	public List<Button> getButtons()
	{
		return buttons;
	}
	
	/**
	 * Get the title of this menu that is being displayed at the top of the screen.
	 * @return The title of the menu
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * Add a button to this menu.
	 * @param button The button to add to this menu
	 */
	protected void addButton(Button button)
	{
		buttons.add(button);
	}
	
	/**
	 * Set the title that this menu will display when it's opened.
	 * @param title The title that will be displayed when the menu is opened 
	 */
	protected void setTitle(String title)
	{
		this.title = title;
	}
	
	/**
	 * Set the inventory that this menu is connected to.
	 * @param inventory The inventory this menu is connected to
	 */
	protected void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	/**
	 * Get the inventory this menu is connected to.
	 * @return The inventory this menu is connected to
	 */
	protected Inventory getInventory()
	{
		return inventory;
	}
	
	/**
	 * Draw a border in the inventory.
	 * @param inventory The inventory in which to draw a border
	 * @param itemstack The itemstack that should be used for the border
	 */
	protected void drawBorder(Inventory inventory, ItemStack itemstack)
	{
		int width = 9;
		int height = inventory.getSize() / width;
		
		for (int i = 0; i < width; i++)
		{
			inventory.setItem(i, itemstack);
			inventory.setItem(i + width*(height-1), itemstack);
		}
		
		for (int i = 1; i < height-1; i++)
		{
			inventory.setItem(i*width, itemstack);
			inventory.setItem(i*width+(width-1), itemstack);
		}
	}
	
	/**
	 * Draw a border in the inventory.
	 * @param inventory The inventory in which to draw a border
	 */
	@SuppressWarnings("deprecation")
	protected void drawBorder(Inventory inventory)
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		item.setDurability(DyeColor.BLUE.getData());
		drawBorder(inventory, item);
	}
	
	/**
	 * Draw all buttons in the inventory.
	 * @param player The player who opened the inventory
	 * @return The inventory in which all buttons have been rendered.
	 */	
	public Inventory drawMenu(Player player)
	{
		Inventory inventory = Bukkit.createInventory(player, 6*9, getTitle());
		
		drawBorder(inventory);
		
		int index = 0;
		for (Button button : getButtons())
		{
			int x = index % 7;
			int y = index / 7;
			index++;
			int location = (x + 1) + ((y+1) * 9);
			inventory.setItem(location, button.asItemStack());
			buttonMap.put(location, button);
		}
		
		return inventory;
	}
	
	/**
	 * Is called when a slot has been clicked.
	 * @param location The index of the clicked slot
	 */
	public void buttonClicked(int location)
	{
		if (buttonMap.containsKey(location))
		{
			Button button = buttonMap.get(location);
			button.getCallback().run();
		}
	}
}














