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

    public static ItemStack[] getReagents(String spellName, MineCraftSpells plugin){
        String settingName = "spells." + spellName + ".reagents";

        List reagents = plugin.getConfig().getList(settingName);
        ItemStack[] items = new ItemStack[reagents.size()];

        for(int i = 0; i < reagents.size(); i++){
            HashMap<String, Object> reagent = (HashMap<String, Object>)reagents.get(i);
            items[i] = new ItemStack(
                    Material.valueOf(reagent.get("reagent").toString()),
                    (Integer)reagent.get("quantity")
            );
        }

        return items;
    }

    public boolean cast(String spellName){

        PlayerInteractEvent e = (PlayerInteractEvent)event;

        // Check if there are charges on the spellbook
        Integer charges = Spellbook.getCharges(event.getPlayer().getItemInHand(), spellName);

        // See if we have charges.
        if(charges == 0){
            player.sendMessage("Your spellbook is out of charges for " + spellName + "!");
            return false;
        }

        // Cause spell effects
        spellEffects(event, spellName);

        // Remove a charge
        Spellbook.setCharges(event.getPlayer().getItemInHand(), spellName, charges - 1);

        // Record it!
        plugin.analytics.trackSpellCast(e.getPlayer(), spellName);

        return true;
    }

    public abstract void spellEffects(PlayerEvent event, String spellName);

    public void addMetadata(Entity object, Player caster, MineCraftSpells plugin){
        object.setMetadata("isSpell", new FixedMetadataValue(plugin, true));
        object.setMetadata("caster", new FixedMetadataValue(plugin, caster.getUniqueId()));
    }

    public static Class getSpellClass(String spellName, MineCraftSpells plugin){
        try {
            return Class.forName(plugin.getConfig().getString("spells." + spellName + ".class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(NullPointerException npe){
            npe.printStackTrace();
        }
        return null;
    }

    public static boolean spellExists(String spellName, MineCraftSpells plugin){
        return plugin.getConfig().getString("spells." + spellName + ".class", null) != null;
    }
}
