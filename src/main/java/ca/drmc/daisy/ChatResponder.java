package ca.drmc.daisy;

//import java.util.ArrayList;
import java.util.Random;

import org.bukkit.entity.Player;

public class ChatResponder {
	
	private Daisy plugin;
	Random r;
	
	public ChatResponder(Daisy d){
		plugin = d;
		r = new Random();
	}
	
	public void respond(Player p, String m, String[] split){
		ConfigPlayer cp = plugin.getCfgPlayer(p.getName());
		String ml = m.toLowerCase();
		plugin.log("DEBUG: responding to " + m);
		if(split[0].toLowerCase().contains("hi") || split[0].toLowerCase().contains("hey") || split[0].toLowerCase().contains("yo") || split[0].toLowerCase().contains("hello") || split[0].toLowerCase().contains("sup")){
			String[] msgs = {"Hello, ", "Hi ", "Hey ", "Sup, "};
			String[] endings = {".", "!"};
			sendDelayedMessage(chooseMessage(msgs) + cp.displayname + chooseMessage(endings));
		}else if(ml.contains("how are you")){
			String[] msgs = {"I'm doing fine, ", "I'm fine, ", "I'm doing great, ", "I'm great, "};
			String[] endings = {".", ". Thanks for asking.", "!"};
			sendDelayedMessage(chooseMessage(msgs) + cp.displayname + chooseMessage(endings));
		}else if(ml.contains("what should i build")){
			String[] msgs = {"Something awesome!", "Something amazing!", "A house!", "A mansion!", "A farm!"};
			sendDelayedMessage(chooseMessage(msgs));
		}else if(ml.contains("thanks") || ml.contains("ty") || ml.contains("tyvm") || ml.contains("thank you") || ml.contains("thankyou")){
			String[] msgs = {"You're welcome, ", "No problem, "};
			String[] endings = {".", ". Glad I could help.", "!", "! Glad I could help."};
			sendDelayedMessage(chooseMessage(msgs) + cp.displayname + chooseMessage(endings));
		}else if(ml.contains("shut up") || ml.contains("be quiet")){
			String[] msgs = {"That's not very nice " + cp.displayname + " -_-", "Don't be rude, " + cp.displayname + "."};
			cp.goodmood = false;
			plugin.saveCfg();
			sendDelayedMessage(chooseMessage(msgs));
		}else if(ml.contains("sorry") && !ml.contains("not")){
			cp.goodmood = true;
			plugin.saveCfg();
			sendDelayedMessage("That's ok, " + cp.displayname + ". I forgive you.");
		}else if(ml.contains("what do you think of me") || ml.contains("do you like me")){
			if(cp.goodmood){
				String[] msgs = {"I think you are a very nice person, ", "I think you are awesome, ", "You're amazing, "};
				sendDelayedMessage(chooseMessage(msgs) + cp.displayname + ".");
			}else{
				String[] msgs = {"I think you are a very mean person, ", "I don't like you much, ", "You're rude, "};
				sendDelayedMessage(chooseMessage(msgs) + cp.displayname + ".");
			}
		}else if(ml.contains("why")){
			if(ml.contains("do you hate me") || ml.contains("do you think im rude") || ml.contains("do you not like me")){
				sendDelayedMessage("You were mean to me before, " + cp.displayname + ".");
			}else{
				sendDelayedMessage("Why what, " + cp.displayname + "?");
			}
		}else{
			sendDelayedMessage("You mentioned my name " + cp.displayname + ", but I don't know how to answer you. " + plugin.getCfgDev() + " should fix that.");
			plugin.logToFile(p.getName() + " said \"" + m + "\". " + plugin.getCfgName() + " could not respond.");
		}
	}
	
	public void playerJoin(Player p){
		ConfigPlayer cp = plugin.getCfgPlayer(p.getName());
		if(cp != null){
			// Player has logged on before
			if(cp.goodmood){
				String[] msgs = {"Welcome back, ", "Nice to see you again, ", "Hello again, "};
				String[] endings = {".", "!"};
				sendDelayedMessage(chooseMessage(msgs) + p.getDisplayName() + chooseMessage(endings) + "Good Mood: " + cp.goodmood);
			}else{
				String[] msgs = {"Oh, it's you again ", "Not you again ", "Oh great, it's "};
				sendDelayedMessage(chooseMessage(msgs) + p.getDisplayName() + "...");
			}
		}else{
			// Player is new
			plugin.addCfgPlayer(p.getName(), new ConfigPlayer(p.getName(), true));
			plugin.saveCfg();
			sendDelayedMessage("Welcome, " + p.getName() + ", It seems this is our first time meeting each other. My name is " + plugin.getCfgName() + " and I am a server bot.");
		}
	}
	
	private void sendDelayedMessage(final String m){
		long min = (long)(plugin.getCfgMindelay()*20);
		long max = (long)(plugin.getCfgMaxdelay()*20);
		long delay = min+((long)(r.nextDouble()*(max-min)));
		
		plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){

			public void run() {
				plugin.getServer().broadcastMessage(plugin.getPrefix() + m);
			}
			
		}, delay);
	}
	
	private String chooseMessage(String[] msgs){
		return msgs[r.nextInt(msgs.length)];
	}
}
