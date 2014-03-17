package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 3/15/14.
 */
public class Recall extends Spell {

    public Recall(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(PlayerInteractEvent event, String spellName) {

        //Find the player's bed spawn location
        Location bedSpawn = player.getBedSpawnLocation();

        //If that is null, it's because the player doesn't have a bed spawn yet (or an invalid one)
        if(bedSpawn == null){
            bedSpawn = player.getWorld().getSpawnLocation();
        }

        // Set the Yaw to be the player's current yaw to maintain the direction the player is facing.
        bedSpawn.setYaw(player.getLocation().getYaw());
        bedSpawn.setPitch(player.getLocation().getPitch());

        //Move the player.
        player.teleport(bedSpawn);

        return true;
    }
}
