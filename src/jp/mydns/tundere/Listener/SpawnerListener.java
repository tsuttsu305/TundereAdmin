package jp.mydns.tundere.Listener;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
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
			getConfig().set(locStr + ".stop", false);
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
