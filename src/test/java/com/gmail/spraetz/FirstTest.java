package com.gmail.spraetz;

import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.anyObject;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by spraetz on 3/2/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({MineCraftSpells.class, PlayerInteractEvent.class})
public class FirstTest {
    @Test
    public void testCastSpell(){

        // Mock config
        FileConfiguration config = PowerMockito.mock(FileConfiguration.class);
        //when(config.getString(anyString(), anyString())).thenReturn("com.spraetz.gmail.spells.FireBall");

        // Mock the plugin
        MineCraftSpells plugin = PowerMockito.mock(MineCraftSpells.class);
        when(plugin.equals(anyObject())).thenReturn(true);
        when(plugin.getConfig()).thenReturn(config);

        // Mock a spellbook.
        ItemStack spellbook = mock(ItemStack.class);

        // Mock itemMeta for spellbook
        ItemMeta itemMeta = mock(ItemMeta.class);
        when(itemMeta.getDisplayName()).thenReturn("fire_ball");
        when(itemMeta.getLore()).thenReturn(new ArrayList<String>(){{
            add("fire_ball: 64");
            add("heal_self: 64");
            add("heal_other: 64");
        }});

        when(spellbook.getItemMeta()).thenReturn(itemMeta);

        // Mock a player.
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("spraetz");
        when(player.getItemInHand()).thenReturn(spellbook);

        // Mock the playerInteractEvent
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);

        // Attach the player to the event.
        when(event.getPlayer()).thenReturn(player);
        when(event.getAction()).thenReturn(Action.LEFT_CLICK_AIR);

        // Create instance of our Listener
        CastSpellListener listener = new CastSpellListener(plugin);
        listener.castSpell(event);
    }
}
