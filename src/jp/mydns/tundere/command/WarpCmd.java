package jp.mydns.tundere.command;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCmd {
	/*こんすとらくたぁ～ここから*/
	private TundereAdmin plugin;
	private CommandSender sender;
	private String[] args;

	public WarpCmd(TundereAdmin plugin, CommandSender sender,
			Command cmd, String label, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.args = args;
	}
	/*こんすとらくたぁ～ここまで*/

	/*PermissionNodeここから*/
	private final String rootPerm = "tundere.warp";
	private final String setPerm = "set";
	private final String toPerm = "to";
	private final String listPerm = "list";
	private final String rmPerm = "remove";
	/*PermissionNodeここまで*/

	public void cmdRun(){
		if (args.length < 1){
			sender.sendMessage("[TundereAdnim] /warp [set|to|list|remove]");
			return;
		}
		//Player以外は処理し…します。　まず第1引数が{set, to, list, remove}かどうかを判定する
		
		if (args[0].equalsIgnoreCase("set")){
			if (chkPerm(rootPerm  + setPerm) == false) return; 
			if (sender instanceof Player){
				if (args.length == 2){
					setWarp((Player)sender, args[1]);
				}else{
					((Player)sender).sendMessage(ChatColor.RED + "[TundereAdmin] 引数が異常です!");
				}
			}else{
				sender.sendMessage("[TundereAdmin] このコマンドはPlayer専用です。");
			}
			
		}else if (args[0].equalsIgnoreCase("to")){
			if (chkPerm(rootPerm  + toPerm) == false) return; 

			
		}else if(args[0].equalsIgnoreCase("list")){
			if (chkPerm(rootPerm  + listPerm) == false) return; 

			
		}else if(args[0].equalsIgnoreCase("remove")){
			if (chkPerm(rootPerm  + rmPerm) == false) return; 

			
		}else{

		}
	}


	/**
	 * 権限ないよm9用処理
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
	 * ワープポイント設定
	 * @param pl :Player
	 * @param locName :設定名
	 */
	public void setWarp(Player pl, String locName){
		Location here = pl.getLocation();
		int x, y, z;
		x = here.getBlockX();
		y = here.getBlockY();
		z = here.getBlockZ();
		plugin.getWarpLocConfig().getConfig().set(locName + ".x", x);
		plugin.getWarpLocConfig().getConfig().set(locName + ".y", y);
		plugin.getWarpLocConfig().getConfig().set(locName + ".z", z);
		plugin.getWarpLocConfig().saveConfig();
	}
}












