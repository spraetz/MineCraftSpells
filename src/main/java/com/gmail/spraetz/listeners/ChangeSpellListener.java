package com.gmail.spraetz.listeners;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 3/1/14.
 */
public class ChangeSpellListener implements Listener {
    MineCraftSpells plugin;

    public ChangeSpellListener(MineCraftSpells plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void changeSpell(PlayerInteractEvent event) {


    }
}
