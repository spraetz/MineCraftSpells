package com.gmail.spraetz.listeners;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by spraetz on 2/16/14.
 */
public class CastSpellListener implements Listener {

    MineCraftSpells plugin;

    public CastSpellListener(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void castSpell(PlayerInteractEvent event) {

        // Check if they have a spellbook in their hand.
        if(Spellbook.isSpellbook(event.getPlayer().getItemInHand(), plugin) &&
                (event.getAction() == Action.LEFT_CLICK_AIR)){

            // See if the display name matches the name of a spell.
            String displayName = event.getPlayer().getItemInHand().getItemMeta().getDisplayName();

            if(Spellbook.spellExists(displayName, plugin)){

                Class spellClass = Spellbook.getSpellClass(displayName, plugin);
                try{
                    Constructor constructor = spellClass.getConstructor(new Class[]{PlayerInteractEvent.class, MineCraftSpells.class});
                    Object obj = constructor.newInstance(event, plugin);
                    Method method = obj.getClass().getMethod("cast", String.class);
                    method.invoke(obj, displayName);
                }
                catch(NoSuchMethodException e){
                    // Do nothing.  This just means we're casting a spell that doesn't use PlayerInteractEvent
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
