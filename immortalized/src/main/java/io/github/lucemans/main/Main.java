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
		Bukkit.getPluginManager().registerEvents(this, this);
		
		getLogger().info("MAIN 2nd WORKS");
    }
	
	public String getVotes()
	{
		int immoint = 0;
		int vengfullint = 0;
		String vote = "";
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasMetadata("vote"))
			{
				getLogger().info(player.getName() + " has vote Metadata");
				if(player.getMetadata("vote").get(0).asString().equals("immostr"))
				{
					//getLogger().info("immostring found");
					immoint = immoint + 1;
				}
				if(player.getMetadata("vote").get(0).asString().equals("vengfullstr"))
				{
					//getLogger().info("vengfullstring found");
					vengfullint = vengfullint + 1;
				}
				player.sendMessage("FID: " + player.getMetadata("vote").get(0).asString());
			}
		}
		Bukkit.broadcastMessage("IMMORTAL: " + immoint + " VENGFULL: " + vengfullint);
		int num = getLargestKey(immoint, vengfullint);
		++num;
		if(num == 1)
		{
			vote = "immostr";
		}
		if(num == 2)
		{
			vote = "vengfullstr";
		}
		Bukkit.broadcastMessage("num - " + num);
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
			player.sendMessage("right click block");//RIGHT CLICK
			Block block = event.getClickedBlock();
			if(block.getState() instanceof Sign)
			{
				player.sendMessage("SIGN"); //SIGN
				Sign sign = (Sign) block.getState();
				player.sendMessage("rightlickEvent");
				playerRightClickSign(player, sign);
				player.sendMessage("DONE");
			}
		}
	}
	
	public void vote(Player player, String string)
	{
		if (player.hasMetadata("vote")){player.removeMetadata("vote", this);}
		player.setMetadata("vote", new FixedMetadataValue(this, string));
		player.sendMessage("you voted for " + string);
	}
	
	public void playerRightClickSign(Player player, Sign sign)
	{
		player.sendMessage("-");
		String line1 = sign.getLine(0);
		String line2 = sign.getLine(1);
		String line3 = sign.getLine(2);
		String line4 = sign.getLine(3);
		
		player.sendMessage("SIGN1: " + line1 + "SIGN2: " + line2);
		
		if(line1.equals("Immortalized"))
		{
			player.sendMessage("IMMORTALIZED");
			vote(player, "immostr");
			
		}
		if(line1.equals("Vengfullone"))
		{
			player.sendMessage("VENGFULLONE");
			vote(player, "vengfullstr");
		}
		if(line1.equals("topvotes")){
			player.sendMessage(getVotes());
		}
		if(this.voting != false)
		{
			this.voting = true;
		}
	}
	
	public int getLargestKey(int ... nums)//3 5 8 1
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

