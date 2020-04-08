package com.brokencube.api.server.commands;

import com.brokencube.api.API;
import com.brokencube.api.Messages;
import com.brokencube.api.command.Command;
import com.brokencube.api.user.Console;
import com.brokencube.api.user.Executor;
import com.brokencube.api.user.User;

public class Command_DB_Status extends Command {
	
	public Command_DB_Status(API instance) {
		super(instance, "database status", "List the connection status of the database", "db.status", "/db status");
	}

	@Override
	public void userExe(User u, String[] args) {
		exe(u, args);
	}
	
	@Override
	public void consoleExe(Console c, String[] args) {
		exe(c, args);
	}
	
	private void exe(Executor e, String[] split) {
		if(split.length > 2) {
			e.sendMessage(Messages.wrongArgs);
			return;
		}
		if(instance.getDB().connectionOk()) {
			e.sendMessage("&6Connection to the server is &a&lGOOD&r&6.");
		} else {
			e.sendMessage("&6Connection to the server is &c&lBAD&r&6.");
		}
	}

}
