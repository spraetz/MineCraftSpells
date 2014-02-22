package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.Engine;
import com.gmail.spraetz.spells.Spell;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by spraetz on 2/17/14.
 */
public class ChargeSpellbook implements CommandExecutor {

    private final Engine plugin;

    public ChargeSpellbook(Engine plugin){
        this.plugin = plugin;
    }

    /*
        Command: chargeSpellbook
        Requirements: Must be holding a book in hand.  If that book is already charged,
            you must be adding the same type of spell.
        args:
            spellName - String, name of spell
            charges - Integer, optional number of charges to add to the spellbook.
                Default will add as many as it can up to 64.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player p = (Player)commandSender;

        ItemStack book = p.getItemInHand();

        // Check if they have a book in hand
        if(book.getType() != Material.BOOK){
            p.sendMessage("Must be holding a book to charge it!");
            return true;
        }

        // Make sure they have a spell name in the args.
        if(strings.length < 1 || strings.length > 2){
            p.sendMessage("Incorrect number of arguments.");
            return false;
        }

        Integer limit = Spellbook.MAX_SPELL_CHARGES;
        if(strings.length == 2){
            try{
                limit = Integer.parseInt(strings[1]);
            }
            catch(Exception e){
                p.sendMessage("charges must be an integer.");
                return false;
            }
        }

        //Get the spell they'd like to charge.
        String spellName = strings[0];

        //Check if there's a spell with that spell name.
        Class spellClass = Spell.getSpellClass(spellName, plugin);

        if(spellClass == null){
            p.sendMessage("There is no spell with that name.  /help spells");
            return true;
        }

        //Make sure if the book is already charged that we're adding the same spell on top.
        if(Spellbook.isSpellbook(book, plugin) && !Spellbook.getSpell(book).equals(spellName)){
            p.sendMessage("That book has a different spell on it!");
            return true;
        }

        Integer currentCharges = Spellbook.getCharges(book);
        ItemStack[] reagents;

        // Get the reagent for adding this spell.
        reagents = Spell.getReagents(spellName, plugin);

        // Find out how many of each reagent we have in our inventory.
        Integer[] reagentsInInventory = new Integer[reagents.length];

        for(int i = 0; i < reagents.length; i++){

            reagentsInInventory[i] = 0;

            for(ItemStack item : p.getInventory().getContents()){

                if(item == null){
                    continue;
                }
                if(item.getType() == reagents[i].getType()){
                    reagentsInInventory[i] += item.getAmount();
                }
            }
        }

        // Find the limiting reagent.
        Integer smallestNumber = (Spellbook.MAX_SPELL_CHARGES - currentCharges) > limit
                ? limit : (Spellbook.MAX_SPELL_CHARGES - currentCharges);

        for(int i = 0; i < reagents.length; i++){

            if(reagentsInInventory[i] / reagents[i].getAmount() < smallestNumber){
                smallestNumber = reagentsInInventory[i] / reagents[i].getAmount();
            }
        }

        // Remove items from inventory
        for(ItemStack reagent : reagents){

            Integer leftToFind = reagent.getAmount() * smallestNumber;

            for(ItemStack item : p.getInventory().getContents()){
                if(item != null && item.getType() == reagent.getType() && leftToFind > 0){

                    Integer amountFound = item.getAmount();

                    //If we find an item and we can remove it all, remove it all.
                    if(item.getAmount() <= leftToFind){
                        p.getInventory().remove(item);
                        leftToFind -= amountFound;
                    }
                    //Otherwise we have to remove some of it.
                    else{
                        item.setAmount(item.getAmount() - leftToFind);
                        leftToFind = 0;
                    }
                }
            }
        }

        if(smallestNumber > 0){
            Spellbook.setSpell(book, spellName);
            Spellbook.addCharges(book, smallestNumber);
        }
        else{
            p.sendMessage("You can't charge that spellbook right now.  Is it full or are you missing reagents?");
        }

        return true;
    }
}
