package com.gmail.spraetz;

import com.gmail.spraetz.commands.ChargeSpellbook;
import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by spraetz on 3/14/14.
 */

@RunWith(PowerMockRunner.class)
public class ChargeSpellbookTest extends BaseTest{

    @Test
    public void testChargeSpellbook(){

        Integer CHARGES_TO_ADD = 10;
        Integer EXISTING_CHARGES = 10;

        //Find the list of all the spells:
        String[] spells = getAllSpellNames();

        for(String spellName : spells){

            //Mock item meta for spellbook
            when(spellbookMeta.getDisplayName()).thenReturn(spellName);
            ArrayList<String> loreList = new ArrayList<String>();
            loreList.add(spellName + Spellbook.LORE_STRING_SEPARATOR + EXISTING_CHARGES);
            when(spellbookMeta.getLore()).thenReturn(loreList);

            //Mock inventory contents
            ItemStack[] items = getReagentsForInventory(spellName, CHARGES_TO_ADD, plugin);
            when(playerInventory.getContents()).thenReturn(items);

            chargeSpellbook(player, getChargeArgs(spellName, CHARGES_TO_ADD));

            //Verify it didn't error
            verify(player, never()).sendMessage(anyString());

            //Verify we set the book's lore.
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(spellName + Spellbook.LORE_STRING_SEPARATOR + (CHARGES_TO_ADD + EXISTING_CHARGES));
            verify(spellbookMeta).setLore(lore);
        }
    }

    @Test
    public void testChargeSpellAtMaxCharges(){
        //Get a spell name
        String spellName = (String) config.getConfigurationSection("spells").getKeys(false).toArray()[0];

        //Get a spellbook with max charges for a spell.
        ArrayList<String> loreList = new ArrayList<String>();
        loreList.add(spellName + Spellbook.LORE_STRING_SEPARATOR + Spellbook.MAX_SPELL_CHARGES);
        when(spellbookMeta.getLore()).thenReturn(loreList);

        //Put some reagents in the player's inventory
        ItemStack[] items = getReagentsForInventory(spellName, 1, plugin);
        when(playerInventory.getContents()).thenReturn(items);

        chargeSpellbook(player, getChargeArgs(spellName, 1));

        verify(player).sendMessage("You can't charge that spellbook right now.  Is it full or are you missing reagents");
    }

    @Test
    public void testChargeSpellbookTooManySpells(){

        Object[] objectArray = config.getConfigurationSection("spells").getKeys(false).toArray();
        String[] spellNames =  Arrays.asList(objectArray).toArray(new String[objectArray.length]);

        //Get a spell name.
        String spellName = spellNames[0];

        //Create a full spellbook.
        ArrayList<String> loreList = new ArrayList<String>();
        for(int i = 0; i < Spellbook.MAX_NUMBER_OF_SPELLS_PER_BOOK; i++){
            loreList.add(spellNames[i] + Spellbook.LORE_STRING_SEPARATOR + Spellbook.MAX_SPELL_CHARGES);
        }
        when(spellbookMeta.getLore()).thenReturn(loreList);
        when(spellbookMeta.getDisplayName()).thenReturn(spellName);

        //Try to charge the book with a spell that doesn't already exist on it.
        chargeSpellbook(player, getChargeArgs(spellNames[Spellbook.MAX_NUMBER_OF_SPELLS_PER_BOOK], 1));

        verify(player).sendMessage("That book is already full of spells!");
    }

    @Test
    public void testChargeNewSpellbook(){

        String spellName = getAnySpellName();

        //Mock inventory contents
        ItemStack[] items = getReagentsForInventory(spellName, 1, plugin);
        when(playerInventory.getContents()).thenReturn(items);

        chargeSpellbook(player, getChargeArgs(spellName, 1));

        ArrayList<String> newLore = new ArrayList<String>();
        newLore.add(spellName + Spellbook.LORE_STRING_SEPARATOR + "1");
        verify(spellbookMeta).setLore(newLore);
        verify(spellbookMeta).setDisplayName(spellName);
    }

    @Test
    public void testRechargeSpellbook(){

        String[] spellNames =  getAllSpellNames();

        //Create a full spellbook.
        ArrayList<String> loreList = new ArrayList<String>();
        for(int i = 0; i < Spellbook.MAX_NUMBER_OF_SPELLS_PER_BOOK; i++){
            loreList.add(spellNames[i] + Spellbook.LORE_STRING_SEPARATOR + "0");
        }
        when(spellbookMeta.getLore()).thenReturn(loreList);
        when(spellbookMeta.getDisplayName()).thenReturn(spellNames[0]);

        //Recharge each spell on the book
        for(int i = 0; i < Spellbook.MAX_NUMBER_OF_SPELLS_PER_BOOK; i++){

            //Mock inventory contents
            ItemStack[] items = getReagentsForInventory(spellNames[i], 1, plugin);
            when(playerInventory.getContents()).thenReturn(items);

            chargeSpellbook(player, getChargeArgs(spellNames[i], 1));

            ArrayList<String> newLore = new ArrayList<String>();
            for(int j = 0; j < Spellbook.MAX_NUMBER_OF_SPELLS_PER_BOOK; j++){
                if(j <= i){
                    newLore.add(spellNames[j] + Spellbook.LORE_STRING_SEPARATOR + "1");
                }
                else{
                    newLore.add(spellNames[j] + Spellbook.LORE_STRING_SEPARATOR + "0");
                }
            }

            verify(spellbookMeta, atLeastOnce()).setLore(newLore);
        }
    }

    @Test
    public void testChargeSpellbookNoReagents(){
        String spellName = getAnySpellName();

        when(spellbookMeta.getLore()).thenReturn(new ArrayList<String>());

        //Mock inventory contents
        ItemStack[] items = getReagentsForInventory(spellName, 0, plugin);
        when(playerInventory.getContents()).thenReturn(items);

        chargeSpellbook(player, getChargeArgs(spellName, 1));

        verify(player).sendMessage("You can't charge that spellbook right now.  Is it full or are you missing reagents");
    }

    public void chargeSpellbook(Player player, String[] args){
        Command command = PowerMockito.mock(Command.class);
        String commandString = "charge";
        ChargeSpellbook chargeCommand = new ChargeSpellbook(plugin);
        chargeCommand.onCommand(player, command, commandString, args);
    }

    public ItemStack[] getReagentsForInventory(String spellName, Integer chargesToAdd, MineCraftSpells plugin){
        ItemStack[] items = Spellbook.getReagents(spellName, plugin);
        for(ItemStack item : items){
            item.setAmount(item.getAmount() * chargesToAdd);
        }
        return items;
    }

    public String getAnySpellName(){
        return (String) config.getConfigurationSection("spells").getKeys(false).toArray()[0];
    }

    public String[] getAllSpellNames(){
        Object[] objectArray = config.getConfigurationSection("spells").getKeys(false).toArray();
        return Arrays.asList(objectArray).toArray(new String[objectArray.length]);
    }

    public String[] getChargeArgs(String spellName, Integer charges){
        return new String[]{
                spellName,
                charges.toString()
        };
    }
}
