package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by spraetz on 2/16/14.
 */
public class Teleport extends Spell {

    public Teleport(PlayerInteractEvent event, MineCraftSpells plugin){
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent e, String spellName) {

        // Get the block the player is currently targeting.
        // Add 1 in the Y direction so they don't sink into the ground.
        Location toLocation = player.getTargetBlock(null, 20).getLocation().add(0, 1, 0);

        Location currentLocation = player.getLocation();

        // If the player is teleport into a solid block, step them one block back towards where they came from.
        if(toLocation.getBlock().getType() != Material.AIR){
            //Triangle math =(
            if(toLocation.getBlockX() > currentLocation.getBlockX() ){
                toLocation.add(-1, 0, 0);
            }
            else{
                toLocation.add(1, 0, 0);
            }
            if(toLocation.getBlockZ() > currentLocation.getBlockZ() ){
                toLocation.add(0, 0, -1);
            }
            else{
                toLocation.add(0, 0, 1);
            }
        }

        // Set the Yaw to be the player's current yaw to maintain the direction the player is facing.
        toLocation.setYaw(player.getLocation().getYaw());

        // Perform the teleport.
        player.teleport(toLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);

        // Make a lightning clap to sound badass.
        player.getWorld().strikeLightningEffect(player.getTargetBlock(null, 20).getLocation().add(0, 1, 0));
    }
}
