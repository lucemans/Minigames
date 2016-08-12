package io.github.lucemans.main;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.lucemans.config.SLAPI;

public final class Main extends JavaPlugin implements Listener 
{	
	public io.github.lucemans.immortalized.Main immo;
	public io.github.lucemans.bookofwritings.Main bow;
	public io.github.lucemans.config.Main config;
	public io.github.lucemans.config.SLAPI slapi;
	public io.github.lucemans.main.ScoreboardManager SBM;
	public static io.github.lucemans.main.InventoryManager im;
	
	public String state = "Lobby";
	public Inventory voteInv;
	
	public int online_players = 0;

	private boolean voting = false;
	
	public HashMap<String, Player> BypassUsers = new HashMap<>();
	
	@Override
    public void onEnable()
    {
		immo = new io.github.lucemans.immortalized.Main(this);
		bow = new io.github.lucemans.bookofwritings.Main(this);
		config = new io.github.lucemans.config.Main(this);
		slapi = new io.github.lucemans.config.SLAPI(this);
		SBM = new io.github.lucemans.main.ScoreboardManager(this);
		im = new io.github.lucemans.main.InventoryManager(this);
		
		SLAPI.loadBalances();
		Bukkit.getPluginManager().registerEvents(this, this);
		
		getLogger().info("MAIN 2nd WORKS");
		
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		public void run() {
			tickMethod();
		}}, 0, 2);
        
        for (Player player : Bukkit.getOnlinePlayers()){
			player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
			player.setGlowing(false);
			player.getPlayer().setHealth(player.getMaxHealth());
			player.getPlayer().setGameMode(GameMode.ADVENTURE);
			player.setSaturation(1000f);
			player.getPlayer().setFoodLevel(20);
			player.setLevel(0);
			player.setExp(1f);
			player.getPlayer().performCommand("spawnpoint");
			player.setMetadata("vote", new FixedMetadataValue(this, ""));
			SBM.update(player);
        }
    }
	
	@Override
	public void onDisable()
	{
		SLAPI.saveBalances();
	}
	
	public String getVotes()
	{
		int immoint = 0;
		int vengfullint = 0;
		int switchint = 0;
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
				if(player.getMetadata("vote").get(0).asString().equals("switchstr"))
				{
					switchint = switchint + 1;
				}
			}
		}
		int num = getLargestKey(immoint, vengfullint, switchint);
		getLogger().info(num + " !");
		if(num == 1)
		{
			vote = "immostr";
		}
		if(num == 2)
		{
			vote = "vengfullstr";
		}
		if(num == 3){
			vote = "switchstr";
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
		if (player.hasMetadata("vote")){
		if (player.getMetadata("vote").get(0).asString().equals(string))
		{
			player.setMetadata("vote", new FixedMetadataValue(this, ""));
			player.sendMessage("you changed your mind");
			return;
		}
		}
		
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
		if(line2.equals("Switching Sides"))
		{
			vote(player, "switchstr");
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
	
	public int getLargestKey(int ... nums)//3 5 8 1 //TODO this is broken :| just returning Immortal
	{
		int max = 0;
		int num = 0;
		for(int i = 0; i < nums.length; i++)
		{
			if(nums[i] > max)
			{
				getLogger().info(i + ":hey " + nums[i] + " is higher than " + max);
				max = nums[i];
				num = i;
			}
		}
		num++;
		return num;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event){
		//immo.onPlayerLeave(event);
		
		   getLogger().info("LEFT2");
		   if (immo.IngameUsers.contains(event.getPlayer())){
			   getLogger().info("removed ingame");
			   immo.IngameUsers.remove(event.getPlayer());
		   }
		   
		   if (immo.Spectators.contains(event.getPlayer())){
			   getLogger().info("spec removed");
			   immo.Spectators.remove(event.getPlayer());
		   }
		   
		   if (immo.ReadyUsers.contains(event.getPlayer())){
			   getLogger().info("redy removed");
			   immo.ReadyUsers.remove(event.getPlayer());
		   }
		   
		   if (immo.Finished.contains(event.getPlayer())){
			   getLogger().info("finish removed");
			   immo.Finished.remove(event.getPlayer());
		   }
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lobby")){
			state = "lobby";
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("bypass")){
			if (BypassUsers.containsKey(sender.getName())){
				BypassUsers.remove(sender.getName());
				sender.sendMessage("You are not bypassing things anymore");
			}
			else
			{
				BypassUsers.put(sender.getName(), (Player) sender);
				sender.sendMessage(ChatColor.GRAY + "You obtained " + ChatColor.DARK_AQUA + "the permission to bypass random things");
				sender.sendMessage(ChatColor.LIGHT_PURPLE + " #POKEMON");
			}
			return true;
		}
		
		boolean immoreturn = immo.onCommand(sender, cmd, label, args);
		
		if (immoreturn == true){
			return true;
		}
		
		boolean configreturn = config.onCommand(sender, cmd, label, args);
		
		if (configreturn == true){
			return true;
		}
		
		boolean InventoryManagerreturn = im.onCommand(sender, cmd, label, args);
		
		if (InventoryManagerreturn == true) {
			return true;
		}
		
		sender.sendMessage("Command Execution Failed");
		return true;
		
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!io.github.lucemans.config.Main.hasAccount(event.getPlayer().getName())){
			getLogger().info(event.getPlayer().getName() + " has been added to the economy");
			io.github.lucemans.config.Main.bal.put(event.getPlayer().getName(), 200D);
		}
		
		SBM.update(event.getPlayer());
		
		//TELEPORTS
		if (state.equals("Lobby")){
			event.getPlayer().teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
			event.getPlayer().setGlowing(false);
			event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
			event.getPlayer().setGameMode(GameMode.ADVENTURE);
			event.getPlayer().setSaturation(1000f);
			event.getPlayer().setFoodLevel(20);
			event.getPlayer().setLevel(0);
			event.getPlayer().setExp(1f);
			event.getPlayer().performCommand("spawnpoint");
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
		int switchint = 0;
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
				if(player.getMetadata("vote").get(0).asString().equals("switchstr"))
				{
					switchint = switchint + 1;
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
		
		if (Bukkit.getWorld("Lobby").getBlockAt(new Location(Bukkit.getWorld("Lobby"), -19, 67, 3)).getState() instanceof Sign) {
			//getLogger().info("SIGN");
			Sign sign = ((Sign) Bukkit.getWorld("Lobby").getBlockAt(new Location(Bukkit.getWorld("Lobby"), -19, 67, 3)).getState());
			sign.setLine(2, "VOTES: " + switchint);
			sign.update();
			sign.update(true);
		}
		//End
		
		online_players = 0;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			online_players += 1;
			if (player.getWorld().getName().equals("Lobby")) {
				if (player.getLocation().getBlockY() <= 60){
					if (!BypassUsers.containsKey(player.getName())){
					player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
					player.setGlowing(false);
					player.getPlayer().setHealth(player.getMaxHealth());
					player.getPlayer().setGameMode(GameMode.ADVENTURE);
					player.setSaturation(1000f);
					player.getPlayer().setFoodLevel(20);
					player.setLevel(0);
					player.getPlayer().setExp(1f);
					player.getPlayer().performCommand("spawnpoint");
					}
				}
			}
			if (player.getSaturation() < 100f){
				player.setSaturation(1000f);
			}
		}
		
		if (immoint + vengfullint + switchint == online_players && state == "Lobby" && online_players > 1){
			int num = getLargestKey(immoint, vengfullint, switchint);
			getLogger().info(num + " !");
			if(num == 1)
			{
				//JOIN IMORTALIZED
				   Bukkit.getPluginCommand("imjoin").execute(Bukkit.getConsoleSender(), "imjoin", new String[]{});
				   state = "Immortalized";
				   //remove all votes/
				   for (Player player : Bukkit.getOnlinePlayers()){
					   if (player.hasMetadata("vote")){
						   player.removeMetadata("vote", this);
					   }
				   }
			}
			if(num == 2)
			{
				//"vengfullstr"; JOIN
			}
			if(num == 3)
			{
				//"switch" join;
			}
		}
	}//End Tick event
}//End of Class

