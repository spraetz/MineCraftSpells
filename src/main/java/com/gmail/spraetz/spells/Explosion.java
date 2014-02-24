package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/16/14.
 */
public class Explosion extends Spell{

    public Explosion(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent e, String spellName){

        //Spawn a new fireball at the next tick at the player's current location.
        Fireball explosion = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);

        //Explosions have no fire.
        explosion.setIsIncendiary(false);

        //Set the radius (yield)
        explosion.setYield(plugin.getConfig().getInt("spells." + spellName + ".settings.radius"));

        //Set the shooter to be the current player.
        explosion.setShooter(player);

        //Add metadata to the fireball so we know what it is and where it is going.
        addMetadata(explosion, player, plugin);
    }
}
