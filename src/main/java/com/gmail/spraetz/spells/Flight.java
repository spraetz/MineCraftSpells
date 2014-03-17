package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by spraetz on 3/15/14.
 */
public class Flight extends Spell {

    public Flight(PlayerInteractEvent event, MineCraftSpells plugin) {
        super(event, plugin);
    }

    @Override
    public Boolean spellEffects(PlayerInteractEvent event, String spellName) {

        //Check if the player is already flying
        if(player.getAllowFlight()){
            player.sendMessage("You can already fly!");
            return false;
        }

        //Let the player fly!
        player.setAllowFlight(true);
        player.sendMessage("You feel light like a feather!");

        //Schedule a job to cancel the ability to fly later
        FlightRemover flightRemover = new FlightRemover(player, spellName, plugin);
        flightRemover.remove();

        //Start the flying effects.
        FlightEffect flightEffect = new FlightEffect(player, spellName, plugin);
        flightEffect.showEffect();

        return true;
    }

    private class FlightRemover extends BukkitRunnable {

        private final MineCraftSpells plugin;
        private final Player player;
        private final String spellName;

        FlightRemover(Player player, String spellName, MineCraftSpells plugin){
            this.plugin = plugin;
            this.player = player;
            this.spellName = spellName;
        }

        public void remove(){
            //Generate a random number between min and max lifetime
            Integer ticks = getSetting(spellName, "ticks", Integer.class);

            plugin.getServer().getScheduler().runTaskLater(plugin, this, ticks);
        }

        @Override
        public void run() {
            //Remove flight from the player
            player.setAllowFlight(false);
            player.sendMessage("Uh-oh, you feel heavy again.");
        }
    }

    private class FlightEffect extends BukkitRunnable {

        private final MineCraftSpells plugin;
        private final Player player;
        private final String spellName;

        FlightEffect(Player player, String spellName, MineCraftSpells plugin){
            this.plugin = plugin;
            this.player = player;
            this.spellName = spellName;
        }

        public void showEffect(){
            if(player.getAllowFlight()){
                plugin.getEffects().playVisual(plugin, player.getLocation(), "cloud", .2F, .2F, .1F, 20, 0F);
                plugin.getServer().getScheduler().runTaskLater(plugin, this, 10);
            }
        }

        @Override
        public void run() {
            FlightEffect newEffect = new FlightEffect(player, spellName, plugin);
            newEffect.showEffect();
        }
    }
}
