package com.gmail.spraetz.plugin;

import io.keen.client.java.KeenClient;
import io.keen.client.java.exceptions.KeenException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spraetz on 2/19/14.
 */
public class Analytics {

    private final KeenClient client;
    private final MineCraftSpells plugin;

    public Analytics(MineCraftSpells plugin){
        this.plugin = plugin;
        Boolean can_track = this.plugin.getConfig().getBoolean("allow_tracking");
        String projectId = "530a932c36bf5a2d23000000";
        String writeKey = "ed91795fd2a7e88c5de09a31c982edf3c242b90cbdce6f5e375ec3200d1ee75d0efeba6597a277d51579dde3a61ceafc2a9e902925c18f6d9a410c4db78f388dcc2518f4ba48f9789ae5fea7f0ba2cce8f3b8999a8965d7f7148383d0184a497be09369d8cb6da315e3cd2ad01cc93fb";

        if(can_track){
            KeenClient.initialize(projectId, writeKey, null);
            client = KeenClient.client();
            plugin.getLogger().info("Logging analytics data to Keen IO Project: " + projectId);
        }
        else{
            System.out.println("what");
            client = null;
        }
    }

    public void trackSpellCast(Player p, String spellName){
        HashMap<String, Object> properties = new HashMap<String, Object>();

        properties.put("player", getPlayerProperties(p));

        properties.put("spell", getSpellProperties(spellName));

        track("spell_casts", properties);
    }

    public void trackCharge(Player player, String spellName, Integer chargesToAdd){
        trackCharge(player, spellName, chargesToAdd, true, null);
    }

    public void trackCharge(Player player, String spellName, Integer chargesToAdd, Boolean success, String error){
        HashMap<String, Object> properties = new HashMap<String, Object>();

        properties.put("player", getPlayerProperties(player));

        if(success){
            properties.put("spell", getSpellProperties(spellName));
            properties.put("charges", chargesToAdd);
        }

        properties.put("success", success);

        if(!success){
            properties.put("error_message", error);
        }

        track("charge_spellbook", properties);
    }

    private void track(String eventName, Map<String, Object> properties){
        if(client != null){
            try {
                properties.put("server_id", plugin.getServer().getServerId());
                client.addEvent(eventName, properties);
            } catch (KeenException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, Object> getPlayerProperties(Player player){
        //Build player HashMap
        HashMap<String, Object> playerProperties = new HashMap<String, Object>();
        playerProperties.put("name", player.getName().toLowerCase());

        return playerProperties;
    }

    public HashMap<String, Object> getSpellProperties(String spellName){

        HashMap<String, Object> spellProperties = new HashMap<String, Object>();
        spellProperties.put("name", spellName.toLowerCase());
        spellProperties.put("class", plugin.getConfig().getString("spells." + spellName + ".class"));

        return spellProperties;
    }
}
