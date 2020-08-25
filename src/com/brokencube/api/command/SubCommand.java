package com.brokencube.api.command;

import com.brokencube.api.API;
import com.brokencube.api.ranks.SimpleRank;

public abstract class SubCommand extends CommandBase {
	protected API instance;
	
	private Command parent;
	
	public String permString;
	private SimpleRank perm;
	
	public SubCommand(Command parent, String permission) {
		this.parent = parent;
		this.parent.addChild(this);
		
		this.instance = parent.instance;
		
		this.permString = permission;
		regrabPerm();
	}
	
	public void regrabPerm() {
		SimpleRank prank = instance.getPR().getRank(this.permString);
		
		this.perm = prank;
		
		if(!prank.equals(SimpleRank.Undefined))
			if(parent.getLowestPermNeeded().equals(SimpleRank.Undefined) || 
					parent.getLowestPermNeeded().rankNum() > prank.rankNum())
				parent.lowestPermNeeded = prank;
	}
	
}
