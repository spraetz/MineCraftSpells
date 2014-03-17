package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.WitherSkull;
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
        WitherSkull explosion = player.getWorld().spawn(player.getEyeLocation(), WitherSkull.class);

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
    public static void onImpact(Entity entity, String spellName, MineCraftSpells plugin){
        plugin.getEffects().playVisual(plugin, entity.getLocation(), "largesmoke", 1F, 1F, .1F, 20, 0F);
    }
}
