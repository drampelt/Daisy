package ca.drmc.daisy;

public class ConfigPlayer extends ConfigObject{
	public ConfigPlayer(String name){
		this.displayname = name;
	}
	public String displayname = null;
	public Boolean goodmood = true;
}
