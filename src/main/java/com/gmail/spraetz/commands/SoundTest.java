package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by spraetz on 3/16/14.
 */
public class SoundTest implements CommandExecutor {

    private final MineCraftSpells plugin;

    public SoundTest(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player)commandSender;

        plugin.getEffects().playSound(plugin, p.getLocation(), args[0]);

        return true;

    }
}
