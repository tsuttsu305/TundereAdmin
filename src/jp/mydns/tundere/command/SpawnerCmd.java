package jp.mydns.tundere.command;

import java.util.HashMap;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
/**
 * 
 * @author tsuttsu305
 *
 */
public class SpawnerCmd {
	/*こんすとらくたぁ～ここから*/
	private TundereAdmin plugin;
	private CommandSender sender;
	private String[] args;
	
	public SpawnerCmd(TundereAdmin plugin, CommandSender sender,
			Command cmd, String label, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.args = args;
	}
	/*こんすとらくたぁ～ここまで*/
	
	/*PermissionNodeここから*/
	final String rootPerm = "tundere.spawner";
	final String regPerm = ".reg";
	final String adminPerm  = ".admin";
	/*PermissionNodeここまで*/
	
	public void cmdRun(){
		if (args.length < 1){
			sender.sendMessage("[TundereAdmin] /spawner [ reg | flag | list ]");
			return;
		}
		
		
		if (args[0].equalsIgnoreCase("reg")){
			if (chkPerm(rootPerm + regPerm) == false) return;
			if (args.length == 2){
				
			}
			
			
		}else if (args[0].equalsIgnoreCase("flag")){
			 
		}else if (args[0].equalsIgnoreCase("list")){
			
		}
	}
	
	/**
	 * 権限ないよm9用処理
	 * @param perm PermissionNode
	 */
	public boolean chkPerm(String perm){
		
		if (!(sender instanceof Player)){
			return true;
		}
		if (((Player)sender).hasPermission(perm)) return true;
		
		sender.sendMessage(ChatColor.RED + "[TundereAdmin] 権限がありません。");
		return false;
	}
	
	/**
	 * 
	 * @return Spawner Flag HashMap
	 */
	public HashMap<Player, Boolean> getFlag(){
		return plugin.getspawnerFlagMap();
	}
	
}
