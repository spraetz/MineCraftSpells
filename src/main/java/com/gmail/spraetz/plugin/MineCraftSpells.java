package com.gmail.spraetz.plugin;

import com.gmail.spraetz.commands.ChargeSpellbook;
import com.gmail.spraetz.listeners.CastSpellListener;
import com.gmail.spraetz.listeners.CastTouchSpellListener;
import com.gmail.spraetz.listeners.RotateSpellListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by spraetz on 2/16/14.
 */
public class MineCraftSpells extends JavaPlugin{

    private Analytics analytics;

    public Analytics getAnalytics(){
        return analytics;
    }

    public void setAnalytics(Analytics a){
        this.analytics = a;
    }

    @Override
    public void onEnable() {
        //Set up the config
        setupConfig();

        //Set up tracking
        setAnalytics(new Analytics(this));

        //Register commands
        registerCommands();

        //Register Event Listeners
        registerEventListeners();
    }

    public void registerCommands(){
        getCommand("charge").setExecutor(new ChargeSpellbook(this));
    }

    public void registerEventListeners(){
        this.getServer().getPluginManager().registerEvents(new CastSpellListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RotateSpellListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CastTouchSpellListener(this), this);
    }

    public void setupConfig(){
        //Look for a config.yml file in the plugins folder.
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }
}
