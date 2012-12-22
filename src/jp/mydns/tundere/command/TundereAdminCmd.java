package jp.mydns.tundere.command;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class TundereAdminCmd {
	/*コンストラクタここから*/
	private TundereAdmin plugin;
	private CommandSender sender;
	private Command cmd ;
	private String label;
	private String[] args;
	
	/**
	 * TundereAdminCmd (TundereAdminCmd.java)
	 * @author tsuttsu305
	 */
	public TundereAdminCmd(TundereAdmin plugin, CommandSender sender,
			Command cmd, String label, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
	}
	/*コンストラクタここまで*/
	
	/*PermissionNodeここから*/
	private final String rootPerm = "tundere.admin";
	private final String helpPerm = ".help";
	/*PermissionNodeここまで*/
	
	
	public void cmdRun(){
		//引数なしの場合、helpコマンドの紹介
		if (args.length < 1){
			if (sender instanceof Player){
				((Player)sender).sendMessage(ChatColor.LIGHT_PURPLE + "[TundereAdmin] /tundereadmin help コマンドを使用してください!");
			}else{
				sender.sendMessage("[TundereAdmin] Show help command  /tundereadmin help");
			}
			return;
		}
		
		//第一引数で比較
		if (args[0].equalsIgnoreCase("help")){
			if (isHasPerm(helpPerm, sender)){
				//TODO: Helpメッセージ
				if (sender instanceof Player){
					//TODO: そのうち権限別にhelpメッセージの内容を変える。そのうち
					Player player = (Player)sender;
					player.sendMessage(ChatColor.GREEN + "[TundereCmd] TundereAdminPlugin Ver" + plugin.getDescription().getVersion());
					player.sendMessage(ChatColor.GREEN + "[TundereCmd] コマンド一覧。(そのうち作る)");
				}else{
					sender.sendMessage("[TundereCmd] TundereAdminPlugin Ver" + plugin.getDescription().getVersion());
					sender.sendMessage("[TundereCmd] コマンド一覧。(そのうち作る)");
				}
				return;
			}
		}
		
	}
	
	/**
	 * Playerにかかわらず権限を判定する<br/>
	 * Player以外の場合はtrueを返す
	 */
	private boolean isHasPerm(String node, CommandSender sender){
		if (!(sender instanceof Player)){
			return true;
		}else if (((Player)sender).hasPermission(rootPerm + node)){
			return true;
		}
		return false;
	}
}
