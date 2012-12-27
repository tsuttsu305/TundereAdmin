package jp.mydns.tundere.Listener;

import jp.mydns.tundere.TundereAdmin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * 
 * @author tsuttsu305
 *
 */
public class AFKListener implements Listener {
	private TundereAdmin plugin = null;
	
	public AFKListener(TundereAdmin tundereAdmin) {
		this.plugin = tundereAdmin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event){
		if (plugin.getAfkStatusMap().get(event.getPlayer())){
			plugin.getServer().broadcastMessage(ChatColor.AQUA + event.getPlayer().getName() + "はOnlineになりました。");
			plugin.getAfkStatusMap().put(event.getPlayer(), false);
		}
	}
	
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		plugin.getAfkStatusMap().put(event.getPlayer(), false);
	}
}
