package io.github.lucemans.immortalized;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main implements Listener {
	
	public int state = 0; //LOBBY;
	//public UIManager ui; //COMMENTED OUT FOR NOW
	public ArrayList<Player> IngameUsers = new ArrayList<Player>();
	public ArrayList<Player> ReadyUsers = new ArrayList<Player>();
	public ArrayList<Player> Spectators = new ArrayList<Player>();
	public ArrayList<Player> Finished = new ArrayList<Player>();
	
	public static io.github.lucemans.main.Main main;
	
	public int time_left = 0;
	public int time_endleft = 0;
	
	public String gameWinner = "";
	
	public boolean nightvision = true;
	
    public Main(io.github.lucemans.main.Main plugin) {
    	
    	main = plugin;
    	
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    	
        plugin.getLogger().info("Server Immortalized :P");
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
		public void run() {
			tickMethod();
		}}, 0, 2);
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
		public void run() {
			secondTimer();
		}}, 0, 20);
    }
    
    protected void tickMethod() {
    	if (state > 0){
    	int i = 0;
    	int k = 0;
    	int l = 0;
    	for(Player player : IngameUsers) {
    			if (nightvision){
    			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6*20*2000, 10));
    			}
    			else
    			{
    				if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
    					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    				}
    			}
    			player.setFoodLevel(9);
    			player.setSaturation(9.5f);
    			player.setGlowing(false);
    			
    			/*
    			if (state == 8 && player.getName().equals(gameWinner)){
    				player.setGlowing(true);
    			}
    			*/
    			
    			//TODO: WAIT FOR USER
    			if (state == 7 || state == 8){
    				if (player.getLocation().getBlockX() >= 125 && player.getLocation().getBlockX() <= 127 && player.getWorld().equals(Bukkit.getWorld("ImmoParkour")) && !Spectators.contains(player) && IngameUsers.contains(player)) {
    					player.teleport(new Location(player.getWorld(), 130, 123, -2, 90, 2));
    					Finished.add(player);
    					
    					if (state == 7){
    						//TIMER
    						state = 8;
    						for (Player p : IngameUsers){
    							p.sendMessage("15 seconds till game end");
    						}
    						new BukkitRunnable()
    	    				{
    	    					@Override
    	    					public void run()
    	    					{
    	    						for (Player p : IngameUsers){
    	    							p.sendMessage("10 seconds till game end");
    	    						}
    	    						new BukkitRunnable()
    	    	    				{
    	    	    					@Override
    	    	    					public void run()
    	    	    					{
    	    	    						for (Player p : IngameUsers){
    	    	    							p.sendMessage("5 seconds till game end");
    	    	    						}
    	    	    						new BukkitRunnable()
    	    	    	    				{
    	    	    	    					@Override
    	    	    	    					public void run()
    	    	    	    					{
    	    	    	    						for (Player p : IngameUsers){
    	    	    	    							p.sendMessage("Game Ended");
    	    	    	    						}
    	    	    	    						
    	    	    	    						Player highest = null;
    	    	    	    						
    	    	    	    						for (Player p : Finished){
    	    	    	    							if (highest == null){
    	    	    	    								highest = p;
    	    	    	    							}
    	    	    	    							else
    	    	    	    							{
    	    	    	    								if (p.getHealth() > highest.getHealth()){
    	    	    	    									highest = p;
    	    	    	    								}
    	    	    	    								if (p.getHealth() == highest.getHealth()){
    	    	    	    									highest = p;
    	    	    	    								}
    	    	    	    							}
    	    	    	    						}
    	    	    	    						
    	    	    	    						gameWinner = highest.getName();
    	    	    	    						
    	    	    	        					time_endleft = 10;
    	    	    	        					state = 9;
    	    	    	        					Bukkit.broadcastMessage(player.getName() + " Won the Game");
    	    	    	        					for (Player target : IngameUsers){
    	    	    	        						if (!target.getName().equals(player.getName())){
    	    	    	        							if (!Finished.contains(target)){
    	    	    	        							target.setGameMode(GameMode.ADVENTURE);
    	    	    	        							target.setHealth(target.getMaxHealth());
    	    	    	        							target.teleport(player.getLocation());
    	    	    	        							}
    	    	    	        						}
    	    	    	        					}//EMD
    	    	    	    					}
    	    	    	    				}.runTaskLater(main, 5*20);
    	    	    					}
    	    	    				}.runTaskLater(main, 5*20);
    	    					}
    	    				}.runTaskLater(main, 5*20);
    					}
    					
    					/*//TODO: WIN
    					time_endleft = 10;
    					state = 8;
    					Bukkit.broadcastMessage(player.getName() + " Won the Game");
    					gameWinner = player.getName();
    					for (Player target : IngameUsers){
    						if (!target.getName().equals(player.getName())){
    							target.setGameMode(GameMode.ADVENTURE);
    							target.setHealth(target.getMaxHealth());
    							target.teleport(player.getLocation());
    						}
    					}
    					*/
    				}
    			}
    			
    			if (state == 1){
    				if (IngameUsers.size() == ReadyUsers.size()){
    					state = 2;
    					if (Bukkit.getWorld("im") != null){
    						Bukkit.getPluginCommand("imunloadworld").execute(Bukkit.getConsoleSender(), "imunloadworld", new String[]{""});
    					}
    					
    		    		World world = Bukkit.createWorld(new WorldCreator("im"));
    		    		world.setGameRuleValue("CommandBlockOutput", "false");
    		    		world.setGameRuleValue("keepinventory", "true");
    	    			
    					for(Player target : IngameUsers) {
    	    					//target.teleport(new Location(Bukkit.getWorld("ImmoParkour"), 1, 226, -1, 90, 2));
    	    					target.teleport(world.getSpawnLocation());
    	    					target.setGameMode(GameMode.SURVIVAL);
    	    					target.setBedSpawnLocation(player.getWorld().getSpawnLocation());
    	    					target.performCommand("spawnpoint");
    	    					removeAllImmortals(target);
    	    			}
    				}
    			}
    			
    			if (state == 2){
    				if (player.getWorld().getName().equals("im")){
    					i += 1;
    				}
    			}
    			if (state == 4){
    				if (player.getWorld().getName().equals("ImmoParkour")){
    					k += 1;
    				}
    			}
    			if (state == 5){ //TODO REPLACE WOOL
    				state = 6;
    				
					World world = Bukkit.getWorld("ImmoParkour");
					for (int z = -4; z <= 2; z++) {
						for (int y = 226; y <= 230; y++) {
							Block block = world.getBlockAt(-3, y, z);
							block.setType(Material.WOOL);
							MaterialData data = block.getState().getData();
							Wool wool = (Wool) data;
							wool.setColor(DyeColor.BLACK);
						}//-3 226 2 //-3 23- -4
					}
					
    				new BukkitRunnable()
    				{
    					@Override
    					public void run()
    					{
    						state = 7;
    						//BLOCK REPLACEMENT
    						World world = Bukkit.getWorld("ImmoParkour");
    							for (int z = -4; z <= 2; z++) {
    								for (int y = 226; y <= 230; y++) {
    									world.getBlockAt(-3, y, z).setType(Material.AIR);
    								}//-3 226 2 //-3 23- -4
    							}
    					}
    				}.runTaskLater(main, 5*20);
    			}
    			if (player.getWorld().getName().equals("ImmoLobby") && !player.getGameMode().equals(GameMode.ADVENTURE)){
    				player.setGameMode(GameMode.ADVENTURE);
    			}
    			if (player.getWorld().getName().equals("ImmoParkour") && !player.getGameMode().equals(GameMode.ADVENTURE) && !player.getGameMode().equals(GameMode.SPECTATOR)){
    				player.setGameMode(GameMode.ADVENTURE);
    			}
    			if (player.getWorld().getName().equals("im") && !player.getWorld().equals(GameMode.SURVIVAL)) {
    				player.setGameMode(GameMode.SURVIVAL);
    			}
    			if (player.getGameMode().equals(GameMode.SPECTATOR)){
    				l += 1;
    			}
    	}
    	if (state == 2){
    		if (i == IngameUsers.size()){
    			time_left = 60*5; //TODO:EDIT TIME in seconds
    			state = 3;
    			main.getLogger().info("All players Succesfully Arived");
    			Bukkit.broadcastMessage("You are now Vurnerable");
    		}
    	}
    	if (state == 4){
    		if (k == IngameUsers.size()){
    			state = 5;
    			main.getLogger().info("All players Succesfully Arived");
    		}
    	}
    	if (state == 7 || state == 8){
    		if (l == IngameUsers.size()){
    			state = 0; //EVERYONE DIED
    			for (Player target : IngameUsers){
    				target.sendMessage("Nobody became the Immortal Master..... stay determined");
					removeAllImmortals(target);
					target.setGlowing(false);
					target.setHealth(target.getMaxHealth());
					target.setGameMode(GameMode.ADVENTURE);
					target.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
    			}
				IngameUsers.clear();
				ReadyUsers.clear();
				Spectators.clear();
				Finished.clear();
    		}
    	}
    	} 
    	
		if (IngameUsers.size() <= 1 && state > 0){
			main.getLogger().info("USR");
			state = 0;
			for (Player p : main.getServer().getOnlinePlayers()) {
				p.sendMessage("The Game has ended due to people that are too afraid and left :(..... sorry for this problem");
				main.getLogger().info("send msg to " + p.getName());
				removeAllImmortals(p);
				p.setGlowing(false);
				p.setHealth(p.getMaxHealth());
				p.setGameMode(GameMode.ADVENTURE);
				p.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
			}
			IngameUsers.clear();
			ReadyUsers.clear();
			Finished.clear();
			main.state = "Lobby";
		}
    }
    
   protected void secondTimer(){ 
		if (state == 3){
			if (time_left > 0){
				time_left -= 1;
			for (Player target : IngameUsers) {
				target.setExp(1f);
				target.setLevel(time_left);
				//target.sendMessage(time_left + " Left");
			}
			if (IngameUsers.size() <= 1){
					state = 0;
					for (Player player : IngameUsers) {
						player.sendMessage("The Game has ended due to people that are too afraid and left :(..... sorry for this problem");
						removeAllImmortals(player);
						player.setGlowing(false);
						player.setHealth(player.getMaxHealth());
						player.setGameMode(GameMode.ADVENTURE);
						player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
					}
					IngameUsers.clear();
					ReadyUsers.clear();
					Finished.clear();
					main.state = "Lobby";
			}
		}
			else
			{
				state = 4;
				main.getLogger().info("TIMES OVER");
				for (Player player : IngameUsers) { //TODO:
					player.teleport(new Location(Bukkit.getWorld("ImmoParkour"), 1, 226, -1, 91, 2));
					player.setGameMode(GameMode.ADVENTURE);
					player.getInventory().clear();
				}
			}
		}
		if (state == 9){
			if (time_endleft > 0){
				time_endleft -= 1;
			for (Player target : IngameUsers) {
				target.setExp(1f);
				target.setLevel(time_endleft);
			}
			}
			else
			{
				state = 0;
				for (Player player : IngameUsers) {
					removeAllImmortals(player);
					player.setGlowing(false);
					player.setHealth(player.getMaxHealth());
					player.setGameMode(GameMode.ADVENTURE);
					player.teleport(Bukkit.getWorld("Lobby").getSpawnLocation());
				}
				IngameUsers.clear();
				ReadyUsers.clear();
				Finished.clear();
				main.state = "Lobby";
			}
		}
	}
   
   public void onPlayerLeave(PlayerQuitEvent event)
   {
	   main.getLogger().info("LEFT");
	   if (IngameUsers.contains(event.getPlayer())){
		   IngameUsers.remove(event.getPlayer());
	   }
	   
	   if (Spectators.contains(event.getPlayer())){
		   Spectators.remove(event.getPlayer());
	   }
	   
	   if (ReadyUsers.contains(event.getPlayer())){
		   ReadyUsers.remove(event.getPlayer());
	   }
	   
	   if (Finished.contains(event.getPlayer())){
		   Finished.remove(event.getPlayer());
	   }
   }
   
   public void removeAllImmortals(Player target){
	   for (DamageCause cause: DamageCause.values()) {
		   Bukkit.getPluginCommand("imr").execute(Bukkit.getConsoleSender(), "imr", new String[]{target.getName(), cause.toString()});
	   }
   }
   
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
    	if (state == 1 || state == 2){
    	event.setCancelled(true);
    	return;
    	}
    	
    	if (state >= 0){
    	DamageCause cause = event.getCause();
    	if (event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
    		
    		//ALS NIET DOOD KAN
    		if (player.hasMetadata(cause.toString()) || cause.toString().equals("ENTITY_ATTACK")){
    			event.setCancelled(true);
    		}
    		
    		if (state == 3 && player.getHealth() - event.getFinalDamage() <= 0 && !player.hasMetadata(cause.toString())){ //IF THE FUCKING PLAYER DIES
    			if (cause.toString() != "VOID" && cause.toString() != "CUSTOM"){
    				if (cause.toString() == "FIRE_TICK" || cause.toString() == "FIRE" || cause.toString() == "LAVA") {
        				player.setMetadata("FIRE", new FixedMetadataValue(main, true));
        				player.setMetadata("FIRE_TICK", new FixedMetadataValue(main, true));
        				player.setMetadata("LAVA", new FixedMetadataValue(main, true));
        				main.getLogger().info("[DEATH]: " + player.getName().toString() + " Died by " + cause.toString());
        				player.sendMessage("[DEATH]: You became inmune to " + cause.toString());
        				for(Player target : Bukkit.getOnlinePlayers()) {
        					if (!target.equals(player)){
        						target.sendMessage("[Immortalize]: " + player.getName() + " became Immortal to " + "BURN" + " Damage");
        					}
        				}
    				}
    				else
    				{
        				player.setMetadata(cause.toString(), new FixedMetadataValue(main, true));
        				main.getLogger().info("[DEATH]: " + player.getName().toString() + " Died by " + cause.toString());
        				player.sendMessage("[DEATH]: You became inmune to " + cause.toString());
        				for(Player target : Bukkit.getOnlinePlayers()) {
        					if (!target.equals(player)){
        						target.sendMessage("[Immortalize]: " + player.getName() + " became Immortal to " + cause.toString() + " Damage");
        					}
        				}
    				}
    			}
    		}
        	if (state == 7 && player.getHealth() - event.getFinalDamage() <= 0 && !player.hasMetadata(cause.toString())){
        		player.setGameMode(GameMode.SPECTATOR); //PLEASE WORK //TODO
        		player.sendMessage("You died you are now in Spectator Mode");
        		Spectators.add(player);
        		event.setCancelled(true);
        	}
    	}
    	}
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	if (state > 0) {
    	event.setDeathMessage("");
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
    	main.getLogger().info("CMD");
    	
    	if (cmd.getName().equalsIgnoreCase("imvision")){
    		nightvision = !nightvision;
    		String nvstr = "Bugged";
    		if (nightvision == true){
    			nvstr = "Enabled";
    		}
    		else
    		{
    			nvstr = "Disabled";
    		}
    		sender.sendMessage("Immortal NightVision is now " + nvstr);
    		return true;
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("imcreateworld")) {
    		if (sender instanceof Player){
    		if (!((Player) sender).hasPermission("immortalized.createworld")){
    			return false;
    		}
    		}
    		World world = Bukkit.createWorld(new WorldCreator("im"));
    		((Player) sender).teleport(world.getSpawnLocation());
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("imunloadworld")) {
    		if (sender instanceof Player) {
    		if (!((Player) sender).hasPermission("immortalized.unloadworld")){
    			return false;
    		}
    		}
    		//World world = Bukkit.getWorld("im");
    		try
            {
                File folder = Bukkit.getWorld("im").getWorldFolder();
                Bukkit.unloadWorld("im", false);
                FileUtils.deleteDirectory(folder);
            }
            catch (IOException e)
            {
                main.getLogger().severe("Failed to delete world folder: " + e.getMessage());
                e.printStackTrace();
            }
    	}
    	
    	//IMjoin
    	if (cmd.getName().equalsIgnoreCase("imjoin")) {
    		if (sender instanceof Player){
    			return false;
    		}
    		
    		if (state == 0){
    			state = 1;
    			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    				if (player.getWorld().getName().equals("Lobby")){
    					IngameUsers.add(player);
    					player.setHealth(player.getMaxHealth());
    					removeAllImmortals(player);
    					player.teleport(new Location(Bukkit.getWorld("ImmoLobby"),0, 65, 0, 0, 0));
    					player.getInventory().clear();
    					player.setGameMode(GameMode.ADVENTURE);
    					main.getLogger().info("Teleporting players to Lobby");
    					player.sendMessage("You Joined Immortualize. Type /ready if you are ready");
    				}
    			}
    		return true;
    		}
    		//HelloWorldMenu menu = new HelloWorldMenu(this);
    		//ui.showMenu((Player) sender, menu);
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("ready")){
    		//if (((Player) sender).hasPermission("immortalized.ready")){
    		if (state == 1 || state == 2){
    		if (IngameUsers.contains((Player) sender)){
    			if (!ReadyUsers.contains((Player) sender)){
    				ReadyUsers.add((Player) sender);
    				if (IngameUsers.size() - ReadyUsers.size() <= 0){
    					Bukkit.broadcastMessage("Everyone Ready......Lets Start");
    				}
    				else
    				{
    					Bukkit.broadcastMessage(IngameUsers.size() - ReadyUsers.size() + " more user need to become ready");
    				}
    				return true;
    			}
    			ReadyUsers.remove((Player) sender);
    			((Player) sender).sendMessage("Ok..... your not Ready I guess");
    			Bukkit.broadcastMessage(((Player) sender).getName() + " Is not Ready Yet");
    		return true;
    		}
    		else
    		{
    			((Player) sender).sendMessage("You are Not Ingame");
    			return true;
    		}
    		}
			((Player) sender).sendMessage("You are Not Ingame");
    		return true;
    		//}
    		//else
    		//{
    		//	return false; //not perm
    		//}
    	}
    	
    	//IMR
    	if (cmd.getName().equalsIgnoreCase("imr")) {
    		if (sender instanceof Player){
    		if (!((Player) sender).hasPermission("immortalized.imr")){
    			return true;
    		}
    		}
    		if(args[0].equalsIgnoreCase("all")){
    	    	for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    	    		if (args[1] == "FIRE" || args[1] == "FIRE_TICK" || args[1] == "LAVA"){
    	    			player.removeMetadata("FIRE", main);
    	    			player.removeMetadata("FIRE_TICK", main);
    	    			player.removeMetadata("LAVA", main);
    	    		}
    	    		else
    	    		{
    	    		player.removeMetadata(args[1], main);
    	    		}
    	    	}
    	    	return true;
    		}
    		Player target = Bukkit.getServer().getPlayer(args[0]);
    		if (target == null)
    		{
    			sender.sendMessage(args[0] + " is Dead");
    			return true;
    		}
    		
    		if (args[1] == "FIRE" || args[1] == "FIRE_TICK" || args[1] == "LAVA"){
    			target.removeMetadata("FIRE", main);
    			target.removeMetadata("FIRE_TICK", main);
    			target.removeMetadata("LAVA", main);
    			return true;
    		}
    		
    		target.removeMetadata(args[1], main);
    		return true;
    	}
    	return false;
    }
    
    @EventHandler
    public void onRespawn(PlayerRespawnEvent event){
    	if (state == 3){
    	event.getPlayer().teleport(Bukkit.getWorld("im").getSpawnLocation());    
    	event.getPlayer().setGameMode(GameMode.SURVIVAL);
    	}
    }
    
    public void start(){
		if (state == 0){
			state = 1;
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				if (player.getWorld().getName().equals("Lobby")){
					IngameUsers.add(player);
					player.setHealth(player.getMaxHealth());
					removeAllImmortals(player);
					player.teleport(new Location(Bukkit.getWorld("ImmoLobby"),0, 65, 0, 0, 0));
					main.getLogger().info("Teleporting players to Lobby");
					player.sendMessage("You Joined Immortualize. Type /ready if you are ready");
				}
			}
		}
    }
    
    public void joinSpectator(Player player){
    	
    }
}