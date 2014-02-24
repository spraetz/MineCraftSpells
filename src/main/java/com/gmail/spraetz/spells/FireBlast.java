package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Fireball;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/16/14.
 */
public class FireBlast extends Spell {

    public FireBlast(PlayerInteractEvent event, MineCraftSpells plugin){
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent event, String spellName) {

        //Spawn a new fireball at the next tick at the player's current location.
        Fireball fireblast = player.getWorld().spawn(player.getEyeLocation(), Fireball.class);

        //Fireblasts set stuff on fire.
        fireblast.setIsIncendiary(true);

        //Set the radius (yield)
        fireblast.setYield(plugin.getConfig().getInt("spells." + spellName + ".settings.radius"));

        //Set the shooter to be the current player.
        fireblast.setShooter(player);

        //Add metadata to the fireball so we know what it is and where it is going.
        addMetadata(fireblast, player, plugin);
    }
}
