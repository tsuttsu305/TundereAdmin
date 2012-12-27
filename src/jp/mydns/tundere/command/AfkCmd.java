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
public class AfkCmd {
	/*こんすとらくたぁ〜ここから*/
	private TundereAdmin plugin;
	private CommandSender sender;
	private String[] args;
	
	public AfkCmd(TundereAdmin tundereAdmin, CommandSender sender, Command cmd,
			String label, String[] args) {
		this.plugin = tundereAdmin;
		this.sender = sender;
		this.args = args;
	}
	/*こんすとらくたぁ〜ここまで*/
	
	/*PermissionNodeここから*/
	private final String rootPrem = "tundere.afk";
	private final String usePerm = ".use";
	private final String listPerm = ".list";
	/*PermissionNodeここまで*/
	
	
	public void cmdRun(){
		if (args.length < 1){
			if (!(sender instanceof Player)){
				sender.sendMessage("[TundereAdmin] このコマンドはPlayer専用です。");
				return;
			}
			if (chkPerm(rootPrem + usePerm)){
				setAFK((Player)sender);
			}
		}else if(args[0].equalsIgnoreCase("list")){
			if (chkPerm(rootPrem + listPerm)){
				afkList(sender);
			}
		}else{
			if (chkPerm(rootPrem + usePerm)){
				setAFK((Player)sender, args);
			}
		}
	}
	
	/**
	 * AFK パラメータセット
	 * @param player AFKにするPlayer
	 */
	public void setAFK(Player player){
		getStatus().put(player, true);
		plugin.getServer().broadcastMessage( 
				ChatColor.AQUA + player.getName() + ChatColor.GOLD + " is " + 
				ChatColor.RED + "AFK");
	}
	
	/**
	 * AFK パラメータセット
	 * @param player AFKにするPlayer
	 * @param args サーバ全体通知メッセージ
	 */
	public void setAFK(Player player, String[] args){
		getStatus().put(player, true);
		String msg = "";
		
		//String配列を1つに
		for (int i = 0;i < args.length;i++){
			msg = msg + args[i] + " ";
		}
		
		//サーバ全体に通知
		plugin.getServer().broadcastMessage( 
				ChatColor.AQUA + player.getName() + ChatColor.GOLD + " is " + 
				ChatColor.RED + "AFK" + ChatColor.AQUA +  ">" + msg);
	}
	
	
	/**
	 * AFK list表示
	 * @param sender
	 */
	public void afkList(CommandSender sender){
		Player[] onlinePlayers = plugin.getServer().getOnlinePlayers();
		String list = " ";
		
		for(int i = 0;i <onlinePlayers.length;i++){
			if (getStatus().get(onlinePlayers[i])){
				if (i == onlinePlayers.length-1){
					list = list + onlinePlayers[i].getName();
				}else{
					list = list + onlinePlayers[i].getName() + ", ";
				}
			}
		}
		
		sender.sendMessage(ChatColor.AQUA + "AFK List: " + ChatColor.RED + list);
	}
	
	
	/**
	 * 
	 * @return TundereAdmin.afkStatusMap
	 */
	private HashMap<Player, Boolean> getStatus(){
		return plugin.getAfkStatusMap();
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
	
}
