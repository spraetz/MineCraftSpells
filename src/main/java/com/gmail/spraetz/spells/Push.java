package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

/**
 * Created by spraetz on 3/15/14.
 */
public class Push extends TouchSpell {

    public Push(EntityDamageByEntityEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(EntityDamageByEntityEvent event, String spellName) {

        //Get the entity we're pushing.
        Entity attacked = event.getEntity();

        if(attacked instanceof LivingEntity){
            Double knockback = getSetting(spellName, "knockback", Double.class);
            Double upForce = getSetting(spellName, "up_force", Double.class);
            Double maxUpForce = getSetting(spellName, "max_up_force", Double.class);

            attacked.setVelocity(attacked.getVelocity().add(
                    attacked.getLocation().toVector().subtract(
                            player.getLocation().toVector()).normalize().multiply(knockback)
                )
            );

            //Tip the player's vector up a bit.
            attacked.setVelocity(attacked.getVelocity().add(new Vector(0, upForce, 0)));

            //Make sure we don't go over the max upforce
            if(attacked.getVelocity().getY() > maxUpForce){
                attacked.setVelocity(attacked.getVelocity().setY(maxUpForce));
            }

            plugin.getEffects().playSound(plugin, attacked.getLocation(), "mob.zombie.metal");
            plugin.getEffects().playVisual(plugin, attacked.getLocation(), "explode", .2F, .2F, .1F, 20, 1F);
            return true;
        }
        else{
            return false;
        }
    }
}
