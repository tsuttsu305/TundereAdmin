package jp.mydns.tundere.command;

import java.util.Set;

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
	private final String setPerm = ".set";
	private final String toPerm = ".to";
	private final String listPerm = ".list";
	private final String rmPerm = ".remove";
	private final String reloadPrem = ".reload";
	/*PermissionNodeここまで*/
	
	public void cmdRun(){
		if (args.length < 1){
			sender.sendMessage(ChatColor.GOLD + "[TundereAdnim] /warp [set | to | list | remove]");
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
			if (args.length == 1){
				warpList("1", sender);
			}else if (args.length == 2){
				warpList(args[1], sender);
			}else{
				sender.sendMessage(ChatColor.RED + "[TundereAdmin] 引数が異常です。");
			}
			
			
		}else if(args[0].equalsIgnoreCase("remove")){
			if (chkPerm(rootPerm  + rmPerm) == false) return; 
			if (args.length == 2){
				rmWarpPoint(args[1], sender);
			}else{
				((Player)sender).sendMessage(ChatColor.RED + "[TundereAdmin] 引数が異常です!");
			}
			
			
		}else if(args[0].equalsIgnoreCase("reload")){
			if (chkPerm(rootPerm  + reloadPrem) == false) return; 
			plugin.getWarpLocConfig().loadConfig();
			sender.sendMessage(ChatColor.GREEN + "[TundereAdmin] Warp Config Reload SUCCESS!");
			
			
		}else{
			if (getWarpConfig().isConfigurationSection(args[0])){
				if (chkPerm(rootPerm  + toPerm) == false) return; 
				if (sender instanceof Player){
					
					if (args.length == 1){
						doWarp(args[0], (Player)sender);
					}else{
						((Player)sender).sendMessage(ChatColor.RED + "[TundereAdmin] 引数が異常です!");
					}
					
				}else{
					sender.sendMessage("[TundereAdmin] このコマンドはPlayer専用です。");
				}
			}else{
				sender.sendMessage(ChatColor.GOLD + "[TundereAdnim] /warp [set | to | list | remove]");
			}
		}
	}
	
	/**
	 * 登録されたWarpPoint消去
	 * @param locKey :WarpPointName
	 * @param sender :CommandSender
	 */
	public void rmWarpPoint(String locKey, CommandSender sender){
		//場所が登録されているかどうか取得　ない場合はErrorを返す
		if (!getWarpConfig().isConfigurationSection(locKey)){
			sender.sendMessage(ChatColor.RED + "[TundereAdmin] その場所は登録されていません。");
			return;
		}
		
		//削除
		getWarpConfig().set(locKey, null);
		plugin.getWarpLocConfig().saveConfig();
		
		//完了メッセージ
		sender.sendMessage(ChatColor.GREEN + "[TundereAdmin] WarpPoint:" + ChatColor.GOLD + locKey + ChatColor.GREEN + "を削除しました");
	}
	
	/**
	 *Warpポイントのリストを表示させる
	 *@param args :番号
	 *@param sender :Sender
	 */
	public void warpList(String arg, CommandSender sender){
		Set<String> setlist = getWarpConfig().getKeys(false);
		String[] list = (String[]) setlist.toArray(new String[0]);
		int page;
		try {
			page = Integer.parseInt(arg);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "[TundereAdmin] 引数が異常です。");
			return;
		}
		
		//pageが負or0の場合は拒否
		if (page < 1){
			sender.sendMessage(ChatColor.RED + "[TundereAdmin] ページは1以上の値を入力してください。");
			return;
		}
		
		
		//項目数取得
		int index = list.length;
		//総ページ数取得。1ページあたり5つのpoint
		int maxPage;
		if (index % 5 == 0){
			maxPage = index / 5;
		}else{
			maxPage = (index - (index % 5)) /5 + 1;
		}
		
		//最大ページ超えてないかCheck
		if (page > maxPage){
			sender.sendMessage(ChatColor.RED + "[TundereAdmin] Listの最大ページを超えています!");
			return;
		}
		
		//表示開始位置取得(配列用名ので0スタート)
		int start = page * 5 - 5;
		
		
		//表示
		sender.sendMessage(ChatColor.GREEN + "[TundereAdmin] WarpPointList(" + page + "/" + maxPage + ")");
		for (int i = 0;i <=4; i++ ){
			//最終ページで存在しないものが出た場合
			if (list.length - 1 < i + start){
				break;
			}
			int show = start + i + 1;
			String key = list[start + i];
			String pointName = key;
			String xyz = getWarpConfig().getInt(key + ".x") + ", " + getWarpConfig().getInt(key + ".y") + ", " + getWarpConfig().getInt(key + ".z");
			String worldName = getWarpConfig().getString(key + ".world");
			
			sender.sendMessage(ChatColor.GREEN + "" +  show + ". " + ChatColor.GOLD +  pointName + ChatColor.AQUA + ":" + xyz + " - " + worldName);
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