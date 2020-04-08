package com.brokencube.api.server.commands;

import com.brokencube.api.API;
import com.brokencube.api.Messages;
import com.brokencube.api.command.Command;
import com.brokencube.api.user.Console;
import com.brokencube.api.user.Executor;
import com.brokencube.api.user.User;

public class Command_DB_Reset extends Command {
	public Command_DB_Reset(API instance) {
			// api		command				description								perm		use
		super(instance, "database reset", "Reset the connection in the database", "db.reset", "/db reset");
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
		instance.getDB().resetConnection();
		if(instance.getDB().connectionOk()) {
			e.sendMessage("&a&lConncetion was successfully reset!");
		} else {
			e.sendMessage("&c&lThere was an issue re-establishing the connection and the database can't be found!");
		}
	}
	
}
