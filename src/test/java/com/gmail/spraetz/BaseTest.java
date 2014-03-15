package com.gmail.spraetz;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    protected World world;
    protected ItemStack spellbook;
    protected Player player;
    protected ItemMeta spellbookMeta;

    @BeforeClass
    public static void testClassSetup(){
        File configFile = new File(new File("").getAbsolutePath() + "/src/main/resources/config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        MockGateway.MOCK_STANDARD_METHODS = false;
    }

    @Before
    public void testSetup(){
        plugin = PowerMockito.mock(MineCraftSpells.class);
        when(plugin.getConfig()).thenReturn(config);

        world = PowerMockito.mock(World.class);
        player = PowerMockito.mock(Player.class);
        spellbook = PowerMockito.mock(ItemStack.class);
        spellbookMeta = PowerMockito.mock(ItemMeta.class);
        when(spellbook.getItemMeta()).thenReturn(spellbookMeta);
        when(spellbook.getType()).thenReturn(Material.BOOK);
    }
}
