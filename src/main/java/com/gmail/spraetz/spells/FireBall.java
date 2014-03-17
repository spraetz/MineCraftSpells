package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

/**
 * Created by spraetz on 2/16/14.
 */
public class FireBall extends Spell {

    public FireBall(PlayerInteractEvent event, MineCraftSpells plugin){
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(PlayerInteractEvent event, String spellName) {

        //Spawn a new fireball at the next tick at the player's current location.
        Fireball fireball = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);

        //fireballs set stuff on fire.
        fireball.setIsIncendiary(true);

        //Set the radius (yield)
        fireball.setYield(getSetting(spellName, "radius", Integer.class));

        //Set the shooter to be the current player.
        fireball.setShooter(player);

        //Add metadata to the fireball so we know what it is and where it is going.
        addMetadata(fireball, player, spellName, plugin);

        //Play effects
        plugin.getEffects().playSound(plugin, player.getLocation(), "mob.irongolem.hit");
        plugin.getEffects().playVisual(plugin, player.getLocation(), "largesmoke", .2F, .2F, .1F, 20, 2F);

        return true;
    }

    //TODO: Move this to Spell.java and add a new constructor and make instance method
    public static void onImpact(Entity entity, String spellName, MineCraftSpells plugin){

        //Get some settings.
        //TODO: Once this becomes an instance method, use getSettings
        Integer igniteRadius = plugin.getConfig().getInt("spells." + spellName + ".settings.ignite_radius");
        Integer fireTicks = plugin.getConfig().getInt("spells." + spellName + ".settings.fire_ticks");

        //Light nearby entites on fire.
        ArrayList<Entity> entities = (ArrayList<Entity>)entity.getNearbyEntities(igniteRadius, igniteRadius, igniteRadius);
        for(Entity e : entities){
            e.setFireTicks(fireTicks);
        }

        //Show effects.
        plugin.getEffects().playVisual(plugin, entity.getLocation(), "lava", 1F, 1F, .1F, 20, 0F);
        plugin.getEffects().playVisual(plugin, entity.getLocation(), "flame", 1F, 1F, .1F, 20, 0F);
    }
}
