package com.gmail.spraetz;

import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.FireBall;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by spraetz on 3/2/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PlayerInteractEvent.class, MineCraftSpells.class, Spellbook.class})
public class FirstTest {

    @Test
    public void testCastSpell(){

        MineCraftSpells plugin = PowerMockito.mock(MineCraftSpells.class);

        //Mock item meta for spellbook
        ItemMeta itemMeta = mock(ItemMeta.class);
        when(itemMeta.getDisplayName()).thenReturn("fire_ball");
        when(itemMeta.getLore()).thenReturn(new ArrayList<String>(){{
            add("fireball: 64");
            add("heal_self: 64");
            add("heal_other: 64");
        }});

        mockStatic(Spellbook.class);
        when(Spellbook.spellExists(anyString(), any(MineCraftSpells.class))).thenReturn(true);
        when(Spellbook.isSpellbook(any(ItemStack.class), any(MineCraftSpells.class))).thenReturn(true);
        when(Spellbook.getSpellClass(anyString(), any(MineCraftSpells.class))).thenReturn(FireBall.class);

        //Mock a spellbook
        ItemStack spellbook = mock(ItemStack.class);
        when(spellbook.getItemMeta()).thenReturn(itemMeta);

        Player player = mock(Player.class);
        when(player.getItemInHand()).thenReturn(spellbook);

        PlayerInteractEvent event = PowerMockito.mock(PlayerInteractEvent.class);
        when(event.getPlayer()).thenReturn(player);
        when(event.getAction()).thenReturn(Action.LEFT_CLICK_AIR);

        CastSpellListener listener = new CastSpellListener(plugin);
        listener.castSpell(event);

    }
}
