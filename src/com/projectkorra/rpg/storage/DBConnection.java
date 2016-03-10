package com.projectkorra.rpg.storage;

import com.projectkorra.rpg.ProjectKorraRPG;

public class DBConnection {

	public static Database sql;

	private static String engine;
	private static String host;
	private static int port;
	private static String db;
	private static String user;
	private static String pass;
	private static boolean isOpen;

	public static void init() {
		open();
		if (!isOpen) return;
		if (!sql.tableExists("pk_avatars")) {
			ProjectKorraRPG.log.info("Creating pk_avatars table.");
			String query = "CREATE TABLE `pk_avatars` ("
					+ "`id` INTEGER PRIMARY KEY,"
					+ "`uuid` TEXT(255),"
					+ "`player` TEXT(255),"
					+ "`element` TEXT(255));";
			sql.modifyQuery(query);
		}
	}
	
	public static void close() {
		isOpen = false;
		sql.close();
	}
	
	public static void open() {
		engine = ProjectKorraRPG.plugin.getConfig().getString("Storage.engine");
		host = ProjectKorraRPG.plugin.getConfig().getString("Storage.MySQL.host");
		port = ProjectKorraRPG.plugin.getConfig().getInt("Storage.MySQL.port");
		pass = ProjectKorraRPG.plugin.getConfig().getString("Storage.MySQL.pass");
		db = ProjectKorraRPG.plugin.getConfig().getString("Storage.MySQL.db");
		user = ProjectKorraRPG.plugin.getConfig().getString("Storage.MySQL.user");
		if (isOpen) 
			return;
		if (engine.equalsIgnoreCase("mysql")) {
			sql = new MySQL(ProjectKorraRPG.log, "Establishing MySQL Connection... ", host, port, user, pass, db);
			if (((MySQL) sql).open() == null) {
				ProjectKorraRPG.log.severe("Failed to open the database.");
				return;
			}
			isOpen = true;
			ProjectKorraRPG.log.info("Database connection established.");
		} else {
			sql = new SQLite(ProjectKorraRPG.log, "Establishing SQLite Connection... ", "projectkorra.db", ProjectKorraRPG.plugin.getDataFolder().getAbsolutePath());
			if (((SQLite) sql).open() == null) {
				ProjectKorraRPG.log.severe("Disabling due to database error");
				return;
			}
			isOpen = true;
			ProjectKorraRPG.log.info("Database connection established.");
		}
	}
	
	public static boolean isOpen() {
		return isOpen;
	}
}