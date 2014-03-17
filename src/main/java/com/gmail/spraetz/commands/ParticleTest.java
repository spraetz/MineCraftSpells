package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by spraetz on 3/16/14.
 */
public class ParticleTest implements CommandExecutor{

    private final MineCraftSpells plugin;

    public ParticleTest(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player)commandSender;

        plugin.getEffects().playVisual(plugin, p.getLocation(), args[0], .2F, .2F, .1F, 20, 2F);

        return true;

    }
}
