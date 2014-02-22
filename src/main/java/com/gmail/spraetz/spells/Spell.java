package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;

/**
 * Created by spraetz on 2/16/14.
 */
public abstract class Spell {

    Engine plugin;
    PlayerEvent event;
    Player player;

    public Spell(PlayerEvent event, Engine plugin){
        this.player = event.getPlayer();
        this.plugin = plugin;
        this.event = event;
    }

    public static ItemStack[] getReagents(String spellName, Engine plugin){
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

    public boolean cast(){

        PlayerInteractEvent e = (PlayerInteractEvent)event;

        // Check if there are charges on the spellbook
        Integer charges = Spellbook.getCharges(event.getPlayer().getItemInHand());

        // Remove one charge
        if(charges == 0){
            player.sendMessage("Your spellbook is out of charges!");
            return false;
        }

        Spellbook.setCharges(event.getPlayer().getItemInHand(), charges - 1);

        // Cause spell effects
        spellEffects(event);

        return true;
    }

    public abstract void spellEffects(PlayerEvent event);

    public void addMetadata(Entity object, Player caster, Engine plugin){
        object.setMetadata("isSpell", new FixedMetadataValue(plugin, true));
        object.setMetadata("caster", new FixedMetadataValue(plugin, caster.getUniqueId()));
    }

    public static Class getSpellClass(String spellName, Engine plugin){
        try {
            return Class.forName("com.gmail.spraetz.spells." + plugin.getConfig().getString("spells." + spellName + ".class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch(NullPointerException npe){
            npe.printStackTrace();
        }
        return null;
    }

    public static boolean spellExists(String spellName, Engine plugin){
        return plugin.getConfig().getString("spells." + spellName + ".class", null) != null;
    }
}
