package com.brokencube.api.user;

import com.brokencube.api.API;
import com.brokencube.api.ranks.SimpleRank;

public class Console extends Executor {
	
	protected SimpleRank sRank = SimpleRank.Console;

	public Console(API instance) {
		super(instance.getServer().getConsoleSender());
		rank = instance.getRM().getRankFromSRank(sRank);
	}
	
	@Override
	public boolean isUser() {
		return false;
	}
	
	@Override
	public boolean isConsole() {
		return true;
	}

	@Override
	public boolean hasPermission(String perm) {
		return true;
	}

}
