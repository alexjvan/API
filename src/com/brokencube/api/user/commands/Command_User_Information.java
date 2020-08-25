package com.brokencube.api.user.commands;

import com.brokencube.api.command.SubCommand;
import com.brokencube.api.command.exceptions.CommandNotFoundException;
import com.brokencube.api.command.exceptions.IncorrectArgumentsException;
import com.brokencube.api.command.exceptions.NoPermsException;
import com.brokencube.api.user.Executor;

public class Command_User_Information extends SubCommand {

	// Def chat width = 53chars
	
	public Command_User_Information(Command_User parent) {
		super(parent, "user.information");
	}

	@Override
	public void exe(Executor e, String[] split)
			throws CommandNotFoundException, IncorrectArgumentsException, NoPermsException {
		
	}
	
}
