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
    private final Engine plugin;

    public Analytics(Engine plugin){
        this.plugin = plugin;
        String projectId = this.plugin.getConfig().getString("keen.project_id");
        String writeKey = this.plugin.getConfig().getString("keen.write_key");


        if( projectId != null && writeKey != null){
            KeenClient.initialize(projectId, writeKey, null);
            client = KeenClient.client();
            plugin.getLogger().info("Logging analytics data to Keen IO Project: " + projectId);
        }
        else{
            client = null;
        }
    }

    public void trackLogin(Player p){
        HashMap<String, Object> properties = new HashMap<String, Object>();

        //Build player HashMap
        HashMap<String, Object> playerProperties = new HashMap<String, Object>();
        playerProperties.put("name", p.getName().toLowerCase());

        properties.put("player", playerProperties);

        track("logins", properties);
    }

    private void track(String eventName, Map<String, Object> properties){
        if(client != null){
            try {
                client.addEvent(eventName, properties);
            } catch (KeenException e) {
                e.printStackTrace();
            }
        }
    }
}
