package io.github.lucemans.main;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener 
{	
	public static io.github.lucemans.immortalized.Main immo;
	
	public String state = "Lobby";

	private boolean voting = false;
	
	@Override
    public void onEnable()
    {
		immo = new io.github.lucemans.immortalized.Main(this);
		
		getLogger().info("MAIN 2nd WORKS");
    }
	
	public String getVotes()
	{
		int immoint = 0;
		String vote = "";
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasMetadata("vote"))
			{
				if(player.getMetadata("vote").equals("immostr"))
				{
					++immoint;
				}
			}
		}
		int num = getLargestKey(immoint);
		if(num == 1)
		{
			vote = "immostr";
		}
		return vote;
	}
	
	public void clearVotes()
	{
		for(Player player: Bukkit.getOnlinePlayers())
		{
			if(player.hasMetadata("vote"))
			{
				player.removeMetadata("vote", this);
			}
		}
		this.voting = false;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		Action action = event.getAction();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block block = event.getClickedBlock();
			if(block.getState() instanceof Sign)
			{
				Sign sign = (Sign) block.getState();
				playerRightClickSign(player, sign);
			}
		}
	}
	
	public void vote(Player player, String string)
	{
		player.setMetadata(string, new FixedMetadataValue(this, "vote"));
	}
	
	public void playerRightClickSign(Player player, Sign sign)
	{
		String line1 = sign.getLine(1);
		String line2 = sign.getLine(2);
		String line3 = sign.getLine(3);
		String line4 = sign.getLine(4);
		
		if(line1.equals("Immortalized"))
		{
			vote(player, "immostr");
		}
		if(this.voting != false)
		{
			this.voting = true;
		}
	}
	
	public int getLargestKey(int ... nums)
	{
		int max = 0;
		int num = 0;
		for(int i: nums)
		{
			if(nums[i] > max)
			{
				max = nums[i];
				num = i;
			}
		}
		return num;
	}
	
	@Override
	public void onDisable()
	{
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return immo.onCommand(sender, cmd, label, args);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
	}
}

