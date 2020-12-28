package com.brokencube.api.command.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.brokencube.api.Messages;
import com.brokencube.api.Utils;
import com.brokencube.api.chat.ColorReplacer;
import com.brokencube.api.command.Command;
import com.brokencube.api.command.CommandBase;
import com.brokencube.api.command.SubCommand;
import com.brokencube.api.command.exceptions.CommandNotFoundException;
import com.brokencube.api.command.exceptions.IncorrectArgumentsException;
import com.brokencube.api.command.exceptions.NoPermsException;
import com.brokencube.api.user.Executor;
import com.brokencube.api.user.User;

public class Command_Commands_List extends SubCommand {
	
	HashMap<String, ArrayList<CommandBase>> usersLists = new HashMap<String, ArrayList<CommandBase>>();
	
	private final int numCmdsToSend = 10;
			
	public Command_Commands_List(Command_Commands parent) {
		super(parent, "commands.list");
		this.useCase = "/commands list [page#]";
		this.description = "List the commands available to you.";
	}

	@Override
	public void exe(Executor e, String[] split) throws CommandNotFoundException, IncorrectArgumentsException, NoPermsException {
		if(!e.hasPermission(permString))
			throw new NoPermsException();
		
		if(split.length == 1) {
			sendHelp(e, 1);
		} else if(split.length == 2) {
			if(Utils.isInt(split[1])) {
				sendHelp(e, Integer.parseInt(split[1]));
			} else {
				sendHelp(e, 1);
			}
		} else if(split.length == 3) {
			sendHelp(e, Integer.parseInt(split[2]));
		}
	}
	
	private void sendHelp(Executor e, int page) {
		String name = "";
		if(e.isUser())
			name = ((User)e).username;
		else
			name = "<Console>";
		if(!usersLists.containsKey(name)) {
			createList(name, e);
		}
		
		ArrayList<CommandBase> user = usersLists.get(name);
		
		int pages = user.size() / numCmdsToSend;
		if(user.size() % numCmdsToSend != 0)
			pages++;
		
		e.sendMessage(Messages.general + "--------------===[Help Page " + Messages.success + page + Messages.general + " / " + Messages.success + pages + Messages.general + "]===--------------");
		e.sendMessage(ColorReplacer.colorize("&8Key: &7<> &8are required arguments, &7[] &8are optional arguments."));
		if(page > pages) {
			return;
		}
		
		int toSend = numCmdsToSend;
		if(page == 1) {
			toSend--;
			e.sendMessage(ColorReplacer.colorize("&8For more commands type in /commands list [page #]"));
		}
		
		for(int i = 0; i < toSend; i++) {
			int sendIndex = ((page - 1) * (numCmdsToSend - 1)) + i;
			if(sendIndex < user.size()) {
				e.sendMessage(ColorReplacer.colorize("&b" + user.get(sendIndex).useCase + " &6| &f"+user.get(sendIndex).description));
			}
		}
	}
	
	private void createList(String name, Executor e) {
		ArrayList<CommandBase> cmds = new ArrayList<CommandBase>();
		
		List<Command> parents = instance.getCR().commands;
		
		for(int i = 0; i < parents.size(); i++) {
			addParent(e, parents.get(i), cmds);
		}
		
		usersLists.put(name, cmds);
	}
	
	private void addParent(Executor e, Command c, ArrayList<CommandBase> cmds) {
		if(c.children.size() == 0) {
			if(e.isConsole() || e.rank.inherits(c.getLowestPermNeeded()))
				cmds.add(c);
		} else {
			ArrayList<CommandBase> cmdTree = new ArrayList<CommandBase>();
			cmdTree.add(c);
			boolean adding = false;
			for(int i = 0; i < c.children.size(); i++) {
				if(addChild(e, c.children.get(i), cmdTree))
					adding = true;
			}
			
			if(adding)
				cmds.addAll(cmdTree);
		}
	}
	
	private boolean addChild(Executor e, SubCommand sc, ArrayList<CommandBase> cmdTree) {
		if(!e.hasPermission(sc.permString))
			return false;
		
		cmdTree.add(sc);
		return true;
	}
	
	
}
