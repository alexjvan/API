package com.brokencube.api.server.commands;

import com.brokencube.api.API;
import com.brokencube.api.command.Command;

public class Command_DB extends Command {

	public Command_DB(API instance) {
			// api		command		description					perm	use	  alias	default
		super(instance, "database", "Commands for the database", "db", "/db", "db", "db status");
	}
	
}
