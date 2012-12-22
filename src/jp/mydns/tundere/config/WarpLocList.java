package jp.mydns.tundere.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import jp.mydns.tundere.TundereAdmin;

public class WarpLocList {
	private TundereAdmin plugin;

	//config関係
	private FileConfiguration conf;
	private File confDir;

	//使用するConfigの名前
	private  String confName;

	/**
	 * 
	 * @param plugins JavaPlugin
	 * @param configName ロードするConfigの名前
	 */
	public WarpLocList(TundereAdmin plugins, String configName) {
		this.plugin = plugins;
		this.confName = configName;
		//configのパスを取得
		this.confDir = plugin.getDataFolder();
	}

	public void loadConfig(){
		//Config.ymlへのパスを取得
		File confPath = new File(confDir, confName);

		//configが存在しなかった場合はjarからコピー
		if (!(confPath.exists())){
			plugin.saveResource(confName, false);
		}
		//configを格納
		conf = YamlConfiguration.loadConfiguration(confPath);
	}
	
	public FileConfiguration getConfig(){
		if (conf == null){
			loadConfig();
		}
		return conf;
	}
	
	public void saveConfig(){
		if (conf == null || confDir == null){
			return;
		}else{
			try {
				File confPath = new File(confDir, confName);
				conf.save(confPath);
			} catch (IOException e) {
				plugin.getLogger().log(Level.WARNING, "Cannot save " + confName + " " + e);
			}
		}
	}
}









