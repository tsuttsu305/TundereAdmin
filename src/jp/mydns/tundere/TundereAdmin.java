package jp.mydns.tundere;

import java.util.logging.Logger;

import jp.mydns.tundere.command.MsgCmd;
import jp.mydns.tundere.command.TundereAdminCmd;
import jp.mydns.tundere.command.WarpCmd;
import jp.mydns.tundere.config.WarpLocList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class TundereAdmin extends JavaPlugin {
	public static TundereAdmin plugin;
	Logger logger = Logger.getLogger("Minecraft");
	
	//config
	private WarpLocList warpLoc;
	
	@Override
	public void onEnable(){
		eventRegister();
		
		//config
		warpLoc = new WarpLocList(this, "warpLoclist.yml");
		warpLoc.loadConfig();
	}
	
	@Override
	public void onDisable(){
		
	}
	
	/**
	 * イベント登録用メソッド
	 */
	private void eventRegister(){
		
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("tundereadmin")){//alias: tadmin
			TundereAdminCmd ta = new TundereAdminCmd(this, sender, cmd, label, args);
			ta.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("afk")){
			
		}else if (cmd.getName().equalsIgnoreCase("spawn")){
			
		}else if (cmd.getName().equalsIgnoreCase("warp")){
			WarpCmd wc = new WarpCmd(this, sender, cmd, label, args);
			wc.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("spawnblock")){//alias: spb
			
		}else if (cmd.getName().equalsIgnoreCase("msg")){
			MsgCmd mc = new MsgCmd(this, sender, cmd, label, args);
			mc.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("tundereworld")){//alias: tw
			
		}
		return false;
	}

	public WarpLocList getWarpLocConfig() {
		return warpLoc;
	}
	
}










