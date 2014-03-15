package com.gmail.spraetz;

import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.plugin.Analytics;
import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.FireBall;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by spraetz on 3/2/14.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({PlayerInteractEvent.class, MineCraftSpells.class, Spellbook.class})
public class FirstTest {

    @BeforeClass
    public static void testClassSetup(){
        MockGateway.MOCK_STANDARD_METHODS = false;
    }

    @Test
    public void testCastSpell(){
        //Mock the analytics
        Analytics analytics = PowerMockito.mock(Analytics.class);
        doNothing().when(analytics).trackSpellCast(any(Player.class), anyString());

        //Mock the damn fireball
        Fireball fireball = PowerMockito.mock(Fireball.class);

        //Mock the world
        World world = PowerMockito.mock(World.class);
        when(world.spawn(any(Location.class), any(Class.class))).thenReturn(fireball);

        //Mock the config
        FileConfiguration config = PowerMockito.mock(FileConfiguration.class);
        when(config.get(eq("spells.fire_ball.settings.radius"))).thenReturn(1);
        when(config.getString(eq("spells.fire_ball.class"))).thenReturn("com.gmail.spraetz.spells.FireBall");

        //Mock the plugin
        MineCraftSpells plugin = PowerMockito.mock(MineCraftSpells.class);
        when(plugin.getConfig()).thenReturn(config);
        when(plugin.getAnalytics()).thenReturn(analytics);

        //Mock item meta for spellbook
        ItemMeta itemMeta = mock(ItemMeta.class);
        when(itemMeta.getDisplayName()).thenReturn("fire_ball");
        when(itemMeta.getLore()).thenReturn(new ArrayList<String>(){{
            add("fire_ball: 64");
            add("heal_self: 64");
            add("heal_other: 64");
        }});

        //Mock a spellbook
        ItemStack spellbook = mock(ItemStack.class);
        when(spellbook.getItemMeta()).thenReturn(itemMeta);

        //Mock the player
        Player player = mock(Player.class);
        when(player.getItemInHand()).thenReturn(spellbook);
        when(player.getWorld()).thenReturn(world);
        when(player.getEyeLocation()).thenReturn(new Location(world, 0, 0 , 0));

        //Mock the event
        PlayerInteractEvent event = PowerMockito.mock(PlayerInteractEvent.class);
        when(event.getPlayer()).thenReturn(player);
        when(event.getAction()).thenReturn(Action.LEFT_CLICK_AIR);

        //Holy shit we're gonna do something that isn't a mock!
        CastSpellListener listener = new CastSpellListener(plugin);
        listener.castSpell(event);

        //Verify that our spell was cast and our spellbook updated.
        verify(itemMeta).setLore(new ArrayList<String>() {{
            add("fire_ball: 63");
            add("heal_self: 64");
            add("heal_other: 64");
        }});

    }
}
