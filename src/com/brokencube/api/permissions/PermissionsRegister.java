package com.brokencube.api.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.brokencube.api.API;
import com.brokencube.api.ranks.Rank;
import com.brokencube.api.ranks.SimpleRank;

public class PermissionsRegister {
	private API instance;
	
	public TreeMap<String, SimpleRank> perms = new TreeMap<String, SimpleRank>();
	
	public List<String> permsReqNotFound = new ArrayList<String>();
	
	public PermissionsRegister(API instance) {
		this.instance = instance;
		grabPerms();
	}
	
	public void grabPerms() {
		List<String[]> results = instance.getDB().getQuery("SELECT permission, rankid FROM permissions");
		for(int i = 0; i < results.size(); i++) {
			String[] row = results.get(i);
			perms.put(row[0], SimpleRank.getFromNum(Integer.parseInt(row[1])));
		}
	}

	public void assignPermission(String perm, SimpleRank sRank) {
		perms.put(perm, sRank);
	}
	
	public boolean doesRankHavePerm(Rank r, String perm) {
		SimpleRank sr = perms.get(perm);
		if(sr == null) {
			this.permsReqNotFound.add(perm);
			return false;
		}
		Rank or = instance.getRM().getRankFromSRank(sr);
		return r.inherits(or.sRank);
	}
	
	public TreeMap<String, SimpleRank> getPerms() {
		return this.perms;
	}

	public SimpleRank getRank(String permString) {
		if(!perms.containsKey(permString)) {
			this.permsReqNotFound.add(permString);
			return SimpleRank.Undefined;
		}
		return perms.get(permString);
	}
	
	public boolean permissionExists(String perm) {
		return this.perms.containsKey(perm);
	}
	
}
