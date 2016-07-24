package io.github.lucemans.immortalized;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Main extends JavaPlugin implements Listener {
	
	public int state = 0; //LOBBY;
	//public UIManager ui; //COMMENTED OUT FOR NOW
	public ArrayList<Player> IngameUsers = new ArrayList<Player>();
	public ArrayList<Player> ReadyUsers = new ArrayList<Player>();
	
    @Override
    public void onEnable() {
    	
    	//ui = new UIManager(this);
    	
    	Bukkit.getPluginManager().registerEvents(this, this);
    	//Bukkit.getPluginManager().registerEvents(ui, this);
    	
        getLogger().info("Server Immortalized :P");
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		public void run() {
			tickMethod();
		}}, 0, 2);
    }
    
    protected void tickMethod() {
    	int i = 0;
    	for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    		//if(player.getWorld().getName() == "ImmoParkour") { //TODO replace world vert. with game status check
    			//player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 6*20*2000, 10));
    			player.setFoodLevel(9);
    			player.setGlowing(true);
    			player.setSaturation(9.5f);
    			
    			if (state == 20){//IF INGAME FINISH
    				if (player.getLocation().getBlockX() >= 125 && player.getLocation().getBlockX() <= 127) {
    					player.teleport(new Location(player.getWorld(), 130, 123, -2));
    				}
    			}
    			
    			if (state == 1){
    				if (IngameUsers.size() == ReadyUsers.size()){
    					state = 2;
    	    			for(Player target : Bukkit.getServer().getOnlinePlayers()) {
    	    					target.teleport(new Location(Bukkit.getWorld("ImmoParkour"), 1, 226, -1, 90, 2));
    	    			}
    				}
    			}
    			
    			if (state == 2){
    				if (player.getWorld().getName().equals("ImmoParkour")){
    					i += 1;
    				}
    			}
    	}
    	if (state == 2){
    		if (i == IngameUsers.size()){
    			state = 3;
    			getLogger().info("DONE");
    		}
    	}
		
	}
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
    	if (state == 1){
    		if (!event.getPlayer().getWorld().getName().equals("ImmoLobby")){
    			event.getPlayer().teleport(new Location(Bukkit.getWorld("ImmoLobby"),0, 65, 0, 0, 0));
    		}
    	}
    	if (state == 0){
    		if (!event.getPlayer().getWorld().getName().equals("Lobby")){
    			event.getPlayer().teleport(new Location(Bukkit.getWorld("Lobby"),0, 65, 0, 0, 0));
    		}
    	}
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
    	DamageCause cause = event.getCause();
    	if (event.getEntity() instanceof Player) {
    		Player player = (Player) event.getEntity();
    		
    		if (player.hasMetadata(cause.toString()) || cause.toString().equals("ENTITY_ATTACK")){
    			event.setCancelled(true);
    		}
    		
    		if (player.getHealth() - event.getFinalDamage() <= 0 && !player.hasMetadata(cause.toString())){ //IF THE FUCKING PLAYER DIES
    			if (cause.toString() != "VOID" && cause.toString() != "CUSTOM"){
    				if (cause.toString() == "FIRE_TICK" || cause.toString() == "FIRE" || cause.toString() == "LAVA") {
        				player.setMetadata("FIRE", new FixedMetadataValue(this, true));
        				player.setMetadata("FIRE_TICK", new FixedMetadataValue(this, true));
        				player.setMetadata("LAVA", new FixedMetadataValue(this, true));
        				getLogger().info("[DEATH]: " + player.getName().toString() + " Died by " + cause.toString());
        				player.sendMessage("[DEATH]: You became inmune to " + cause.toString());
        				for(Player target : Bukkit.getOnlinePlayers()) {
        					if (!target.equals(player)){
        						target.sendMessage("[Immortalize]: " + player.getName() + " became Immortal to " + "BURN" + " Damage");
        					}
        				}
    				}
    				else
    				{
        				player.setMetadata(cause.toString(), new FixedMetadataValue(this, true));
        				getLogger().info("[DEATH]: " + player.getName().toString() + " Died by " + cause.toString());
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
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	event.setDeathMessage("");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){    	
    	getLogger().info("CMD");
    	
    	//IMjoin
    	if (cmd.getName().equalsIgnoreCase("imjoin")) {
    		if (state == 0){
    			state = 1;
    			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    				if (player.getWorld().getName().equals("Lobby")){
    					IngameUsers.add(player);
    					player.teleport(new Location(Bukkit.getWorld("ImmoLobby"),0, 65, 0, 0, 0));
    					getLogger().info("Teleporting players to Lobby");
    					player.sendMessage("You Joined Immortualize. Type /im if you are ready");
    				}
    			}
    		return true;
    		}
    		//HelloWorldMenu menu = new HelloWorldMenu(this);
    		//ui.showMenu((Player) sender, menu);
    	}
    	
    	if (cmd.getName().equalsIgnoreCase("im")){
    		if (!ReadyUsers.contains((Player) sender)){
    			ReadyUsers.add((Player) sender);
    			Bukkit.broadcastMessage(IngameUsers.size() - ReadyUsers.size() + " more user need to become ready");
    			return true;
    		}
    		return true;
    	}
    	
    	//IMR
    	if (cmd.getName().equalsIgnoreCase("imr")) {
    		if(args[0].equalsIgnoreCase("all")){
    	    	for(Player player : Bukkit.getServer().getOnlinePlayers()) {
    	    		if (args[1] == "FIRE" || args[1] == "FIRE_TICK" || args[1] == "LAVA"){
    	    			player.removeMetadata("FIRE", this);
    	    			player.removeMetadata("FIRE_TICK", this);
    	    			player.removeMetadata("LAVA", this);
    	    		}
    	    		else
    	    		{
    	    		player.removeMetadata(args[1], this);
    	    		}
    	    	}
    	    	return true;
    		}
    		Player target = Bukkit.getServer().getPlayer(args[0]);
    		if (target == null){
    			sender.sendMessage(args[0] + " is Dead");
    			return false;
    		}
    		if (args[1] == "FIRE" || args[1] == "FIRE_TICK" || args[1] == "LAVA"){
    			target.removeMetadata("FIRE", this);
    			target.removeMetadata("FIRE_TICK", this);
    			target.removeMetadata("LAVA", this);
    			return true;
    		}
    		target.removeMetadata(args[1], this);
    		return true;
    	}
    	return false;
    }
}
