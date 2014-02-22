package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.Engine;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/16/14.
 */
public class LightningBolt extends Spell {

    public LightningBolt(PlayerInteractEvent event, Engine plugin){
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent event) {

        Location strikeLocation = player.getTargetBlock(null, 20).getLocation();

        player.getWorld().strikeLightning(strikeLocation);

        //TODO: This doesn't work for some reason?
        player.getWorld().playEffect(strikeLocation, Effect.SMOKE, 4, 20);
    }
}
