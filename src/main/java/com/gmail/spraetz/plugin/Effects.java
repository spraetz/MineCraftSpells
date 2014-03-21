package com.gmail.spraetz.plugin;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerNamedSoundEffect;
import com.comphenix.packetwrapper.WrapperPlayServerWorldParticles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Created by spraetz on 3/15/14.
 */
public class Effects {

    boolean useEffects = true;
    final MineCraftSpells plugin;

    public Effects(MineCraftSpells plugin){
        this.plugin = plugin;

        try{
            Class.forName("com.comphenix.protocol.ProtocolLibrary");
        }
        catch(Exception e){
            plugin.getLogger().warning("***************************************************************************");
            plugin.getLogger().warning("*** Could not find ProtocolLib, your spells won't have special effects! ***");
            plugin.getLogger().warning("***************************************************************************");
            useEffects = false;
        }
    }

    public static Integer BROADCAST_RADIUS = 32;

    public void playVisual(MineCraftSpells plugin, Location location, String name, float spreadHoriz, float spreadVert,
                           float speed, int count, float yOffset) {

        if(!useEffects){
            return;
        }

        WrapperPlayServerWorldParticles packet = new WrapperPlayServerWorldParticles();
        packet.setParticleEffect(WrapperPlayServerWorldParticles.ParticleEffect.fromName(name));
        packet.setLocation(location.add(0f, yOffset, 0f));
        packet.setNumberOfParticles(count);
        packet.setParticleSpeed(speed);
        packet.setOffset(new Vector(spreadHoriz, spreadVert, spreadHoriz));

        broadcastPacket(packet, plugin, location);
    }

    public void playSound(MineCraftSpells plugin, Location location, String name) {

        if(!useEffects){
            return;
        }

        WrapperPlayServerNamedSoundEffect packet = new WrapperPlayServerNamedSoundEffect();
        packet.setSoundName(name);
        packet.setVolume(1.0F);
        packet.setPitch(1.0F);
        packet.setEffectPositionX(location.getX());
        packet.setEffectPositionY(location.getY());
        packet.setEffectPositionZ(location.getZ());

        broadcastPacket(packet, plugin, location);
    }

    public void broadcastPacket(AbstractPacket packet, MineCraftSpells plugin, Location location){

        //Find the players to send the packet to
        for(Player p : plugin.getServer().getOnlinePlayers()){
            if(p.getLocation().distanceSquared(location) <= BROADCAST_RADIUS*BROADCAST_RADIUS){
                packet.sendPacket(p);
            }
        }
    }
}
