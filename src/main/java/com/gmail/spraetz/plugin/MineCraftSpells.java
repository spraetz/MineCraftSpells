package com.gmail.spraetz.plugin;

import com.gmail.spraetz.commands.*;
import com.gmail.spraetz.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Created by spraetz on 2/16/14.
 */
public class MineCraftSpells extends JavaPlugin{

    private Effects effects;
    public Effects getEffects(){
        return effects;
    }
    public void setEffects(Effects e){
        this.effects = e;
    }
    private Analytics analytics;
    public Analytics getAnalytics(){
        return analytics;
    }
    public void setAnalytics(Analytics a){
        this.analytics = a;
    }

    @Override
    public void onEnable() {

        if (this.getConfig().getBoolean("auto-update"))
        {
            @SuppressWarnings("unused")
            Updater updater = new Updater(this, 75077, this.getFile(), Updater.UpdateType.DEFAULT, true);
        }

        //Set up the config
        setupConfig();

        //Set up tracking
        setAnalytics(new Analytics(this));

        //Register commands
        registerCommands();

        //Register Event Listeners
        registerEventListeners();

        //Log the enable
        analytics.trackPluginEnable();
    }

    @Override
    public void onLoad(){
        setEffects(new Effects(this));
    }

    public void registerCommands(){
        getCommand("charge").setExecutor(new ChargeSpellbook(this));
        getCommand("spells").setExecutor(new SpellHelp(this));
        //getCommand("particle").setExecutor(new ParticleTest(this));
        //getCommand("sound").setExecutor(new SoundTest(this));
        //getCommand("launch").setExecutor(new LaunchTest(this));
    }

    public void registerEventListeners(){
        this.getServer().getPluginManager().registerEvents(new CastSpellListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RotateSpellListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CastTouchSpellListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ExplosionListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityDamagedByEntityListener(this), this);
    }

    public void setupConfig(){
        //Look for a config.yml file in the plugins folder.
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();


        if(getConfig().getString("guid") == null){
            getConfig().set("guid", UUID.randomUUID().toString());
            saveConfig();
        }
    }
}
