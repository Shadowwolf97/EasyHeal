package me.shadowwolf97.EasyHeal;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	ArrayList<String> enabled = new ArrayList<String>();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		System.out.println("Easy Heal Enabled!");
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("god") || cmd.getName().equalsIgnoreCase("g")) {
			if(args.length == 0) {
				if(s.hasPermission("easyheal.god.self")) {
					if(enabled.contains(s.getName())) {
						s.sendMessage(ChatColor.RED + "God mode disabled!");
						enabled.remove(s.getName());
					}else {
						s.sendMessage(ChatColor.GREEN + "God mode enabled!");
						enabled.add(s.getName());
					}
				}
			}else {
				if(s.hasPermission("easyheal.god.other")) {
					OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
					if(!op.isOnline()) {
						s.sendMessage(ChatColor.RED + "Player is not online!");
						return true;
					}
					
					Player p = (Player)op;
					
					if(enabled.contains(p.getName())) {
						p.sendMessage(ChatColor.RED + s.getName() + " has disabled god mode for you.");
						s.sendMessage(ChatColor.GOLD + "You have disabled " + p.getName() + "'s god mode.");
						enabled.remove(p.getName());
					}else {
						p.sendMessage(ChatColor.GREEN + s.getName() + " has enabled god mode for you.");
						s.sendMessage(ChatColor.GOLD + "You have enabled " + p.getName() + "'s god mode.");
						enabled.add(p.getName());
					}
				}
			}
		}else if(cmd.getName().equalsIgnoreCase("heal") || cmd.getName().equalsIgnoreCase("h")) {
			if(args.length == 0) {
				if(s.hasPermission("easyheal.heal.self")) {
					Player p = (Player) s;
					p.setHealth(20);
					p.setFoodLevel(20);
				}
			}else {
				OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
				if(!op.isOnline()) {
					s.sendMessage(ChatColor.RED + "Player not online.");
					return true;
				}
				Player p = (Player)op;
				p.setHealth(20);
				p.setFoodLevel(20);
				s.sendMessage(ChatColor.GOLD + "You healed " + p.getName());
				p.sendMessage(ChatColor.GREEN + "You were healed by " + s.getName());
			}
		}
		return true;
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if(enabled.contains(p.getName())) {
			e.setCancelled(true);
		}
	}
}
