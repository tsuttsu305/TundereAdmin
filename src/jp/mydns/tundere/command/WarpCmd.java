package jp.mydns.tundere.command;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * 
 * @author tsuttsu305
 *
 */
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
	private final String reloadPrem = "reload";
	/*PermissionNodeここまで*/
	
	public void cmdRun(){
		if (args.length < 1){
			sender.sendMessage(ChatColor.GOLD + "[TundereAdnim] /warp [set|to|list|remove]");
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
			if (sender instanceof Player){
				if (args.length == 2){
					doWarp(args[1], (Player)sender);
				}else{
					((Player)sender).sendMessage(ChatColor.RED + "[TundereAdmin] 引数が異常です!");
				}
			}else{
				sender.sendMessage("[TundereAdmin] このコマンドはPlayer専用です。");
			}
			
		}else if(args[0].equalsIgnoreCase("list")){
			if (chkPerm(rootPerm  + listPerm) == false) return; 
			
			
		}else if(args[0].equalsIgnoreCase("remove")){
			if (chkPerm(rootPerm  + rmPerm) == false) return; 
			
			
		}else if(args[0].equalsIgnoreCase("reload")){
			if (chkPerm(rootPerm  + reloadPrem) == false) return; 
			plugin.getWarpLocConfig().loadConfig();
			sender.sendMessage(ChatColor.GREEN + "[TundereAdmin] Warp Config Reload SUCCESS!");
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
		String worldName = here.getWorld().getName();
		x = here.getBlockX();
		y = here.getBlockY();
		z = here.getBlockZ();
		
		getWarpConfig().set(locName + ".x", x);
		getWarpConfig().set(locName + ".y", y);
		getWarpConfig().set(locName + ".z", z);
		getWarpConfig().set(locName + ".world", worldName);
		plugin.getWarpLocConfig().saveConfig();
		
		pl.sendMessage(ChatColor.GREEN + "[TundereAdmin] 保存しました! " + locName + ": " + x + ", " + y + ", " + z);
	}
	
	
	/**
	 * Playerをワープさせる
	 * @param locKey :yml key
	 * @param player :Player
	 */
	public void doWarp(String locKey, Player player){
		//locKeyが登録されているか判定
		if (!getWarpConfig().isConfigurationSection(locKey)){
			player.sendMessage(ChatColor.RED + "[TundereAdmin] その場所は登録されていないようです。");
			return;
		}
		//設定値がおかしい時
		if (!getWarpConfig().isInt(locKey + ".x") || 
				!getWarpConfig().isInt(locKey + ".y") || !getWarpConfig().isInt(locKey + ".z") || 
				!getWarpConfig().isString(locKey + ".world")){
			player.sendMessage(ChatColor.RED + "[TundereAdmin] 設定ファイルに異常があります。");
			return;
		}
		
		
		//設定ファイルから取得
		int x, y, z;
		String worldName = getWarpConfig().getString(locKey + ".world");
		x = getWarpConfig().getInt(locKey + ".x");
		y = getWarpConfig().getInt(locKey + ".y");
		z = getWarpConfig().getInt(locKey + ".z");
		
		//Worldが存在するか判定
		if (plugin.getServer().getWorld(worldName) == null){
			player.sendMessage(ChatColor.RED + "[TundereAdmin] World(" + worldName +  ")が存在しません!");
		}
		
		//移動先Location生成
		Location toLoc = new Location(plugin.getServer().getWorld(worldName), x, y, z);
		
		//移動
		player.teleport(toLoc);
		player.sendMessage(ChatColor.GREEN + "[TundereAdmin] " + locKey + " にワープしました!");
	}
	
	
	/**
	 * Warp地点のConfigファイル呼び出し用
	 * @return WarpLocListConfig
	 */
	private FileConfiguration getWarpConfig(){
		return plugin.getWarpLocConfig().getConfig();
	}
}