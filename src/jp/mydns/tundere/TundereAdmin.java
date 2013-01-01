package jp.mydns.tundere;

import java.util.HashMap;
import java.util.logging.Logger;

import jp.mydns.tundere.Listener.AFKListener;
import jp.mydns.tundere.Listener.SpawnerListener;
import jp.mydns.tundere.command.AfkCmd;
import jp.mydns.tundere.command.MsgCmd;
import jp.mydns.tundere.command.SpawnerCmd;
import jp.mydns.tundere.command.TundereAdminCmd;
import jp.mydns.tundere.command.WarpCmd;
import jp.mydns.tundere.config.YAMLManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author tsuttsu305
 *
 */
public class TundereAdmin extends JavaPlugin {
	public static TundereAdmin plugin;
	Logger logger = Logger.getLogger("Minecraft");
	
	//config
	private YAMLManager warpLoc, spawnerloc;
	private HashMap<Player, Boolean> afkStatus = new HashMap<Player, Boolean>();
	private HashMap<Player, Boolean> spawnerRegFlag = new HashMap<Player, Boolean>();
	
	
	@Override
	public void onEnable(){
		eventRegister();//イベント登録
		
		//configLoad
		//warpList
		warpLoc = new YAMLManager(this, "warpLoclist.yml");
		warpLoc.loadConfig();
		logger.info("[TundereAdmin] Load warpLocList.yml");
		//SpawnerList
		spawnerloc = new YAMLManager(this, "spawnerlist.yml");
		spawnerloc.loadConfig();
		logger.info("[TundereAdmin] Load spawnerlist.yml");
		
		tempMapsReset();
	}
	
	@Override
	public void onDisable(){
		
	}
	
	/**
	 * イベント登録用メソッド
	 */
	private void eventRegister(){
		getServer().getPluginManager().registerEvents(new AFKListener(this), this);
		getServer().getPluginManager().registerEvents(new SpawnerListener(this), this);
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("tundereadmin")){//alias: tadmin
			TundereAdminCmd ta = new TundereAdminCmd(this, sender, cmd, label, args);
			ta.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("afk")){
			AfkCmd ac = new AfkCmd(this, sender, cmd, label, args);
			ac.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("spawn")){
			
		}else if (cmd.getName().equalsIgnoreCase("warp")){
			WarpCmd wc = new WarpCmd(this, sender, cmd, label, args);
			wc.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("spawner")){//alias: spb
			SpawnerCmd sc = new SpawnerCmd(this, sender, cmd, label, args);
			sc.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("msg")){
			MsgCmd mc = new MsgCmd(this, sender, cmd, label, args);
			mc.cmdRun();
			return true;
		}else if (cmd.getName().equalsIgnoreCase("tundereworld")){//alias: tw
			
		}
		return false;
	}
	
	
	/**
	 * 
	 * @return Warp Point List Config
	 */
	public YAMLManager getWarpLocConfig() {
		return warpLoc;
	}
	
	
	/**
	 * 
	 * @return Spawner Location List Config
	 */
	public YAMLManager getSpawnerListConfig(){
		return spawnerloc;
	}
	
	/**
	 * 
	 * @return AFK Status HashMap
	 */
	public HashMap<Player, Boolean> getAfkStatusMap(){
		return afkStatus;
	}
	
	/**
	 * 
	 * @return Spaawner Can Regist Flag
	 */
	public HashMap<Player, Boolean> getspawnerFlagMap(){
		return spawnerRegFlag;
	}
	
	private void tempMapsReset(){
		Player[] players = getServer().getOnlinePlayers();
		
		for (int i = 0;i<players.length;i++){
			getAfkStatusMap().put(players[i], false);
			getspawnerFlagMap().put(players[i], false);
		}
	}
}