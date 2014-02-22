package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

/**
 * Created by spraetz on 2/16/14.
 */
public class Explosion extends Spell{

    public Explosion(PlayerInteractEvent event, Engine plugin) {
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent e){

        //Spawn a new fireball at the next tick at the player's current location.
        Fireball explosion = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);

        //Explosions have no fire.
        explosion.setIsIncendiary(false);

        //Explosions have a radius (yield) of 3.
        explosion.setYield(3);

        //Set the shooter to be the current player.
        explosion.setShooter(player);

        //Add metadata to the fireball so we know what it is and where it is going.
        addMetadata(explosion, player, plugin);
    }
}
