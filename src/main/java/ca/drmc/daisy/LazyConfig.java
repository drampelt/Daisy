package ca.drmc.daisy;

import org.bukkit.plugin.Plugin;
import java.io.File;
import java.util.HashMap;

public class LazyConfig extends Config {
	public LazyConfig(Plugin p){
		CONFIG_FILE = new File(p.getDataFolder(), "config.yml");
		CONFIG_HEADER = "For the chat format, use & for colours and +name for the name of the bot.";
	}
	//Nice and easy lazy config, just define the variables and it does everything for you
	public String name = "Daisy";
	public String format = "&4+name&f: ";
	public String devname = "The developer";
	public Boolean logproblems = true;
	public int mindelay = 3;
	public int maxdelay = 4;
	public HashMap<String, ConfigPlayer> players = new HashMap<String, ConfigPlayer>();
}
