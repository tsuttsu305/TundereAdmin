package jp.mydns.tundere.command;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MsgCmd {
	/*こんすとらくたぁ～ここから*/
	private TundereAdmin plugin;
	private CommandSender sender;
	private String[] args;

	public MsgCmd(TundereAdmin plugin, CommandSender sender, Command cmd,
			String label, String[] args) {
		this.plugin = plugin;
		this.sender = sender;
		this.args = args;
	}
	/*こんすとらくたぁ～ここまで*/

	/*PermissionNodeここから*/
	private final String rootPerm = "tundere.msg";
	/*PermissionNodeここまで*/

	public void cmdRun(){
		//権限確認　ない場合は弾く。ただ、Playerのみ
		if (sender instanceof Player){
			if (((Player)sender).hasPermission(rootPerm) == false){
				((Player)sender).sendMessage(ChatColor.RED + "[TundereAdmin] あなたは権限を持っていませんm9");
				return;
			}


			//引数足りないとき
			if (args.length <= 1){
				sender.sendMessage("[TundereAdmin] /msg <toPlayer> <メッセージ>");
				return;
			}

			Player player = (Player)sender;
			//宛先Playerが居なかった場合は処理終わる
			String playerName = args[0];
			if (!plugin.getServer().getPlayerExact(playerName).isOnline()){//Onlineじゃない場合
				player.sendMessage(ChatColor.RED + "[TundereAdmin] 宛先Playerがofflineのため送信出来ません><");
				return;
			}

			Player toPlayer = plugin.getServer().getPlayerExact(playerName);
			//Msg生成
			String msg = ChatColor.GRAY + "<from " + player.getName() + "> ";
			for (int i = 1;i < args.length;i++){
				msg = msg + args[i];
			}
			//送信
			toPlayer.sendMessage(msg);

			//完了メッセージ
			player.sendMessage(ChatColor.GREEN + "[TundereAdmin] " + toPlayer.getName() + " にメッセージを送信しました!");
		}
	}
}

























