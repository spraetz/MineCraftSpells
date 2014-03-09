package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.List;

/**
 * Created by spraetz on 2/16/14.
 */
public abstract class Spell {

    MineCraftSpells plugin;
    PlayerEvent event;
    Player player;

    public Spell(PlayerEvent event, MineCraftSpells plugin){
        this.player = event.getPlayer();
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
        plugin.analytics.trackSpellCast(player, spellName);

        return true;
    }

    public abstract void spellEffects(PlayerEvent event, String spellName);

    public void addMetadata(Entity object, Player caster, MineCraftSpells plugin){
        object.setMetadata("isSpell", new FixedMetadataValue(plugin, true));
        object.setMetadata("caster", new FixedMetadataValue(plugin, caster.getUniqueId()));
    }

    public <T> T getSetting(String spellName, String setting, Class<T> as){
        Object result = plugin.getConfig().get("spells." + spellName + ".settings." + setting);
        return as.cast(result);
    }

}
