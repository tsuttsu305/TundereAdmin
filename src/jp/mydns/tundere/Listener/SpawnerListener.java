package jp.mydns.tundere.Listener;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
/**
 * 
 * @author tsuttsu305 (SpawnerListener.java)
 *
 */
public class SpawnerListener implements Listener{
	/*こんすとらくたぁ～ここから*/
	private TundereAdmin plugin = null;
	
	public SpawnerListener(TundereAdmin plugin) {
		this.plugin = plugin;
	}
	/*こんすとらくたぁ～ここまで*/
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMobSpawn(CreatureSpawnEvent event){
		//スポナー以外から湧いたら処理をしない
		if (event.getSpawnReason() != SpawnReason.SPAWNER) return;
		
		//湧いた位置のBlockを取得
		Block spawnBl = event.getLocation().getBlock();
		
		//スポナー検索
		int x, y, z;
		Block spawner = null;
		for (x = -4; x <=4;x++){
			for (z = -4;z<=4;z++){
				for(y = -1;y<=1;y++){
					if (spawnBl.getRelative(x, y, z).getType().getId() == Material.MOB_SPAWNER.getId()){
						spawner = spawnBl.getRelative(x, y, z);
						break;
					}
				}
			}
		}
		
		
		//もし見つけられなかった場合
		if (spawner == null){
			plugin.getLogger().info("[TundereAdmin] スポナーからMobが出現しましたが、スポナーの位置を特定出来ません。"
					+ event.getLocation().getBlockX() + ", "
					+ event.getLocation().getBlockY() + ", "
					+ event.getLocation().getBlockZ());
			return;
		}
		
		//Configに記録
		setSPBList(spawner);
		
		//Stopフラグがtrueの場合はcancel
		Location loc = spawner.getLocation();
		int x1 = loc.getBlockX(), y1 = loc.getBlockY(), z1 = loc.getBlockZ();
		String locStr =loc.getWorld().getName() + "." +  x1 + "," + y1 + "," + z1;
		if (getConfig().getBoolean(locStr + ".stop")){
			event.setCancelled(true);
		}
	}
	
	/**
	 * Player Join Flag Reset 
	 * @param PlayerJoinEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		plugin.getspawnerFlagMap().put(event.getPlayer(), false);
		plugin.getSpawnerName().put(event.getPlayer(), "NoName");
	}
	
	
	/**
	 * 
	 * @param PlayerInteractEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerClick(PlayerInteractEvent event){
		//登録処理判定
		if (plugin.getspawnerFlagMap().get(event.getPlayer()) == false)return;
		//右クリック以外は弾く
		if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		//スポナー以外は弾く
		if (!(event.getClickedBlock().getType().getId() == Material.MOB_SPAWNER.getId())) return;
		
		Block spawner = event.getClickedBlock();
		int x = spawner.getLocation().getBlockX(), y = spawner.getLocation().getBlockY(), z = spawner.getLocation().getBlockZ();
		String path =spawner.getLocation().getWorld().getName() + "." +  x + "," + y + "," + z;
		
		//すでに誰かに登録されていた場合
		if (!getConfig().getString(path).equalsIgnoreCase("none")){
			event.getPlayer().sendMessage(ChatColor.RED + "[TundereAdmin]すでに登録されています!");
			return;
		}
		
		//登録
		getConfig().set(path + ".owner", event.getPlayer().getName());
		getConfig().set(path + ".name", plugin.getSpawnerName().get(event.getPlayer()));
		getConfig().set(path + ".stop", true);
		
	}
	
	
	/**
	 * スポナー位置記録<br>
	 * 存在しない場合と位置が違う場合のみ書き込みを行う
	 * @param block Spawner
	 */
	public void setSPBList(Block block){
		Location loc = block.getLocation();
		int x = loc.getBlockX(), y = loc.getBlockY(), z = loc.getBlockZ();
		String locStr =loc.getWorld().getName() + "." +  x + "," + y + "," + z;
		if (!getConfig().isConfigurationSection(locStr)){
			getConfig().createSection(locStr);
			getConfig().set(locStr + ".x", loc.getBlockX());
			getConfig().set(locStr + ".y", loc.getBlockY());
			getConfig().set(locStr + ".z", loc.getBlockZ());
			getConfig().set(locStr + ".name", "none");
			getConfig().set(locStr + ".owner", "none");
			getConfig().set(locStr + ".stop", false);
			getConfig().set(locStr + ".flag.secret", false);
			plugin.getSpawnerListConfig().saveConfig();
		}else{
			if (getConfig().getInt(locStr + ".x") != loc.getBlockX() || 
					getConfig().getInt(locStr + ".y") != loc.getBlockY() || 
					getConfig().getInt(locStr + ".z") != loc.getBlockZ()){
				
				getConfig().set(locStr + ".x", loc.getBlockX());
				getConfig().set(locStr + ".y", loc.getBlockY());
				getConfig().set(locStr + ".z", loc.getBlockZ());
				plugin.getSpawnerListConfig().saveConfig();
			}
		}
		
		
	}
	
	
	/**
	 * 
	 * @return Spawner List Config
	 */
	private FileConfiguration getConfig(){
		return plugin.getSpawnerListConfig().getConfig();
	}
	
}

/*
 * x,z +-4
 * y +-1
 * 
 * Player 半径 15.5
 * ライトレベル7
 */
