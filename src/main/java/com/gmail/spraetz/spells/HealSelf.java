package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/19/14.
 */
public class HealSelf extends Spell{

    public HealSelf(PlayerInteractEvent event, Engine plugin) {
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent event) {

        Player player = event.getPlayer();

        Double maxHealth = player.getMaxHealth();
        Double currentHealth = player.getHealth();
        Double newHealth = currentHealth + maxHealth * .3;

        if(newHealth > maxHealth){
            player.setHealth(maxHealth);
        }
        else{
            player.setHealth(newHealth);
        }
    }
}
