package com.brokencube.api.user;

import org.bukkit.command.CommandSender;

import com.brokencube.api.chat.ColorReplacer;

public abstract class Executor {
	
	protected CommandSender executor;
	
	public Executor(CommandSender exe) {
		this.executor = exe;
	}
	
	public abstract boolean isUser();
	public abstract boolean isConsole();
	
	public void sendMessage(String message) {
		this.executor.sendMessage(ColorReplacer.colorize(message));
	}

}
