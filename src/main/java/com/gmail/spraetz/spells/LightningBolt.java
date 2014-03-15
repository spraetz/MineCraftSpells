package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by spraetz on 2/16/14.
 */
public class LightningBolt extends Spell {

    public LightningBolt(PlayerInteractEvent event, MineCraftSpells plugin){
        super(event, plugin);
    }

    @Override
    public void spellEffects(PlayerEvent event, String spellName) {

        Location strikeLocation = player.getTargetBlock(null, 20).getLocation();

        player.getWorld().strikeLightning(strikeLocation);

        player.getWorld().playEffect(strikeLocation, Effect.SMOKE, 4, 20);
    }
}
