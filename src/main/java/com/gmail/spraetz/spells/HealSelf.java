package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/19/14.
 */
public class HealSelf extends Spell{

    public HealSelf(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(PlayerInteractEvent event, String spellName) {

        Player player = event.getPlayer();

        Double maxHealth = player.getMaxHealth();
        Double currentHealth = player.getHealth();
        Double newHealth = currentHealth +
                (maxHealth * .01 * getSetting(spellName, "percent_of_max_health", Integer.class));

        if(newHealth > maxHealth){
            player.setHealth(maxHealth);
        }
        else{
            player.setHealth(newHealth);
        }

        plugin.getEffects().playVisual(plugin, player.getLocation(), "heart", .2F, .2F, .1F, 3, 2F);
        return true;
    }
}
