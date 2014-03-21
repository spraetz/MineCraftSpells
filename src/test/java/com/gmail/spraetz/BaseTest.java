package com.gmail.spraetz;

import com.gmail.spraetz.plugin.Effects;
import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Before;
import org.junit.BeforeClass;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;

import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by spraetz on 3/15/14.
 */
@PrepareForTest({MineCraftSpells.class})
public abstract class BaseTest {

    protected static FileConfiguration config;
    protected MineCraftSpells plugin;
    protected Effects effects;
    protected World world;
    protected ItemStack spellbook;
    protected Player player;
    protected ItemMeta spellbookMeta;
    protected Server server;
    protected BukkitScheduler scheduler;
    protected PlayerInventory playerInventory;

    @BeforeClass
    public static void testClassSetup(){
        File configFile = new File(new File("").getAbsolutePath() + "/src/main/resources/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        MockGateway.MOCK_STANDARD_METHODS = false;
    }

    @Before
    public void testSetup(){

        scheduler = PowerMockito.mock(BukkitScheduler.class);

        server = PowerMockito.mock(Server.class);
        when(server.getScheduler()).thenReturn(scheduler);

        effects = PowerMockito.mock(Effects.class);

        plugin = PowerMockito.mock(MineCraftSpells.class);
        when(plugin.getConfig()).thenReturn(config);
        when(plugin.getServer()).thenReturn(server);
        when(plugin.getEffects()).thenReturn(effects);

        world = PowerMockito.mock(World.class);
        spellbook = PowerMockito.mock(ItemStack.class);
        spellbookMeta = PowerMockito.mock(ItemMeta.class);
        player = PowerMockito.mock(Player.class);
        playerInventory = PowerMockito.mock(PlayerInventory.class);

        when(player.getItemInHand()).thenReturn(spellbook);
        when(player.getInventory()).thenReturn(playerInventory);
        when(spellbook.getItemMeta()).thenReturn(spellbookMeta);
        when(spellbook.getType()).thenReturn(Material.BOOK);
        when(spellbookMeta.getLore()).thenReturn(null);
        when(spellbookMeta.getDisplayName()).thenReturn("BOOK");

    }
}
