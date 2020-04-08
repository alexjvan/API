package com.brokencube.api;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.plugin.java.JavaPlugin;

import com.brokencube.api.blockprotection.Event_BlockBreakEvent_BlockProtection;
import com.brokencube.api.blockprotection.Event_BlockPlaceEvent_BlockProtection;
import com.brokencube.api.blockprotection.Event_ItemEvents_BlockProtection;
import com.brokencube.api.chat.listeners.Event_AsyncPlayerChatEvent_PlayerChatFormatter;
import com.brokencube.api.command.CommandRegister;
import com.brokencube.api.command.listeners.Event_PlayerCommandPreprocess_AltCmdHandler;
import com.brokencube.api.command.listeners.Event_ServerCommand_AltCmdHandler;
import com.brokencube.api.local.ConfigFile;
import com.brokencube.api.permissions.PermissionsRegister;
import com.brokencube.api.plugins.PluginRegister;
import com.brokencube.api.ranks.RankManager;
import com.brokencube.api.server.Database;
import com.brokencube.api.server.commands.Command_DB;
import com.brokencube.api.server.commands.Command_DB_Status;
import com.brokencube.api.user.UserRegister;
import com.brokencube.api.user.listeners.Event_PlayerJoin_DB;

public class API extends JavaPlugin {
	private Database db;
	private UserRegister ur;
	private PermissionsRegister pr;
	private CommandRegister cr;
	private RankManager rm;
	private PluginRegister plug;
	
	ConfigFile conf;
	
	public static API instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		plug = new PluginRegister();
		plug.registerPlugin(this, "API");
		
		// Create conf here to get DB access
		conf = new ConfigFile(this);
		
		conf.addConfigValue("db.host");
		conf.addConfigValue("db.user");
		conf.addConfigValue("db.password");
		
		// DATABASE NEEDS TO BE FIRST
		db = new Database(this);
		// PERMISSION HANDLER NEEDS TO GO BEFORE LOADING COMMANDS
		// COMMANDS NEED TO GO BEFORE PERMISSIONS LOADING
		// CONFIG NEEDS TO BE AFTER PERMISSIONS
		cr = new CommandRegister(this);
		pr = new PermissionsRegister(this);
		
		cr.registerCommand(new Command_DB(this));
		cr.registerCommand(new Command_DB_Status(this));
		
		pr.grabPerms();
		// RANKS NEED TO BE BEFORE USERS
		rm = new RankManager(this);
		ur = new UserRegister(this);
		
		// CONFIG
		conf.addDefault("bp.allowBreak", false);
		conf.addDefault("bp.allowItem", false);
		conf.addDefault("bp.allowPlace", false);
		
		conf.addDefault("chat.channels", new ArrayList<String>(Arrays.asList("global","staff")));
		conf.addDefault("chat.enabled", true);
		conf.addDefault("chat.format", "{rankprefix} {username} &r&b> &7{message}");
		
		conf.addDefault("defaults.gamemode", 2);
		
		conf.addDefault("permissionOverride", new ArrayList<Object>());
		
		conf.addDefault("db.host", "");
		conf.addDefault("db.user", "");
		conf.addDefault("db.password", "");
		
		conf.addConfigValue("bp.allowBreak");
		conf.addConfigValue("bp.allowItem");
		conf.addConfigValue("bp.allowPlace");
		
		conf.addConfigValue("chat.channels");
		conf.addConfigValue("chat.enabled");
		conf.addConfigValue("chat.format");
		
		conf.addConfigValue("defaults.gamemode");
		
		conf.addConfigValue("permissionOverride");
		
		// ---==EVENTS==---
		// Block Protection
		getServer().getPluginManager().registerEvents(new Event_BlockBreakEvent_BlockProtection(this), this);
		getServer().getPluginManager().registerEvents(new Event_BlockPlaceEvent_BlockProtection(this), this);
		getServer().getPluginManager().registerEvents(new Event_ItemEvents_BlockProtection(this), this);
		// Chat
		getServer().getPluginManager().registerEvents(new Event_AsyncPlayerChatEvent_PlayerChatFormatter(this), this);
		// Commands
		getServer().getPluginManager().registerEvents(new Event_PlayerCommandPreprocess_AltCmdHandler(this), this);
		getServer().getPluginManager().registerEvents(new Event_ServerCommand_AltCmdHandler(this), this);
		// User
		getServer().getPluginManager().registerEvents(new Event_PlayerJoin_DB(this), this);
	}
	
	@Override
	public void onDisable() {
		conf.save();
	}
	
	public CommandRegister getCR() {
		return this.cr;
	}
	
	public PermissionsRegister getPR() {
		return this.pr;
	}
	
	public RankManager getRM() {
		return this.rm;
	}
	
	public ConfigFile getConf() {
		return conf;
	}
	
	public Database getDB() {
		return this.db;
	}
	
	public UserRegister getUR() {
		return this.ur;
	}

	public PluginRegister getPlug() {
		return this.plug;
	}
}
