package com.brokencube.api.command;

import java.util.ArrayList;
import java.util.List;

import com.brokencube.api.API;

public class CommandRegister {
	private API instance;
	private List<Command> commands = new ArrayList<Command>();
	
	public CommandRegister(API instance) {
		this.instance = instance;
	}
 	
	public void registerCommand(Command com) {
		String[] split = com.getCommandString().split(" ");
		for(int i = 0; i < commands.size(); i++) {
			if(commands.get(i).getCommandString().equalsIgnoreCase(split[0])) {
				commands.get(i).addChild(com);
				return;
			}
		}
		// command wasn't found in the registered commands
		if(split.length == 1) {
			commands.add(com);
			com.setArgString(split[0]);
			com.setArgCount(0);
		} else {
			PlaceholderCommand phc = new PlaceholderCommand(instance);
			commands.add(phc);
			phc.setArgCount(0);
			phc.setArgString(split[0]);
			phc.addChild(com);
		}
	}
	
	public List<Command> getCommands() {
		return this.commands;
	}
	
	public Command getCommand(String command) {
		String[] split = command.split(" ");
		if(split.length == 0) {
			split = new String[] {command};
		}
		for(int i = 0; i < commands.size(); i++) {
			if(commands.get(i).getArgString().equalsIgnoreCase(split[0]) || commands.get(i).hasAlias(split[0])) {
				return getCommand(split, commands.get(i), 1);
			}
		}
		return null;
	}
	
	private Command getCommand(String[] split, Command cur, int pos) {
		if(pos == split.length) {
			return cur;
		}
		for(int i = 0; i < cur.children.size(); i++) {
			if(cur.children.get(i).getArgString().equalsIgnoreCase(split[pos]) || cur.children.get(i).hasAlias(split[pos])) {
				return getCommand(split, cur.children.get(i), pos + 1);
			}
		}
		return cur;
	}
}
