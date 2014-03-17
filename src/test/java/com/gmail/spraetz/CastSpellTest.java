package com.gmail.spraetz;

import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.listeners.CastTouchSpellListener;
import com.gmail.spraetz.plugin.Analytics;
import com.gmail.spraetz.spells.Spell;
import com.gmail.spraetz.spells.Spellbook;
import com.gmail.spraetz.spells.TouchSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by spraetz on 3/2/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PlayerInteractEvent.class})
public class CastSpellTest extends BaseTest{

    @Test
    public void testCastSpell(){

        //Find the list of all the spells:
        Set<String> spells = config.getConfigurationSection("spells").getKeys(false);

        for(String spellName : spells){

            //Mock the analytics
            Analytics analytics = PowerMockito.mock(Analytics.class);
            doNothing().when(analytics).trackSpellCast(any(Player.class), anyString());
            when(plugin.getAnalytics()).thenReturn(analytics);

            //Mock the damn fireball
            Fireball fireball = PowerMockito.mock(Fireball.class);
            WitherSkull witherSkull = PowerMockito.mock(WitherSkull.class);

            //Mock a target block
            Block block = PowerMockito.mock(Block.class);

            //Mock the world
            when(world.spawn(any(Location.class), eq(Fireball.class))).thenReturn(fireball);
            when(world.spawn(any(Location.class), eq(WitherSkull.class))).thenReturn(witherSkull);
            when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);
            when(world.getSpawnLocation()).thenReturn(new Location(world, 0, 0, 0));

            when(block.getLocation()).thenReturn(new Location(world, 0, 0, 0));
            when(block.getType()).thenReturn(Material.STONE);

            //Mock item meta for spellbook
            when(spellbookMeta.getDisplayName()).thenReturn(spellName);
            ArrayList<String> loreList = new ArrayList<String>();
            loreList.add(spellName + Spellbook.LORE_STRING_SEPARATOR + "64");
            when(spellbookMeta.getLore()).thenReturn(loreList);

            //Mock the player
            when(player.getWorld()).thenReturn(world);
            when(player.getEyeLocation()).thenReturn(new Location(world, 0, 0, 0));
            when(player.getTargetBlock(any(HashSet.class), anyInt())).thenReturn(block);
            when(player.getLocation()).thenReturn(new Location(world, 0, 0, 0, 0 ,0));
            when(player.getBedSpawnLocation()).thenReturn(null);

            //Holy shit we're gonna do something that isn't a mock!
            //Get the spell class for the spell.
            Class c = null;
            try{
                c = Class.forName(plugin.getConfig().getString("spells." + spellName + ".class"));
            }
            catch (ClassNotFoundException e) {
                assert false : "test class not found";
            }

            if(Spell.class.isAssignableFrom(c)){
                //Mock the event
                PlayerInteractEvent event = PowerMockito.mock(PlayerInteractEvent.class);
                when(event.getPlayer()).thenReturn(player);
                when(event.getAction()).thenReturn(Action.LEFT_CLICK_AIR);

                CastSpellListener listener = new CastSpellListener(plugin);
                listener.castSpell(event);
            }
            else if(TouchSpell.class.isAssignableFrom(c)){
                //Mock the event
                EntityDamageByEntityEvent event = PowerMockito.mock(EntityDamageByEntityEvent.class);
                when(event.getDamager()).thenReturn(player);

                CastTouchSpellListener listener = new CastTouchSpellListener(plugin);
                listener.castTouchSpell(event);
            }
            else{
                assert false : "Spell didn't cast";
            }

            //Verify that our spell was cast and our spellbook updated.
            ArrayList<String> loreListToVerify = new ArrayList<String>();
            loreListToVerify.add(spellName + Spellbook.LORE_STRING_SEPARATOR + "63");
            verify(spellbookMeta).setLore(loreListToVerify);
        }
    }
}
