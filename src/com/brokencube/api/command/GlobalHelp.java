package com.brokencube.api.command;

import java.util.ArrayList;
import java.util.Arrays;

import com.brokencube.api.API;
import com.brokencube.api.user.User;

public class GlobalHelp extends Command {

	public GlobalHelp(API instance) {
		super(instance, "help", "A global help command.", "help", new ArrayList<String>(Arrays.asList("/help", "/help #", "/<command> help", "/<command> help #")));
	}
	
	@Override
	public void userExe(User u, String[] split) {
		
	}
	
}
