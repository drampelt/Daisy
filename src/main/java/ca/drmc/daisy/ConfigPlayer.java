package ca.drmc.daisy;

public class ConfigPlayer extends ConfigObject{
	public ConfigPlayer(String displayname, Boolean goodmood){
		this.displayname = displayname;
		this.goodmood = goodmood;
	}
	public ConfigPlayer(){
		// Used for MrFigg's SuperEasyConfig
	}
	public String displayname = null;
	public Boolean goodmood = true;
}
