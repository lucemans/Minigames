package io.github.lucemans.ui;

import org.bukkit.Material;

import io.github.lucemans.main.Main;

public class HelloWorldMenu extends Menu
{
	public HelloWorldMenu(Main plugin)
	{
		super(plugin);
		
		setTitle("Hello world!");
		addButton(new Button(plugin, "Hello world!", Material.COBBLESTONE));
	}
}
