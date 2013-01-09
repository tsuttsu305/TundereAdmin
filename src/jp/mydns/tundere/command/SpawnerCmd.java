package jp.mydns.tundere.command;

import java.util.HashMap;
import jp.mydns.tundere.TundereAdmin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
			//Player以外は弾く
			if (!(sender instanceof Player)){
				sender.sendMessage("[TundereAdmin] Player専用です。");
				return;
			}
			
			if (args.length == 2){
				String pointName = args[1];
				Player player = (Player)sender;
				//すでに使われてないかCheck
				if (isUsePointName(pointName) == false){
					player.sendMessage(ChatColor.RED + "[TundereAdmin] その名前はすでに使われています!");
					return;
				}
				
				if (getFlag().get(player) == null){
					getFlag().put(player, false);
				}
				
				//すでにtrueの場合は処理しない。スケジュール量産防止
				if (getFlag().get(player) == true){
					player.sendMessage(ChatColor.RED + "[TundereAdmin] すでに登録可能状態です。スポナーを右クリックしてください。");
					return;
				}
				
				//30秒でフラグを戻す
				getFlag().put(player, true);
				player.sendMessage(ChatColor.GREEN + "[TundereAdmin] 30秒以内に登録するスポナーを素手で右クリックしてください");
				
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					public void run() {
						if (getFlag().get((Player)sender) == true){
							getFlag().put((Player)sender, false);
							((Player)sender).sendMessage(ChatColor.DARK_GREEN + "[TundereAdmin] スポナー登録処理がタイムアウトしました。");
						}
					}
				}, 20L*30L);
				
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
	
	/**
	 * 
	 * @return Spawner Point List Config
	 */
	private FileConfiguration getConfig(){
		return plugin.getSpawnerListConfig().getConfig();
	}
	
	/**
	 * そのPointNameが使用されているかどうか確認する
	 * @param name 使用されているか確認する名前
	 * @return true = 使用済み
	 */
	public boolean isUsePointName(String name){
		String[] rootKeys = (String[]) getConfig().getKeys(false).toArray(new String[0]);
		for (int i = 0;i < rootKeys.length;i++){
			if (getConfig().getString(rootKeys[i] + ".name").equalsIgnoreCase(name)){
				return true;
			}
		}
		
		return false;
	}
}