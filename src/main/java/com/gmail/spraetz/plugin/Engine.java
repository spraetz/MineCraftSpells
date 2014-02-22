package com.gmail.spraetz.plugin;

import com.gmail.spraetz.commands.ChargeSpellbook;
import com.gmail.spraetz.listeners.CastSpellListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by spraetz on 2/16/14.
 */
public class Engine extends JavaPlugin{

    public Analytics analytics;

    @Override
    public void onEnable() {
        getLogger().info("Enabling MineCraftSpells");

        //Set up the config
        setupConfig();

        //Set up tracking
        analytics = setupAnalytics();

        //Register commands
        registerCommands();

        //Register Event Listeners
        registerEventListeners();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling MineCraftSpells");
    }

    public void registerCommands(){
        getCommand("charge").setExecutor(new ChargeSpellbook(this));
    }

    public void registerEventListeners(){
        new CastSpellListener(this);
    }

    public void setupConfig(){
        //Look for a config.yml file in the plugins folder.
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    public Analytics setupAnalytics() {
        return new Analytics(this);
    }
}
