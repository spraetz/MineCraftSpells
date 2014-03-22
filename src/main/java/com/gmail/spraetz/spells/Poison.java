package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by spraetz on 3/21/14.
 */
public class Poison extends TouchSpell {

    public Poison(EntityDamageByEntityEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(EntityDamageByEntityEvent event, String spellName) {

        if(event.getEntity() instanceof LivingEntity){
            LivingEntity victim = (LivingEntity)event.getEntity();

            Integer duration = getSetting(spellName, "ticks", Integer.class);
            Integer amplifier = getSetting(spellName, "amplifier", Integer.class);
            victim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, duration, amplifier, true));

            plugin.getEffects().playSound(plugin, event.getEntity().getLocation(), "step.gravel");
            plugin.getEffects().playVisual(plugin, event.getEntity().getLocation(), "slime", .2F, .2F, .1F, 20, 2F);

            return true;
        }
        else{
            return false;
        }
    }
}
