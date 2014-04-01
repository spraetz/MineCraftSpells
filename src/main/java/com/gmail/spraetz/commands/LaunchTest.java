package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

/**
 * Created by spraetz on 3/16/14.
 */
public class LaunchTest implements CommandExecutor {

    private final MineCraftSpells plugin;

    public LaunchTest(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player)commandSender;



        Arrow arrow = p.launchProjectile(Arrow.class);
        arrow.setVelocity(arrow.getVelocity().multiply(3));

        System.out.println(arrow.getVelocity());
        return true;

    }
}
