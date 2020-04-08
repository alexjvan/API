package com.brokencube.api.ranks;

public enum SimpleRank {
	Console(90, "Console"),
	Undefined(00, "Undefined"),
	
	Owner(80, "Owner"),
	Developer(70, "Developer"),
	HeadAdmin(62, "HeadAdmin"),
	Admin(61, "Admin"),
	PreAdmin(60, "PreAdmin"),
	HeadMod(52, "HeadMod"),
	Mod(51, "Mod"),
	PreMod(50, "PreMod"),
	Builder(40, "Builder"),
	
	YouTuber(30, "YouTube"),
	
	MVP(23, "MVP"),
	VIP(22, "VIP"),
	Premium(21, "Premium"),
	Pro(20, "Pro"),
	
	User(10, "User");
	
	private static final SimpleRank[] v = values();
	private final int rankNum;
	private final String rankTitle;
	
	SimpleRank(int i, String t) {
		this.rankNum = i;
		this.rankTitle = t;
	}
	
	public int rankNum() {
		return this.rankNum;
	}
	
	public String rankTitle() {
		return this.rankTitle;
	}
	
	public static SimpleRank getFromNum(int value) {
		for(SimpleRank r : v) {
			if(r.rankNum == value)
				return r;
		}
		return null;
	}
	
	public static SimpleRank getFromTitle(String value) {
		for(SimpleRank r : v) {
			if(r.rankTitle.equalsIgnoreCase(value))
				return r;
		}
		return null;
	}
	
}
