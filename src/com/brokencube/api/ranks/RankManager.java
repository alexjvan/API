package com.brokencube.api.ranks;

import java.util.ArrayList;
import java.util.List;

import com.brokencube.api.API;

public class RankManager {
	private API instance;
	public List<Rank> ranks = new ArrayList<Rank>();
	
	public RankManager(API instance) {
		this.instance = instance;
		grabRanks();
		grabInheritance();
	}
	
	private void grabRanks() {
		List<String[]> results = instance.getDB().getQuery("SELECT id, name, prefix, booster FROM ranks");
		for(int i = 0; i < results.size(); i++) {
			String[] cur = results.get(i);
			Rank newRank = new Rank(instance, cur[1], cur[2], Integer.parseInt(cur[3]), SimpleRank.getFromNum(Integer.parseInt(cur[0])));
			ranks.add(newRank);
		}
	}
	
	private void grabInheritance() {
		// id, rankid, inheritedid
		List<String[]> results = instance.getDB().getQuery("SELECT rankid, inheritsid FROM rank_inheritance");
		for(int i = 0; i < results.size(); i++) {
			String[] cur = results.get(i);
			Rank obj = getRankFromSRank(SimpleRank.getFromNum(Integer.parseInt(cur[0])));
			Rank inhs = getRankFromSRank(SimpleRank.getFromNum(Integer.parseInt(cur[1])));
			obj.inherits.add(inhs);
			inhs.inheritedBy.add(obj);
		}
	}
	
	public Rank getRankFromSRank(SimpleRank sRank) {
		if(sRank == null)
			throw new NullPointerException();
		for(int i = 0; i < ranks.size(); i++) {
			if(ranks.get(i).name.equalsIgnoreCase(sRank.name())) {
				return ranks.get(i);
			}
		}
		return null;
	}
}
