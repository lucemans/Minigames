package io.github.lucemans.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.lucemans.main.Main;

public class Button
{
	private Main plugin;
	private String name = "";
	private String description = "";
	private Material icon = Material.LAVA;
	private ItemStack item;
	private BukkitRunnable callback = new BukkitRunnable() {
		@Override
		public void run()
		{
			
		}
	};
	
	public Button(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public Button(Main plugin, String name, Material icon)
	{
		this(plugin);
		setName(name);
		setIcon(icon);
	}
	
	public Button(Main plugin, String name, Material icon, String description)
	{
		this(plugin, name, icon);
		setDescription(description);
	}
	
	public Button(Main plugin, String name, Material icon, BukkitRunnable callback)
	{
		this(plugin, name, icon);
		setDescription(description);
		setCallback(callback);
	}

	public Main getPlugin()
	{
		return plugin;
	}
	
	/**
	 * Set the name this button displays when the mouse is hovered over it.
	 * @param name The name that is displayed in this button's tooltip
	 */
	public void setName(String name)
	{
		this.name = name;
		updateItem();
	}
	
	/**
	 * Set the icon that is displayed for this button.
	 * @param icon The icon that is displayed
	 */
	public void setIcon(Material icon)
	{
		this.icon = icon;
		updateItem();
	}
	
	/**
	 * Set the description that is displayed in the tooltip.
	 * @param description The description that is displayed
	 */
	public void setDescription(String description)
	{
		this.description = description;
		updateItem();
	}
	
	/**
	 * Get the name that is displayed in the button's tooltip.
	 * @return The button that is displayed
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get the icon that is displayed
	 * @return The icon that is displayed
	 */
	public Material getIcon()
	{
		return icon;
	}
	
	/**
	 * Get the description that is displayed in the button's tooltip.
	 * @return The description that is displayed
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * Get the callback that should be executed when the button is pressed.
	 * @return The callback that should be executed when the button is pressed
	 */
	public BukkitRunnable getCallback()
	{
		return callback;
	}

	/**
	 * Set the callback that should be called when the button is pressed.
	 * @param callback The runnable that should be executed when the button pressed
	 */
	public void setCallback(BukkitRunnable callback)
	{
		this.callback = callback;
	}

	/**
	 * Get the button as an itemstack.
	 * The itemstack will have to correct name and description as set by <code>setName</code> and <code>setDescription</code>.
	 * @return The button as an itemstack
	 */
	public ItemStack asItemStack()
	{
		return item;
	}

	/**
	 * Recreate the itemstack
	 */
	private void updateItem()
	{
		item = new ItemStack(icon);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		
		List<String> description = Arrays.asList(getDescription().split("\n"));
		if (description.size() == 1 && description.get(0).isEmpty())
			description = new ArrayList<>();
		
		meta.setLore(description);
		
		item.setItemMeta(meta);
	}
}











