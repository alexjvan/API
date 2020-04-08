package com.brokencube.api.chat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.brokencube.api.API;
import com.brokencube.api.user.User;

public class Event_AsyncPlayerChatEvent_PlayerChatFormatter implements Listener {
	private API instance;
	
	public Event_AsyncPlayerChatEvent_PlayerChatFormatter(API instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		User u = (User)instance.getUR().getExecutorFromUsername(e.getPlayer().getName());
		String msg = e.getMessage();
		
		String format = (String)instance.getConf().get("chat.format");

		String formatted = format;
		
		while(formatted.contains("{username}")) 
			formatted = formatted.replace("{username}", u.getUserName());

		while(formatted.contains("{rankprefix}")) 
			formatted = formatted.replace("{rankprefix}", u.rank.prefix);

		while(formatted.contains("{message}")) 
			formatted = formatted.replace("{message}", msg);
		
		instance.getUR().sendMessageAll(formatted);
		
		e.setCancelled(true);
	}
	
}
