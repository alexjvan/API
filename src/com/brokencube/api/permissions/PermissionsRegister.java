package com.brokencube.api.permissions;

import java.util.ArrayList;
import java.util.List;

import com.brokencube.api.API;
import com.brokencube.api.ranks.Rank;
import com.brokencube.api.ranks.SimpleRank;

public class PermissionsRegister {
	protected API instance;

	List<Permission> permissions = new ArrayList<Permission>();
	
	public PermissionsRegister(API instance) {
		this.instance = instance;
	}
	
	public void grabPerms() {
		List<String[]> results = instance.getDB().getQuery("SELECT permission, rankid FROM permissions");
		for(int i = 0; i < results.size(); i++) {
			String[] row = results.get(i);
			assignPermission(row[0], SimpleRank.getFromNum(Integer.parseInt(row[1])));
		}
	}
	
	public List<Permission> grabAll() {
		return permissions;
	}
	
	public Permission getPerm(String permission) {
		String[] split = permission.split("\\.");
		if(split.length == 0)
			split = new String[] {permission};
		for(int i = 0; i < permissions.size(); i++) {
			if(permissions.get(i).permSection.equalsIgnoreCase(split[0]))
				return getPerm(split, 1, permissions.get(i));
		}
		return null;
	}
	
	private Permission getPerm(String[] split, int index, Permission cur) {
		if(index == split.length)
			return cur;
		for(int i = 0; i < cur.children.size(); i++) {
			if(cur.children.get(i).permSection.equalsIgnoreCase(split[index]))
				return getPerm(split, index + 1, cur.children.get(i));
		}
		return null;
	}
	
	public void registerPermission(String permission) {
		String[] split = permission.split("\\.");
		if(split.length == 0)
			split = new String[] {permission};
		
		for(int i = 0; i < permissions.size(); i++) {
			if(permissions.get(i).permSection.equalsIgnoreCase(split[0])) {
				registerPermission(split, 1, permissions.get(i));
				return;
			}
		}
		// not found
		Permission n = new Permission(split[0], SimpleRank.Undefined);
		n.permSection = split[0];
		permissions.add(n);
		registerPermission(split, 1, n);
	}
	
	private void registerPermission(String[] perm, int index, Permission cur) {
		// done
		if(index == perm.length) return;
		
		for(int i = 0; i < cur.children.size(); i++) {
			if(cur.children.get(i).permSection.equalsIgnoreCase(perm[index])) {
				registerPermission(perm, index + 1, cur.children.get(i));
				return;
			}
		}
		// not found in perm
		String build = "";
		for(int i = 0; i <= index; i++)
			if(i != index)
				build += perm[i] + ".";
			else
				build += perm[i];
		
		Permission n = new Permission(build, SimpleRank.Undefined);
		n.permSection = perm[index];
		cur.children.add(n);
		registerPermission(perm, index + 1, n);
	}
	
	public void assignPermission(String permission, SimpleRank r) {
		String[] split = permission.split("\\.");
		if(split.length == 0)
			split = new String[] {permission};
		for(int i = 0; i < permissions.size(); i++) {
			if(permissions.get(i).permSection.equalsIgnoreCase(split[0])) {
				assignPermission(split, 1, permissions.get(i), r);
				return;
			}
		}
		//not found in perm, create
		Permission n = new Permission(split[0], SimpleRank.Undefined);
		n.permSection = split[0];
		permissions.add(n);
		assignPermission(split, 1, n, r);
		
	}
	
	private void assignPermission(String[] perm, int index, Permission cur, SimpleRank r) {
		if(index == perm.length) {
			cur.rank = r;
			return;
		}
		
		for(int i = 0; i < cur.children.size(); i++) {
			if(cur.children.get(i).permSection.equalsIgnoreCase(perm[index])) {
				assignPermission(perm, index + 1, cur.children.get(i), r);
				return;
			}
		}
		// not found in perm
		String build = "";
		for(int i = 0; i <= index; i++)
			if(i != index)
				build += perm[i] + ".";
			else
				build += perm[i];

		Permission n = new Permission(build,SimpleRank.Undefined);
		n.permSection = perm[index];
		cur.children.add(n);
		assignPermission(perm, index + 1, n, r);
	}
	
	public boolean doesRankHavePerm(Rank r, String perm) {
		String[] pSplit = perm.split("\\.");
		if(pSplit.length == 0)
			pSplit = new String[] {perm};
		for(int i = 0; i < permissions.size(); i++) {
			if(permissions.get(i).permSection.equalsIgnoreCase(pSplit[0])) {
				return doesRankHavePerm(r, pSplit, permissions.get(i), 1);
			}
		}
		return false;
	}
	
	private boolean doesRankHavePerm(Rank r, String[] perm, Permission cur, int curPos) {
		if(perm.length == curPos) {
			if(r.sRank.name().equalsIgnoreCase(cur.rank.name()) || r.inherits(cur.rank))
				return true;
			else
				return false;
		}
		for(int i = 0; i < cur.children.size(); i++) {
			if(cur.children.get(i).permSection.equalsIgnoreCase(perm[curPos])) {
				return doesRankHavePerm(r, perm, cur.children.get(i), curPos + 1);
			}
		}
		return false;
	}
	
}
