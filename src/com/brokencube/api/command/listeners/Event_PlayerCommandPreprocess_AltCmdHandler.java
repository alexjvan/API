package com.brokencube.api.command.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.brokencube.api.API;
import com.brokencube.api.Messages;
import com.brokencube.api.command.Command;
import com.brokencube.api.command.exceptions.ExecutingDefaultException;
import com.brokencube.api.user.User;

public class Event_PlayerCommandPreprocess_AltCmdHandler implements Listener {
	private API instance;
	
	public Event_PlayerCommandPreprocess_AltCmdHandler(API instance) {
		this.instance = instance;
	}
	
	@EventHandler
	public void onRunCommand(PlayerCommandPreprocessEvent e) {
		User u = (User)instance.getUR().getExecutorFromUsername(e.getPlayer().getName());
		String cmd = e.getMessage();
		cmd = cmd.substring(1);
		Command c = instance.getCR().getCommand(cmd);
		// command null at line below - command not being found
		
		boolean cancel = runCommand(u, c, cmd);
		if(cancel)
			e.setCancelled(true);
	}
	
	public boolean runCommand(User u, Command c, String cmd) {
		if(c == null) {
			u.sendMessage(Messages.nocmd);
			return true;
		}
		String[] split = cmd.split(" ");
		if(split.length == 0)
			split = new String[] {cmd};

		if(!u.hasPermission(c.getPermission())) {
			try {
				c.userExe(null, null);
				u.sendMessage(Messages.noperms);
				return true;
			} catch (ExecutingDefaultException e) {}
		}
		// check if executing a default command
		try {
			c.userExe(u, split);
		} catch (ExecutingDefaultException e) {
			if(c.defaultCommand == null)
				c.defaultCommand = instance.getCR().getCommand(c.defaultString);
			
			Command n = c.defaultCommand;
			String ns = n.getCommandString();
			
			runCommand(u, n, ns);
		}
		return true;
	}
	
}
