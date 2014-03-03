package com.gmail.spraetz;

import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

/**
 * Created by spraetz on 3/2/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(PlayerInteractEvent.class)
public class FirstTest {
    @Test
    public void testCastSpell(){

        // Mock the plugin
        MineCraftSpells plugin = PowerMockito.mock(MineCraftSpells.class);

        // Mock the playerInteractEvent
        PlayerInteractEvent mockEvent = PowerMockito.mock(PlayerInteractEvent.class);

        // Mock a spellbook.
        ItemStack spellBook = new ItemStack(Material.BOOK, 1);
        ItemMeta itemMeta = spellBook.getItemMeta();
        itemMeta.setDisplayName("fire_ball");
        ArrayList<String> loreList = (ArrayList<String>)itemMeta.getLore();
        loreList.add("fire_ball: 64");
        itemMeta.setLore(loreList);
        spellBook.setItemMeta(itemMeta);

        // Mock a player.
        Player mockPlayer = PowerMockito.mock(Player.class);
        PowerMockito.when(mockPlayer.getName()).thenReturn("spraetz");
        PowerMockito.when(mockPlayer.getItemInHand()).thenReturn(new ItemStack(Material.BOOK, 1));

        // Attach the player to the event.
        PowerMockito.when(mockEvent.getPlayer()).thenReturn(mockPlayer);

        // Create instance of our Listener
        CastSpellListener listener = new CastSpellListener(plugin);
        listener.castSpell(mockEvent);
    }
}
