package com.gmail.spraetz.commands;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spell;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by spraetz on 2/17/14.
 */
public class ChargeSpellbook implements CommandExecutor {

    private final MineCraftSpells plugin;

    public ChargeSpellbook(MineCraftSpells plugin){
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
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p = (Player)commandSender;
        ItemStack book = p.getItemInHand();

        try{
            validateArguments(args);

            //Get the number of charges they'd like to put on the book.
            Integer chargesToAdd = getNumberOfCharges(args);

            //Get the name of the spell
            String spellName = args[0];

            validateItem(book, spellName);

            //Find the reagent for that spell
            ItemStack[] reagents = Spellbook.getReagents(spellName, plugin);

            //See how many we have in our inventory
            Integer[] reagentCounts = getReagentCounts(reagents, p);

            //See how many charges the book has already for that spell.
            Integer currentCharges = Spellbook.getCharges(book, spellName);

            //Find how many charges we CAN add.
            chargesToAdd = getChargesToAdd(currentCharges, chargesToAdd, reagents, reagentCounts);

            //Remove them from inventory
            removeReagentsFromInventory(p, reagents, chargesToAdd);

            //Add the charges to the book
            Spellbook.addCharges(book, spellName, chargesToAdd);

            return true;
        }
        catch(ChargeSpellException e){
            p.sendMessage(e.getMessage());
            return true;
        }
        catch(CommandException e){
            p.sendMessage(e.getMessage());
            return false;
        }
    }

    private Integer getChargesToAdd(Integer currentCharges, Integer chargesToAdd, ItemStack[] reagents, Integer[] reagentCounts) throws ChargeSpellException {

        // Find the limiting reagent.
        Integer smallestNumber = (Spellbook.MAX_SPELL_CHARGES - currentCharges) > chargesToAdd
                ? chargesToAdd : (Spellbook.MAX_SPELL_CHARGES - currentCharges);

        for(int i = 0; i < reagents.length; i++){

            if(reagentCounts[i] / reagents[i].getAmount() < smallestNumber){
                smallestNumber = reagentCounts[i] / reagents[i].getAmount();
            }
        }

        if(smallestNumber == 0){
            throw new ChargeSpellException("You can't charge that spellbook right now.  Is it full or are you missing reagents");
        }

        return smallestNumber;
    }

    private Integer getNumberOfCharges(String[] args) throws CommandException {

        if(args.length == 2){
            try {
                return Integer.parseInt(args[1]);
            } catch(NumberFormatException e) {
                throw new CommandException("Second argument must be an integer. Found: " + args[2]);
            }
        }
        else{
            return Spellbook.MAX_SPELL_CHARGES;
        }
    }

    private void removeReagentsFromInventory(Player p, ItemStack[] reagents, Integer numberOfCharges){
        // Remove items from inventory
        for(ItemStack reagent : reagents){

            Integer leftToFind = reagent.getAmount() * numberOfCharges;

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
    }

    private void validateItem(ItemStack book, String spellName) throws ChargeSpellException {
        if(book.getType() != Material.BOOK){
            throw new ChargeSpellException("Must be holding a book to charge it!");
        }
        if(!Spellbook.spellExists(spellName, plugin)){
            throw new ChargeSpellException("A spell with this name doesn't exist: " + spellName);
        }
        if(!Spellbook.canCharge(book, spellName)){
            throw new ChargeSpellException("That book is already full of spells!");
        }
    }

    private void validateArguments(String[] args) throws CommandException {
        if(args.length < 1 || args.length > 2){
            throw new CommandException("Incorrect number of arguments: " + args.length);
        }
    }

    private Class<Spell> getSpellClass(String spellName) throws ChargeSpellException {
        Class spellClass = Spellbook.getSpellClass(spellName, plugin);
        if(spellClass == null){
            throw new ChargeSpellException("There is no spell by that name: " + spellName);
        }
        return spellClass;
    }

    public class ChargeSpellException extends Exception{
        public ChargeSpellException(String message){
            super(message);
        }
    }

    public class CommandException extends Exception{
        public CommandException(String message){
            super(message);
        }
    }

    public Integer[] getReagentCounts(ItemStack[] reagents, Player p){
        // Find out how many of each reagent we have in our inventory.
        Integer[] reagentCounts = new Integer[reagents.length];

        for(int i = 0; i < reagents.length; i++){

            reagentCounts[i] = 0;

            for(ItemStack item : p.getInventory().getContents()){

                if(item == null){
                    continue;
                }
                if(item.getType() == reagents[i].getType()){
                    reagentCounts[i] += item.getAmount();
                }
            }
        }

        return reagentCounts;
    }
}
