package com.gmail.spraetz.listeners;

import com.gmail.spraetz.spells.*;
import com.gmail.spraetz.plugin.Engine;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by spraetz on 2/16/14.
 */
public class CastSpellListener implements Listener {

    Engine plugin;

    public CastSpellListener(Engine plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void castSpell(PlayerInteractEvent event) {

        // Check if they have a spellbook in their hand.
        if(event.getPlayer().getItemInHand().getType() == Material.BOOK && event.getAction() == Action.RIGHT_CLICK_AIR){

            // See if the display name matches the name of a spell.
            String displayName = event.getPlayer().getItemInHand().getItemMeta().getDisplayName();

            if(Spell.spellExists(displayName, plugin)){

                Class spellClass = Spell.getSpellClass(displayName, plugin);
                try{
                    Constructor constructor = spellClass.getConstructor(new Class[]{PlayerInteractEvent.class, Engine.class});
                    Object obj = constructor.newInstance(event, plugin);
                    Method method = obj.getClass().getMethod("cast");
                    method.invoke(obj);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
