package com.gmail.spraetz.listeners;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by spraetz on 3/16/14.
 */
public class ExplosionListener implements Listener {

    private MineCraftSpells plugin;

    public ExplosionListener(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onExplosion(EntityExplodeEvent event) {

        //If this explosion was caused by a spell...
        if(event.getEntity().getMetadata("isSpell") != null &&
                event.getEntity().getMetadata("isSpell").size() > 0 &&
                event.getEntity().getMetadata("isSpell").get(0).asBoolean()){

            if(event.getEntity().getMetadata("spellName").size() > 0 && Spellbook.spellExists(event.getEntity().getMetadata("spellName").get(0).asString(), plugin)){

                String spellName = event.getEntity().getMetadata("spellName").get(0).asString();

                Class spellClass = Spellbook.getSpellClass(spellName, plugin);

                try {
                    Method method = spellClass.getMethod("onImpact", Location.class, String.class, MineCraftSpells.class);
                    method.invoke(null, event.getEntity().getLocation(), spellName, plugin);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
