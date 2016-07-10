package com.alfalfascout.Nicknames;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class NickCommand implements CommandExecutor, Listener {
    private final Nicknames plugin;
    
    public NickCommand(Nicknames plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void preProcess(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage();
        for (String nickname : plugin.nicks.getValues(true).keySet()) {
            if (message.contains(nickname)) {
                message.replace(nickname,
                        plugin.nicks.getString(nickname + ".id"));
                e.getPlayer().sendMessage("ooo");
                break;
            }
        }
        e.setMessage(message);
    }
    
    public boolean onCommand(CommandSender sender, Command cmd,
            String label, String[] args) {
        
        if (cmd.getName().equalsIgnoreCase("nick")) {
            if (args.length > 0) {
                if (args[0].equals("unset")) {
                    if (sender instanceof Player) {
                        Player player = (Player) sender;
                        
                        if (!plugin.nicks.contains(player.getDisplayName())) {
                            sender.sendMessage("You don't have a nickname.");
                            return true;
                        }
                        
                        String nickName = player.getDisplayName();
                        String realName = plugin.nicks.getString(
                                player.getDisplayName() + ".name");
                        player.setDisplayName(realName);
                        player.setPlayerListName(realName);
                        
                        plugin.nicks.set(nickName, null);
                        plugin.saveNicks();
                        
                        sender.sendMessage("Removed your nickname.");
                        return true;
                    }
                }
                
                else {
                    if (sender instanceof Player) {
                        String joinedArgs = String.join(" ", args);
                        Player player = (Player) sender;
                        
                        if (plugin.nicks.contains(player.getDisplayName())) {
                            ConfigurationSection oldData = 
                                    plugin.nicks.getConfigurationSection(
                                            player.getDisplayName());
                            plugin.nicks.set(joinedArgs, oldData);
                            plugin.nicks.set(player.getDisplayName(), null);
                        }
                        else {
                            plugin.nicks.set(joinedArgs + ".name",
                                    player.getDisplayName());
                            plugin.nicks.set(joinedArgs + ".id",
                                    player.getUniqueId().toString());
                        }
                        
                        player.setDisplayName(joinedArgs);
                        player.setPlayerListName(joinedArgs);
                        sender.sendMessage("Your nickname is now " + joinedArgs);
                        return true;
                    }
                }
            }
            
            sendUsage(sender);
            
            return true;
        }
        
        return false;
    }
    
    public void sendUsage(CommandSender sender) {
        String usage = "To set your nickname: /nick Name\n" +
                "To remove your nickname: /nick unset";
        sender.sendMessage(usage);
    }
}
