package com.brokencube.api.ranks;

import java.util.ArrayList;
import java.util.List;

import com.brokencube.api.API;

public class Rank {
	private API instance;
	
	public String name;
	public String prefix;
	public int booster;
	public List<Rank> inherits = new ArrayList<Rank>();
	public List<Rank> inheritedBy = new ArrayList<Rank>();
	
	public SimpleRank sRank;
	
	public Rank(API instance, String name, String prefix, int booster, SimpleRank sRank) {
		this.instance = instance;
		this.name = name;
		this.prefix = prefix;
		this.booster = booster;
		this.sRank = sRank;
	}
	
	public boolean inherits(SimpleRank other) {
		if(sRank.name().equalsIgnoreCase(other.name()))
			return true;
		for(int i = 0; i < inherits.size(); i++) {
			if(inherits.get(i).inherits(other))
				return true;
		}
		return false;
	}
	
	public boolean isInherited(SimpleRank other) {
		for(int i = 0; i < inheritedBy.size(); i++) {
			if(inheritedBy.get(i).sRank.name().equalsIgnoreCase(other.name()))
				return true;
			boolean internal = inheritedBy.get(i).isInherited(other);
			if(internal)
				return true;
		}
		return false;
	}
	
	public boolean hasPermission(String permission) {
		return this.instance.getPR().doesRankHavePerm(this, permission);
	}
	
}
