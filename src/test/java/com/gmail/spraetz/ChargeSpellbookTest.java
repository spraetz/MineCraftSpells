package com.gmail.spraetz;

import com.gmail.spraetz.commands.ChargeSpellbook;
import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.command.Command;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
        Set<String> spells = config.getConfigurationSection("spells").getKeys(false);

        for(String spellName : spells){

            ChargeSpellbook chargeCommand = new ChargeSpellbook(plugin);

            //Mock item meta for spellbook
            when(spellbookMeta.getDisplayName()).thenReturn(spellName);
            ArrayList<String> loreList = new ArrayList<String>();
            loreList.add(spellName + Spellbook.LORE_STRING_SEPARATOR + EXISTING_CHARGES);
            when(spellbookMeta.getLore()).thenReturn(loreList);

            //Mock inventory contents
            ItemStack[] items = getReagentsForInventory(spellName, CHARGES_TO_ADD, plugin);

            //Mock a PlayerInventory
            PlayerInventory playerInventory = PowerMockito.mock(PlayerInventory.class);
            when(playerInventory.getContents()).thenReturn(items);

            //Mock a player
            when(player.getItemInHand()).thenReturn(spellbook);
            when(player.getInventory()).thenReturn(playerInventory);

            Command command = PowerMockito.mock(Command.class);
            String commandString = "charge";
            String[] args = new String[]{
                spellName,
                CHARGES_TO_ADD.toString()
            };

            chargeCommand.onCommand(player, command, commandString, args);

            //Verify it didn't error
            verify(player, never()).sendMessage(anyString());

            //Verify we set the book's lore.
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(spellName + Spellbook.LORE_STRING_SEPARATOR + (CHARGES_TO_ADD + EXISTING_CHARGES));
            verify(spellbookMeta).setLore(lore);
        }
    }

    public ItemStack[] getReagentsForInventory(String spellName, Integer chargesToAdd, MineCraftSpells plugin){
        ItemStack[] items = Spellbook.getReagents(spellName, plugin);
        for(ItemStack item : items){
            item.setAmount(item.getAmount() * chargesToAdd);
        }
        return items;
    }
}
