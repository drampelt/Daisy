package ca.drmc.daisy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.drmc.daisy.LazyConfig;

public class Daisy extends JavaPlugin implements Listener {

	private static final Logger log = Logger.getLogger("Minecraft");

	private LazyConfig config;
	private ChatResponder responder;
	
	private String prefix;

	public void onDisable() {
		saveCfg();
	}

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		
		config = new LazyConfig(this);
		if(!config.CONFIG_FILE.exists()){
			// If the config file doesn't exist, save it
			saveCfg();
		}
		loadCfg();
		updatePrefix(config.name);
		
		responder = new ChatResponder(this);
		
		log.info("[" + getDescription().getName() + "] - Enabled!");
	}

	public void updatePrefix(String n) {
		// Replace & with minecraft's colour character and replace +name with the bot name
		prefix = ChatColor.translateAlternateColorCodes('&', config.format.replaceAll("\\+name", config.name));
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		// Remove all characters except letters, numbers, and spaces
		String m = event.getMessage().replaceAll("[^a-zA-Z0-9 ]+","");
		log("Original: " + event.getMessage() + " | Modified: " + m);
		String[] split = m.split(" ");
		// Only respond to messages that end with the bot name
		if(split[split.length-1].equalsIgnoreCase(config.name)){
			responder.respond(event.getPlayer(), m, split);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		responder.playerJoin(event.getPlayer());
	}
	
	// Start config getters/adders/setters
	public String getCfgName() {
		return config.name;
	}

	public void setCfgName(String name) {
		config.name = name;
		updatePrefix(name);
	}

	public String getCfgFormat() {
		return config.format;
	}

	public void setCfgFormat(String format) {
		config.format = format;
	}

	public int getCfgMindelay() {
		return config.mindelay;
	}

	public void setCfgMindelay(int mindelay) {
		config.mindelay = mindelay;
	}

	public int getCfgMaxdelay() {
		return config.maxdelay;
	}

	public void setCfgMaxdelay(int maxdelay) {
		config.maxdelay = maxdelay;
	}
	
	public ConfigPlayer getCfgPlayer(String player){
		return config.players.get(player);
	}
	
	public void addCfgPlayer(String player, ConfigPlayer cp){
		config.players.put(player, cp);
	}
	
	public String getCfgDev(){
		return config.devname;
	}
	// End config getters/adders/setters
	
	public void loadCfg(){
		try {
			config.load(config.CONFIG_FILE);
			updatePrefix(config.name);
		} catch (InvalidConfigurationException e) {
			log.severe("[" + getDescription().getName() + "] - Could not load config!");
			e.printStackTrace();
		}
	}
	
	public void saveCfg(){
		try {
			config.save();
		} catch (InvalidConfigurationException e) {
			log.severe("[" + getDescription().getName() + "] - Could not save config!");
			e.printStackTrace();
		}
	}

	public String getPrefix() {
		return prefix;
	}
	
	public void log(String m){
		log.info(m);
	}
	
	public void logToFile(String message) {
		// Log errors to file
		if(!config.logproblems){
			return;
		}
        try
        {
            File dataFolder = getDataFolder();
            if(!dataFolder.exists())
            {
                dataFolder.mkdir();
            }
 
            File saveTo = new File(getDataFolder(), "log.txt");
            if (!saveTo.exists())
            {
                saveTo.createNewFile();
            }
 
 
            Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ");
            
            FileWriter fw = new FileWriter(saveTo, true);
 
            PrintWriter pw = new PrintWriter(fw);
 
            pw.println(formatter.format(new Date()) + message);
 
            pw.flush();
 
            pw.close();
 
        } catch (IOException e)
        {
 
            e.printStackTrace();
 
        }
 
    }
	
}

