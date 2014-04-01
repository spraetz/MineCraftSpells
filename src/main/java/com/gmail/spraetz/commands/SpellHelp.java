package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

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
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        //Check if it has any args.
        if(args.length == 0){
            listAllSpells(commandSender);
        }
        if(args.length == 1){

            String spellName = args[0];

            // Check if the arg is a spell name.
            if(Spellbook.spellExists(spellName, plugin)){
                sendSpellDescription(commandSender, spellName);
            }
            else{
                commandSender.sendMessage("There is no spell called " + spellName);
            }
        }
        else if(args.length > 1){
            return false;
        }

        return true;
    }

    private void sendSpellDescription(CommandSender commandSender, String spellName){
        commandSender.sendMessage(ChatColor.BOLD + spellName + ": " + ChatColor.LIGHT_PURPLE + getReagentString(spellName));
        commandSender.sendMessage(ChatColor.GOLD + plugin.getConfig().getString("spells." + spellName + ".description"));
    }

    private void listAllSpells(CommandSender commandSender){
        //Get the list of spell names
        Set<String> spellNames = plugin.getConfig().getConfigurationSection("spells").getKeys(false);

        commandSender.sendMessage(ChatColor.GOLD + "Spell names: " + ChatColor.AQUA + spellNames.toString());
        commandSender.sendMessage(ChatColor.GOLD + "To learn more about a spell, try /spells spell_name.");
    }

    private String getReagentString(String spellName){
        ItemStack[] items = Spellbook.getReagents(spellName, plugin);
        String reagentString = "";
        for(int i = 0; i < items.length; i++){
            reagentString += items[i].getType().toString() + ": " + items[i].getAmount();
            if(i != items.length - 1){
                reagentString += ", ";
            }
        }
        return reagentString;
    }
}
