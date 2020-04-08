package com.brokencube.api.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.brokencube.api.API;
import com.brokencube.api.command.exceptions.ExecutingDefaultException;
import com.brokencube.api.command.exceptions.NoConsoleImplementationException;
import com.brokencube.api.user.Console;
import com.brokencube.api.user.Executor;
import com.brokencube.api.user.User;

public abstract class Command {
	protected API instance;
	
	// values
	protected List<String> aliases;
	protected String command;
	protected String description;
	protected String permission;
	protected List<String> useCases;

	public Command defaultCommand;
	public String defaultString;
	
	// tree cases
	protected int argCount = 0;
	protected String argPart = null;
	protected List<Command> children = new ArrayList<Command>();
	protected Command parent = null;
	
	public Command(API instance, String command, String description, String permission) {
		this(instance, command, description, permission, new ArrayList<String>(), new ArrayList<String>(), "");
	}
	
	public Command(API instance, String command, String description, String permission, String useCase) {
		this(instance, command, description, permission, new ArrayList<>(Arrays.asList(useCase)), new ArrayList<String>(), "");
	}
	
	public Command(API instance, String command, String description, String permission, List<String> useCases) {
		this(instance, command, description, permission, useCases, new ArrayList<String>(), "");
	}
	
	public Command(API instance, String command, String description, String permission, String useCase, String alias) {
		this(instance, command, description, permission, new ArrayList<>(Arrays.asList(useCase)), new ArrayList<>(Arrays.asList(alias)), "");
	}
	
	public Command(API instance, String command, String description, String permission, List<String> useCases, String alias) {
		this(instance, command, description, permission, useCases, new ArrayList<>(Arrays.asList(alias)), "");
	}
	
	public Command(API instance, String command, String description, String permission, List<String> useCases, List<String> aliases) {
		this(instance, command, description, permission, useCases, aliases, "");
	}
	
	public Command(API instance, String command, String description, String permission, String useCase, String alias, String defaultStr) {
		this(instance, command, description, permission, new ArrayList<>(Arrays.asList(useCase)), new ArrayList<>(Arrays.asList(alias)), defaultStr);
	}
	
	public Command(API instance, String command, String description, String permission, List<String> useCases, List<String> aliases, String defaultStr) {
		this.instance = instance;
		this.command = command;
		this.description = description;
		this.permission = permission;
		this.useCases = useCases;
		this.aliases = aliases;
		this.defaultString = defaultStr;
		
		instance.getPR().registerPermission(permission);
	}
	
	public void userExe(User u, String[] args) throws ExecutingDefaultException {
		this.exeDefault(u, args);
	}
	
	public void consoleExe(Console c, String[] args) throws NoConsoleImplementationException {
		throw new NoConsoleImplementationException();
	}
	
	public void exeDefault(Executor e, String[] args) throws ExecutingDefaultException {
		if(defaultCommand == null) {
			defaultCommand = instance.getCR().getCommand(defaultString);
		}
		throw new ExecutingDefaultException();
	}
	
	public void addChild(Command toRegister) {
		String[] oSplit = toRegister.command.split(" ");
		// if there are no other nodes in the string
		if(oSplit.length == argCount + 1) {
			// if this is a placeholder node - override
			if(this.isPlaceholder()) {
				toRegister.argCount = argCount;
				toRegister.argPart = argPart;
				toRegister.parent = parent;
				toRegister.children = children;
				
				if(parent != null) {
					parent.children.remove(this);
					parent.children.add(toRegister);
				} else {
					instance.getCR().getCommands().remove(this);
					instance.getCR().getCommands().add(toRegister);
				}
			}
			// mistake - multiple of same command
			return;
		}
		
		// look for child with same literal
		for(int i = 0; i < children.size(); i++) {
			if(children.get(i).argPart == oSplit[argCount + 1]) {
				children.get(i).addChild(toRegister);
				return;
			}
		}
		// not found as a child - [has only one more arg][has more than one more arg]
		if(oSplit.length == argCount + 2) {
			children.add(toRegister);
			toRegister.argCount = argCount + 1;
			toRegister.argPart = oSplit[argCount + 1];
			toRegister.parent = this;
		} else {
			PlaceholderCommand phc = new PlaceholderCommand(instance);
			children.add(phc);
			phc.argCount = argCount + 1;
			phc.argPart = oSplit[argCount + 1];
			phc.parent = this;
			phc.addChild(toRegister);
		}
	}
	
	public int childrenCount() {
		int count = 0;
		if(children.size() != 0) {
			for(int i = 0; i < children.size(); i++) {
				count = 1 + children.get(i).childrenCount();
			}
		}
		return count;
	}
	
	public void addAlias(String alias) {
		aliases.add(alias);
	}
	
	public void addUseCase(String useCase) {
		useCases.add(useCase);
	}
	
	public void setArgString(String s) {
		this.argPart = s;
	}
	
	public boolean hasAlias(String check) {
		for(int i = 0; i < aliases.size(); i++) {
			if(aliases.get(i).equalsIgnoreCase(check))
				return true;
		}
		return false;
	}
	
	public String getArgString() {
		return this.argPart;
	}
	
	public String getCommandString() {
		return command;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public List<Command> getChildren() {
		return this.children;
	}
	
	public void setArgCount(int n) {
		this.argCount = n;
	}
	
	public boolean isPlaceholder() {
		return false;
	}
}
