package com.brokencube.api.command.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.brokencube.api.API;
import com.brokencube.api.Messages;
import com.brokencube.api.command.Command;
import com.brokencube.api.command.exceptions.CommandNotFoundException;
import com.brokencube.api.command.exceptions.IncorrectArgumentsException;
import com.brokencube.api.command.exceptions.NoPermsException;
import com.brokencube.api.user.User;

public class Event_PlayerCommandPreProcess_AltCmdHandler implements Listener {
	private API instance;
	
	public Event_PlayerCommandPreProcess_AltCmdHandler(API instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onRunCommand(PlayerCommandPreprocessEvent e) {
		User u = (User)instance.getUR().getExecutorFromUsername(e.getPlayer().getName());
		
		String cmd = e.getMessage();
		cmd = cmd.substring(1);
		String[] split = cmd.split(" ");
		if(split.length == 0) 
			split = new String[] {cmd};
		
		Command c = instance.getCR().getCommand(cmd);
		// command null at line below - command not being found
		if(c == null)
			u.sendMessage(Messages.nocmd);
		else if(!u.rank.inherits(c.getLowestPermNeeded()))
			u.sendMessage(Messages.noperms);
		else {
			try {
				c.userExe(u, split);
			} catch (CommandNotFoundException e1) {
				u.sendMessage(Messages.nocmd);
			} catch(IncorrectArgumentsException e1) {
				u.sendMessage(Messages.wrongArgs);
			} catch (NoPermsException e1) {
				u.sendMessage(Messages.noperms);
			}
		}
		
		e.setCancelled(true);
	}
	
}
