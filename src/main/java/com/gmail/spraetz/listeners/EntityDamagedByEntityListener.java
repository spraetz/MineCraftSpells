package com.gmail.spraetz.listeners;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by spraetz on 3/16/14.
 */
public class EntityDamagedByEntityListener implements Listener {

    MineCraftSpells plugin;

    public EntityDamagedByEntityListener(MineCraftSpells plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void damagedBySpell(EntityDamageByEntityEvent event) {

        Entity damager = event.getDamager();

        if(damager.getMetadata("isSpell") != null && damager.getMetadata("isSpell").size() > 0
                && damager.getMetadata("isSpell").get(0).asBoolean()){

            //Get the spell that caused the damage.
            String spellName = damager.getMetadata("spellName").get(0).asString();

            //Find the damage multiplier and modify damage
            Double multiplier = plugin.getConfig().getDouble("spells." + spellName + ".settings.damage_multiplier", -1);

            if(multiplier != -1){
                event.setDamage(event.getDamage() * multiplier);
            }
        }

    }
}
