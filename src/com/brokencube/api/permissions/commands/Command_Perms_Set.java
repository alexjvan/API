package com.brokencube.api.permissions.commands;

import com.brokencube.api.Messages;
import com.brokencube.api.Utils;
import com.brokencube.api.command.Command;
import com.brokencube.api.command.SubCommand;
import com.brokencube.api.command.exceptions.CommandNotFoundException;
import com.brokencube.api.command.exceptions.IncorrectArgumentsException;
import com.brokencube.api.command.exceptions.NoPermsException;
import com.brokencube.api.ranks.SimpleRank;
import com.brokencube.api.user.Executor;

public class Command_Perms_Set extends SubCommand {
	
	public Command_Perms_Set(Command parent) {
		super(parent, "perms.set");
	}

	@Override
	public void exe(Executor e, String[] split) throws CommandNotFoundException, IncorrectArgumentsException, NoPermsException {
		if(!e.hasPermission(permString))
			throw new NoPermsException();
		
		String rank = split[3];
		SimpleRank sr;
		
		if(Utils.isInt(rank)) {
			int i = Integer.parseInt(rank); 
			sr = SimpleRank.getFromNum(i);
		} else {
			sr = SimpleRank.getFromTitle(rank);
		}
		if(sr == null) {
			e.sendMessage(Messages.error+"Rank given (&b"+rank+Messages.error+") is not a valid rank.");
			return;
		}
		
		// need to insert into db
		if(instance.getPR().permissionExists(split[2])) {
			instance.getDB().insertQuery("INSERT INTO permissions VALUES ('"+split[2]+"', "+sr.rankNum()+")");
		}
		// need to update db
		else {
			instance.getDB().updateQuery("UPDATE permissions SET rankid="+sr.rankNum()+" WHERE permission=\""+split[2]+"\"");
		}
		instance.getPR().assignPermission(split[2], sr);
	}
	
}
