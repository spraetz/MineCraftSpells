package com.gmail.spraetz.listeners;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by spraetz on 3/1/14.
 */
public class CastTouchSpellListener implements Listener {
    MineCraftSpells plugin;

    public CastTouchSpellListener(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void castTouchSpell(EntityDamageByEntityEvent event) {

        //If the one causing damage is a player...
        if(event.getDamager() instanceof Player){

            Player ravager = (Player)event.getDamager();

            // Check if they have a spellbook in their hand.
            if(Spellbook.isSpellbook(ravager.getItemInHand(), plugin)){

                // See if the display name matches the name of a spell.
                String displayName = ravager.getItemInHand().getItemMeta().getDisplayName();

                if(Spellbook.spellExists(displayName, plugin)){

                    Class spellClass = Spellbook.getSpellClass(displayName, plugin);

                    try{
                        Constructor constructor = spellClass.getConstructor(new Class[]{EntityDamageByEntityEvent.class, MineCraftSpells.class});
                        Object obj = constructor.newInstance(event, plugin);
                        Method method = obj.getClass().getMethod("cast", String.class);
                        method.invoke(obj, displayName);
                    }
                    catch(NoSuchMethodException e){
                        // Do nothing, it just means we're casting a spell, not a touch spell.
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }

                //Cancel all the events of this event so we can set them ourselves.
                event.setCancelled(true);
                event.setDamage(0);
            }
        }
    }
}
