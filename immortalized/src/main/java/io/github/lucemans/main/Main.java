package io.github.lucemans.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
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
		
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		public void run() {
			tickMethod();
		}}, 0, 2);
       
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
				if(player.getMetadata("vote").get(0).asString().equals("immostr"))
				{
					immoint = immoint + 1;
				}
				if(player.getMetadata("vote").get(0).asString().equals("vengfullstr"))
				{
					vengfullint = vengfullint + 1;
				}
			}
		}
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
		if (player.hasMetadata("vote")){player.removeMetadata("vote", this);}
		player.setMetadata("vote", new FixedMetadataValue(this, string));
		player.sendMessage("you voted for " + string);
	}
	
	public void playerRightClickSign(Player player, Sign sign)
	{
		String line1 = sign.getLine(0);
		String line2 = sign.getLine(1);
		String line3 = sign.getLine(2);
		String line4 = sign.getLine(3);
		
		if (line1.equals("[VOTE]")){
			getLogger().info("VOTE");
		//player.sendMessage("SIGN1: " + line1 + "SIGN2: " + line2);
		
		if(line2.equals("Immortalized"))
		{
			vote(player, "immostr");
			
		}
		if(line2.equals("The VengFull One"))
		{
			vote(player, "vengfullstr");
		}
		if(line2.equals("topvotes")){
			player.sendMessage(getVotes());
		}
		}
		//TODO: what does this do? -start
		if(this.voting != false)
		{
			this.voting = true;
		}
		//TODO: -end
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
		if (state.equals("Lobby")){
			event.getPlayer().teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
			event.getPlayer().setGlowing(false);
			event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			event.getPlayer().setSaturation(10f);
			event.getPlayer().setFoodLevel(20);
		}
		if (state.equals("Immortalized")){
			immo.joinSpectator(event.getPlayer());
		}
	}
	
	//TICK METHOD
	private void tickMethod(){
		//GET VOTES -Start
		int immoint = 0;
		int vengfullint = 0;
		String vote = "";
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player.hasMetadata("vote"))
			{
				if(player.getMetadata("vote").get(0).asString().equals("immostr"))
				{
					immoint = immoint + 1;
				}
				if(player.getMetadata("vote").get(0).asString().equals("vengfullstr"))
				{
					vengfullint = vengfullint + 1;
				}
			}
		}
		//-end
		//Currently we have the values of votes
		//Update the signs -Start
		
		if (Bukkit.getWorld("Lobby").getBlockAt(new Location(Bukkit.getWorld("Lobby"), -19, 67, -1)).getState() instanceof Sign) {
			//getLogger().info("SIGN");
			Sign sign = ((Sign) Bukkit.getWorld("Lobby").getBlockAt(new Location(Bukkit.getWorld("Lobby"), -19, 67, -1)).getState());
			sign.setLine(2, "VOTES: " + vengfullint);
			sign.update();
			sign.update(true);
		}
		
		if (Bukkit.getWorld("Lobby").getBlockAt(new Location(Bukkit.getWorld("Lobby"), -19, 67, 1)).getState() instanceof Sign) {
			//getLogger().info("SIGN");
			Sign sign = ((Sign) Bukkit.getWorld("Lobby").getBlockAt(new Location(Bukkit.getWorld("Lobby"), -19, 67, 1)).getState());
			sign.setLine(2, "VOTES: " + immoint);
			sign.update();
			sign.update(true);
		}
		//End
	}//End Tick event
}//End of Class

