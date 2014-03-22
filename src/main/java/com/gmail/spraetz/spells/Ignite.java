package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Damageable;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by spraetz on 3/20/14.
 */
public class Ignite extends TouchSpell {

    public Ignite(EntityDamageByEntityEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(EntityDamageByEntityEvent event, String spellName) {

        if(event.getEntity() instanceof Damageable){

            //Set the target on fire.
            event.getEntity().setFireTicks(getSetting(spellName, "ticks", Integer.class));

            return true;
        }
        else{
            return false;
        }
    }
}
