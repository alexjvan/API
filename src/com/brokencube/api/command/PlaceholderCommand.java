package com.brokencube.api.command;

import com.brokencube.api.API;
import com.brokencube.api.user.Console;
import com.brokencube.api.user.User;

public class PlaceholderCommand extends Command {
	
	public PlaceholderCommand(API api) {
		super(api, "", "", "");
	}
	
	@Override
	public boolean isPlaceholder() {
		return true;
	}

	@Override
	public void userExe(User u, String[] args) {
		// DO NOTHING
	}
	
	@Override
	public void consoleExe(Console c, String[] args) {
		// DO NOTHING
	}
	
}
