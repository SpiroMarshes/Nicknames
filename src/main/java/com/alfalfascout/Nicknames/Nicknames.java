package com.alfalfascout.Nicknames;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Nicknames extends JavaPlugin implements Listener {
    public FileConfiguration nicks;
    
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("nick").setExecutor(new NickCommand(this));
        loadNicks();
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        setNick(player);
    }
    
    @EventHandler
    public void onChatTab(PlayerChatTabCompleteEvent e) {
        getLogger().info("chat tab");
    }
    
    @EventHandler
    public void onCommandTab(TabCompleteEvent e) {
        getLogger().info("command tab");
    }
    
    public void onDisable() {
        saveNicks();
    }
    
    public void loadNicks() {
        nicks = getConfig();
    }
    
    public void saveNicks() {
        saveConfig();
    }
    
    public void setNick(Player player) {
        for (String nickname : nicks.getValues(true).keySet()) {
            UUID playerId = player.getUniqueId();
            UUID configId = UUID.fromString(nicks.getString(nickname + ".id"));
            
            if (playerId.equals(configId)) {
                player.setDisplayName(nickname);
                player.setPlayerListName(nickname);
                break;
            }
        }
    }
    
    public UUID getUUIDFromNick(String nickname) {
        return UUID.fromString(nicks.getString(nickname + ".id"));
    }
    
    public Player getPlayerFromNick(String nickname) {
        return Bukkit.getPlayer(getUUIDFromNick(nickname));
    }
}
