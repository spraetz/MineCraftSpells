package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 3/1/14.
 */
public abstract class TouchSpell {

    MineCraftSpells plugin;
    EntityDamageByEntityEvent event;
    Player player;

    public TouchSpell(EntityDamageByEntityEvent event, MineCraftSpells plugin){
        this.player = (Player)event.getDamager();
        this.plugin = plugin;
        this.event = event;
    }

    public boolean cast(String spellName){

        // Check if there are charges on the spellbook
        Integer charges = Spellbook.getCharges(player.getItemInHand(), spellName);

        // See if we have charges.
        if(charges == 0){
            player.sendMessage("Your spellbook is out of charges for " + spellName + "!");
            return false;
        }

        // Cause spell effects
        spellEffects(event, spellName);

        // Remove a charge
        Spellbook.setCharges(player.getItemInHand(), spellName, charges - 1);

        // Record it!
        plugin.getAnalytics().trackSpellCast(player, spellName);

        return true;
    }

    public <T> T getSetting(String spellName, String setting, Class<T> as){
        Object result = plugin.getConfig().get("spells." + spellName + ".settings." + setting);
        return as.cast(result);
    }

    public abstract Boolean spellEffects(EntityDamageByEntityEvent event, String spellName);
}
