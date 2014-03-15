package com.gmail.spraetz;

import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.listeners.CastTouchSpellListener;
import com.gmail.spraetz.plugin.Analytics;
import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spell;
import com.gmail.spraetz.spells.Spellbook;
import com.gmail.spraetz.spells.TouchSpell;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by spraetz on 3/2/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PlayerInteractEvent.class, MineCraftSpells.class})
public class CastSpellTests {

    FileConfiguration config;

    @Before
    public void testClassSetup(){
        File configFile = new File(new File("").getAbsolutePath() + "/src/main/resources/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        MockGateway.MOCK_STANDARD_METHODS = false;
    }

    @Test
    public void testCastSpell(){

        //Find the list of all the spells:
        Set<String> spells = config.getConfigurationSection("spells").getKeys(false);

        for(String spellName : spells){

            //Mock the analytics
            Analytics analytics = PowerMockito.mock(Analytics.class);
            doNothing().when(analytics).trackSpellCast(any(Player.class), anyString());

            //Mock the damn fireball
            Fireball fireball = PowerMockito.mock(Fireball.class);

            //Mock a target block
            Block block = PowerMockito.mock(Block.class);

            //Mock the world
            World world = PowerMockito.mock(World.class);
            when(world.spawn(any(Location.class), any(Class.class))).thenReturn(fireball);
            when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);

            when(block.getLocation()).thenReturn(new Location(world, 0, 0, 0));
            when(block.getType()).thenReturn(Material.STONE);

            //Mock the plugin
            MineCraftSpells plugin = PowerMockito.mock(MineCraftSpells.class);
            when(plugin.getConfig()).thenReturn(config);
            when(plugin.getAnalytics()).thenReturn(analytics);

            //Mock item meta for spellbook
            ItemMeta itemMeta = mock(ItemMeta.class);
            when(itemMeta.getDisplayName()).thenReturn(spellName);
            ArrayList<String> loreList = new ArrayList<String>();
            loreList.add(spellName + Spellbook.LORE_STRING_SEPARATOR + "64");
            when(itemMeta.getLore()).thenReturn(loreList);

            //Mock a spellbook
            ItemStack spellbook = mock(ItemStack.class);
            when(spellbook.getItemMeta()).thenReturn(itemMeta);

            //Mock the player
            Player player = mock(Player.class);
            when(player.getItemInHand()).thenReturn(spellbook);
            when(player.getWorld()).thenReturn(world);
            when(player.getEyeLocation()).thenReturn(new Location(world, 0, 0, 0));
            when(player.getTargetBlock(any(HashSet.class), anyInt())).thenReturn(block);
            when(player.getLocation()).thenReturn(new Location(world, 0, 0, 0, 0 ,0));

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
            verify(itemMeta).setLore(loreListToVerify);
        }
    }
}
