package io.github.lucemans.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener 
{	
	public static io.github.lucemans.immortalized.Main immo;
	
	@Override
    public void onEnable()
    {
		immo = new io.github.lucemans.immortalized.Main(this);
		getLogger().info("MAIN 2nd WORKS");
    }
	
	@Override
	public void onDisable()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return immo.onCommand(sender, cmd, label, args);
	}
	
}

