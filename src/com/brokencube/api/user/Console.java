package com.brokencube.api.user;

import com.brokencube.api.API;
import com.brokencube.api.ranks.SimpleRank;

public class Console extends Executor {
	
	protected SimpleRank rank = SimpleRank.Console;

	public Console(API instance) {
		super(instance.getServer().getConsoleSender());
	}
	
	@Override
	public boolean isUser() {
		return false;
	}
	
	@Override
	public boolean isConsole() {
		return true;
	}

}
