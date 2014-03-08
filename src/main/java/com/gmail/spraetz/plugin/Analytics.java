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
        Boolean can_track = this.plugin.getConfig().getBoolean("keen.allow_tracking");
        String projectId = this.plugin.getConfig().getString("keen.project_id");
        String writeKey = this.plugin.getConfig().getString("keen.write_key");


        if(can_track && projectId != null && writeKey != null){
            KeenClient.initialize(projectId, writeKey, null);
            client = KeenClient.client();
            plugin.getLogger().info("Logging analytics data to Keen IO Project: " + projectId);
        }
        else{
            client = null;
        }
    }

    public void trackSpellCast(Player p, String spellName){
        HashMap<String, Object> properties = new HashMap<String, Object>();

        //Build player HashMap
        HashMap<String, Object> playerProperties = new HashMap<String, Object>();
        playerProperties.put("name", p.getName().toLowerCase());

        properties.put("player", playerProperties);

        //Build spell HashMap
        HashMap<String, Object> spellProperties = new HashMap<String, Object>();
        spellProperties.put("name", spellName.toLowerCase());
        spellProperties.put("class", plugin.getConfig().getString("spells." + spellName + ".class"));

        properties.put("spell", spellProperties);

        track("spell_casts", properties);
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
}
