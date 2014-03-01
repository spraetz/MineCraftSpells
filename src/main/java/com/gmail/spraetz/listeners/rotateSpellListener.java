package com.gmail.spraetz.listeners;

import com.gmail.spraetz.plugin.MineCraftSpells;
import com.gmail.spraetz.spells.Spellbook;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 3/1/14.
 */
public class RotateSpellListener implements Listener {
    MineCraftSpells plugin;

    public RotateSpellListener(MineCraftSpells plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void rotateSpells(PlayerInteractEvent event) {

        if(event.getPlayer().getItemInHand().getType() == Material.BOOK &&
                (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)){

            if(Spellbook.isSpellbook(event.getPlayer().getItemInHand(), plugin)){
                Spellbook.rotateSpells(event.getPlayer().getItemInHand());
                event.getPlayer().sendMessage("Now casting " + event.getPlayer().getItemInHand().getItemMeta().getDisplayName());
            }
        }
    }
}
