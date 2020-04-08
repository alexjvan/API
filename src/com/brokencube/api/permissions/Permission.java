package com.brokencube.api.permissions;

import java.util.ArrayList;
import java.util.List;

import com.brokencube.api.ranks.SimpleRank;

public class Permission {
	public String fullPerm;
	public String permSection;
	public SimpleRank rank;
	
	public Permission parent;
	public List<Permission> children = new ArrayList<Permission>();
	
	public Permission(String fullPerm, SimpleRank r) {
		this.fullPerm = fullPerm;
		this.rank = r;
	}
	
}
