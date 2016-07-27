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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.OptionStatus;

public final class Main implements Listener {
	
	public int state = 0; //LOBBY;
	//public UIManager ui; //COMMENTED OUT FOR NOW
	public ArrayList<Player> IngameUsers = new ArrayList<Player>();
	public ArrayList<Player> ReadyUsers = new ArrayList<Player>();
	
	public static io.github.lucemans.main.Main main;
	
	public int time_left = 0;
	
	public String gameWinner = "";
	
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
    	for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    		//if(player.getWorld().getName() == "ImmoParkour") { //TODO replace world vert. with game status check
    			//player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6*20*2000, 10));
    			player.setFoodLevel(9);
    			player.setSaturation(9.5f);
    			player.setGlowing(false);
    			
    			if (state == 8 && player.getName().equals(gameWinner)){
    				player.setGlowing(true);
    			}
    			
    			if (state == 7){//IF INGAME FINISH //TODO: GAME FINISH
    				if (player.getLocation().getBlockX() >= 125 && player.getLocation().getBlockX() <= 127 && player.getWorld().equals(Bukkit.getWorld("ImmoParkour"))) {
    					player.teleport(new Location(player.getWorld(), 130, 123, -2, 90, 2));
    					state = 8;
    					Bukkit.broadcastMessage(player.getName() + " Won the Game");
    					gameWinner = player.getName();
    					for (Player target : Bukkit.getServer().getOnlinePlayers()){
    						if (!target.getName().equals(player.getName())){
    							target.teleport(player.getLocation());
    						}
    					}
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
    	    			
    					for(Player target : Bukkit.getServer().getOnlinePlayers()) {
    	    					//target.teleport(new Location(Bukkit.getWorld("ImmoParkour"), 1, 226, -1, 90, 2));
    	    					target.teleport(world.getSpawnLocation());
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
    	}
    	if (state == 2){
    		if (i == IngameUsers.size()){
    			time_left = 10; //TODO:EDIT TIME in seconds
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
    	} 
    }
    
   protected void secondTimer(){ 
		if (state == 3){
			if (time_left > 0){
				time_left -= 1;
			for (Player target : Bukkit.getServer().getOnlinePlayers()) {
				target.setExp(1f);
				target.setLevel(time_left);
				//target.sendMessage(time_left + " Left");
		}
		}
			else
			{
				state = 4;
				main.getLogger().info("TIMES OVER");
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.teleport(new Location(Bukkit.getWorld("ImmoParkour"), 1, 226, -1, 91, 2));
				}
			}
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
    	}
    	}
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	if (state > 0) {
    	event.setDeathMessage("");
    	}
    	if (state == 7){
    		event.getEntity().setGameMode(GameMode.SPECTATOR); //PLEASE WORK
    	}
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
    	main.getLogger().info("CMD");
    	
    	if (cmd.getName().equalsIgnoreCase("imcreateworld")) {
    		World world = Bukkit.createWorld(new WorldCreator("im"));
    		((Player) sender).teleport(world.getSpawnLocation());
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("imunloadworld")) {
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
    		return true;
    		}
    		//HelloWorldMenu menu = new HelloWorldMenu(this);
    		//ui.showMenu((Player) sender, menu);
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("ready")){
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
    	}
    	
    	//IMR
    	if (cmd.getName().equalsIgnoreCase("imr")) {
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
    			return false;
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
    	}
    }
}