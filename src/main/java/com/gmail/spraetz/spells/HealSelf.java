package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/19/14.
 */
public class HealSelf extends Spell{

    public HealSelf(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent event, String spellName) {

        Player player = event.getPlayer();

        Double maxHealth = player.getMaxHealth();
        Double currentHealth = player.getHealth();
        Double newHealth = currentHealth +
                (maxHealth * .01 * plugin.getConfig().getInt("spells." + spellName + ".settings.percent_of_max_health"));

        if(newHealth > maxHealth){
            player.setHealth(maxHealth);
        }
        else{
            player.setHealth(newHealth);
        }
    }
}
