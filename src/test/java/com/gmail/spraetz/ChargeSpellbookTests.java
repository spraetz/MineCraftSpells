package com.gmail.spraetz;

import com.gmail.spraetz.commands.ChargeSpellbook;
import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by spraetz on 3/14/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({MineCraftSpells.class})
public class ChargeSpellbookTests {
    FileConfiguration config;

    @Before
    public void testClassSetup(){
        File configFile = new File(new File("").getAbsolutePath() + "/src/main/resources/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        MockGateway.MOCK_STANDARD_METHODS = false;
    }

    @Test
    public void testChargeSpellbook(){

        Integer CHARGES_TO_ADD = 10;
        Integer EXISTING_CHARGES = 10;

        //Find the list of all the spells:
        assert config != null;
        Set<String> spells = config.getConfigurationSection("spells").getKeys(false);

        for(String spellName : spells){

            MineCraftSpells plugin = PowerMockito.mock(MineCraftSpells.class);
            when(plugin.getConfig()).thenReturn(config);
            ChargeSpellbook chargeCommand = new ChargeSpellbook(plugin);

            //Mock item meta for spellbook
            ItemMeta itemMeta = mock(ItemMeta.class);
            when(itemMeta.getDisplayName()).thenReturn(spellName);
            ArrayList<String> loreList = new ArrayList<String>();
            loreList.add(spellName + Spellbook.LORE_STRING_SEPARATOR + EXISTING_CHARGES);
            when(itemMeta.getLore()).thenReturn(loreList);

            //Mock a spellbook
            ItemStack spellbook = mock(ItemStack.class);
            when(spellbook.getItemMeta()).thenReturn(itemMeta);
            when(spellbook.getType()).thenReturn(Material.BOOK);

            //Mock inventory contents
            ItemStack[] items = getReagentsForInventory(spellName, CHARGES_TO_ADD, plugin);

            //Mock a PlayerInventory
            PlayerInventory playerInventory = PowerMockito.mock(PlayerInventory.class);
            when(playerInventory.getContents()).thenReturn(items);

            //Mock a player
            Player player = PowerMockito.mock(Player.class);
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
            verify(itemMeta).setLore(lore);
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
