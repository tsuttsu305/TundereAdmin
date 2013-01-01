package jp.mydns.tundere.command;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
		
		
	}
	
}
