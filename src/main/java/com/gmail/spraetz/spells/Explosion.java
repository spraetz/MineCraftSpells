package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/16/14.
 */
public class Explosion extends Spell{

    public Explosion(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(PlayerInteractEvent e, String spellName){

        //Spawn a new fireball at the next tick at the player's current location.
        Fireball explosion = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);

        //Explosions have no fire.
        explosion.setIsIncendiary(false);

        //Set the radius (yield)
        explosion.setYield(getSetting(spellName, "radius", Integer.class));

        //Set the shooter to be the current player.
        explosion.setShooter(player);

        //Add metadata to the fireball so we know what it is and where it is going.
        addMetadata(explosion, player, spellName, plugin);

        return true;
    }

    //TODO: Move this to Spell.java and add a new constructor and make instance method
    public static void onImpact(Location location, String spellName, MineCraftSpells plugin){
        plugin.getEffects().playVisual(plugin, location, "largesmoke", 1F, 1F, .1F, 20, 0F);
    }
}
