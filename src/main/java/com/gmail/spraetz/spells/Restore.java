package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by spraetz on 3/27/14.
 */
public class Restore extends Spell{


    public Restore(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(PlayerInteractEvent event, String spellName) {

        //Remove a bunch of bad statuses
        event.getPlayer().removePotionEffect(PotionEffectType.POISON);
        event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
        event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
        event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
        event.getPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
        event.getPlayer().removePotionEffect(PotionEffectType.WITHER);
        event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        event.getPlayer().setFireTicks(0);

        return true;
    }
}
