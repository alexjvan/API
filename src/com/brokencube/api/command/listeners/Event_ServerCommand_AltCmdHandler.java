package com.brokencube.api.command.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;

import com.brokencube.api.API;
import com.brokencube.api.Messages;
import com.brokencube.api.command.Command;
import com.brokencube.api.command.exceptions.NoConsoleImplementationException;
import com.brokencube.api.user.Console;

public class Event_ServerCommand_AltCmdHandler implements Listener {
	private API instance;
	
	public Event_ServerCommand_AltCmdHandler(API instance) {
		this.instance = instance;
	}

	@EventHandler
	public void onServerCommand(ServerCommandEvent e) {
		Console c = instance.getUR().getConsole();
		String cmd = e.getCommand();
		Command cm = instance.getCR().getCommand(cmd);
		try {
			if(cm != null)
				cm.consoleExe(c, cmd.split(" "));
			else {
				instance.getUR().getConsole().sendMessage(Messages.nocmd);
				e.setCancelled(true);
			}
		} catch(NoConsoleImplementationException ee) {
			instance.getUR().getConsole().sendMessage(Messages.noconsole);
		}
	}
	
}
