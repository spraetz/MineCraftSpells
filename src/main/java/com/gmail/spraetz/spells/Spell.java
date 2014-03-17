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
    PlayerInteractEvent event;
    Player player;

    public Spell(PlayerInteractEvent event, MineCraftSpells plugin){
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
        Boolean success = spellEffects(event, spellName);

        if(success){
            // Remove a charge
            Spellbook.setCharges(player.getItemInHand(), spellName, charges - 1);

            // Record it!
            plugin.getAnalytics().trackSpellCast(player, spellName);
        }

        return true;
    }

    public abstract Boolean spellEffects(PlayerInteractEvent event, String spellName);

    public void addMetadata(Entity object, Player caster, String spellName, MineCraftSpells plugin){
        object.setMetadata("isSpell", new FixedMetadataValue(plugin, true));
        object.setMetadata("spellName", new FixedMetadataValue(plugin, spellName));
        object.setMetadata("caster", new FixedMetadataValue(plugin, caster.getUniqueId()));
    }

    public <T> T getSetting(String spellName, String setting, Class<T> as){
        Object result = plugin.getConfig().get("spells." + spellName + ".settings." + setting);
        return as.cast(result);
    }
}
