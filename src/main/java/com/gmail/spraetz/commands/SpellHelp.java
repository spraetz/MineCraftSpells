package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by spraetz on 3/21/14.
 */
public class SpellHelp implements CommandExecutor {

    private final MineCraftSpells plugin;

    public SpellHelp(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        //Get the list of spell names
        Set<String> spellNames = plugin.getConfig().getConfigurationSection("spells").getKeys(false);

        //Iterate over them and return a message to the commandSender with info
        for(String spellName : spellNames){
            ItemStack[] items = Spellbook.getReagents(spellName, plugin);
            String reagentString = "";
            for(int i = 0; i < items.length; i++){
                reagentString += items[i].getType().toString() + ": " + items[i].getAmount();
                if(i != items.length - 1){
                    reagentString += ", ";
                }
            }

            commandSender.sendMessage(ChatColor.AQUA + spellName + ": " + ChatColor.RED + reagentString);
        }

        return true;
    }
}
