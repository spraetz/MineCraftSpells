package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.MineCraftSpells;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by spraetz on 2/17/14.
 */
public class Spellbook {

    /*

        Spellbooks hold charges in bukkit Lore slots.

        The spells are represented by strings in the Lore entry that look like this:
            spell_name: 23

     */

    public static Integer MAX_SPELL_CHARGES = 64;
    public static Integer MAX_NUMBER_OF_SPELLS_PER_BOOK = 3;
    public static String LORE_STRING_SEPARATOR = ": ";

    public static String chargeString(String spellName, Integer numberOfCharges){
        return spellName + ": " + numberOfCharges;
    }

    public static Integer getCharges(ItemStack spellbook, String spellName){
        ArrayList<String> loreList = (ArrayList<String>)spellbook.getItemMeta().getLore();

        if(loreList == null || loreList.size() == 0){
            return 0;
        }
        else{
            for(String lore : loreList) {
                String[] loreParts = lore.split(LORE_STRING_SEPARATOR);
                if (loreParts[0].equalsIgnoreCase(spellName)) {
                    return Integer.parseInt(loreParts[1]);
                }
            }
            return 0;
        }
    }

    public static void setCharges(ItemStack spellbook, String spellName, Integer numberOfCharges){
        ItemMeta itemMeta = spellbook.getItemMeta();
        ArrayList<String> loreList = (ArrayList<String>)itemMeta.getLore();

        //If lore is empty, easy to add.
        if(loreList == null || loreList.size() == 0){
            System.out.println("in if");
            loreList = new ArrayList<String>();
            loreList.add(chargeString(spellName, numberOfCharges));
            itemMeta.setDisplayName(spellName);
        }
        else{
            boolean found = false;
            //otherwise we need to iterate through it and see if we already have it.
            for(int i = 0; i < loreList.size(); i++){
                String[] loreParts = loreList.get(i).split(LORE_STRING_SEPARATOR);
                if(loreParts[0].equalsIgnoreCase(spellName)){
                    loreList.set(i, spellName + LORE_STRING_SEPARATOR + numberOfCharges);
                    found = true;
                }
            }
            if(!found){
                loreList.add(spellName + LORE_STRING_SEPARATOR + numberOfCharges);
            }
        }
        itemMeta.setLore(loreList);
        spellbook.setItemMeta(itemMeta);
    }

    public static void addCharges(ItemStack spellbook, String spellName, Integer numberToAdd){
        Integer currentCharges = getCharges(spellbook, spellName);
        if(currentCharges + numberToAdd > MAX_SPELL_CHARGES){
            numberToAdd = MAX_SPELL_CHARGES - currentCharges;
        }
        setCharges(spellbook, spellName, currentCharges + numberToAdd);
    }

    public static void rotateSpells(ItemStack spellbook){
        ItemMeta itemMeta = spellbook.getItemMeta();
        String currentSpell = itemMeta.getDisplayName();

        //Find where the current spell is in the list of spells on it.
        ArrayList<String> spells = (ArrayList<String>)itemMeta.getLore();
        Integer currentSpellLocation = null;
        for (int i=0; i < spells.size(); i++){
            if(spells.get(i).split(LORE_STRING_SEPARATOR)[0].equals(currentSpell)){

                currentSpellLocation = i;

                // Find the max location
                Integer maxLocation = spells.size() - 1;

                // Determine the new location (wrap around if needed)
                Integer newLocation;
                if(currentSpellLocation.equals(maxLocation)){
                    newLocation = 0;
                }
                else{
                    newLocation = currentSpellLocation + 1;
                }

                // Set the new display name.
                itemMeta.setDisplayName(spells.get(newLocation).split(LORE_STRING_SEPARATOR)[0]);
                spellbook.setItemMeta(itemMeta);
            }
        }
    }

    public static boolean isSpellbook(ItemStack book, MineCraftSpells plugin){
        ItemMeta itemMeta = book.getItemMeta();
        return spellExists(itemMeta.getDisplayName(), plugin);
    }

    public static boolean canCharge(ItemStack book, String spellName){
        ItemMeta itemMeta = book.getItemMeta();
        ArrayList<String> loreList = (ArrayList<String>)itemMeta.getLore();

        if(loreList == null){
            return true;
        }

        boolean found = false;
        //See if the loreList contains our spell.
        for(int i=0; i<loreList.size(); i++){
            if(loreList.get(i).split(LORE_STRING_SEPARATOR)[0].equalsIgnoreCase(spellName)){
                found = true;
            }
        }

        //If we already have max number of spells per book and we don't already have the spell in question...
        return (loreList.size() < MAX_NUMBER_OF_SPELLS_PER_BOOK) ||
                (loreList.size() == MAX_NUMBER_OF_SPELLS_PER_BOOK && found);
    }

    public static ItemStack[] getReagents(String spellName, MineCraftSpells plugin){
        String settingName = "spells." + spellName + ".reagents";

        List reagents = plugin.getConfig().getList(settingName);
        ItemStack[] items = new ItemStack[reagents.size()];

        for(int i = 0; i < reagents.size(); i++){
            HashMap<String, Object> reagent = (HashMap<String, Object>)reagents.get(i);
            items[i] = new ItemStack(
                    Material.valueOf(reagent.get("reagent").toString()),
                    (Integer)reagent.get("quantity")
            );
        }

        return items;
    }

    public static Class getSpellClass(String spellName, MineCraftSpells plugin){
        try {
            return Class.forName(plugin.getConfig().getString("spells." + spellName + ".class"));
        } catch (ClassNotFoundException e) {
            //e.printStackTrace();
        }
        catch(NullPointerException npe){
            //npe.printStackTrace();
        }
        return null;
    }

    public static boolean spellExists(String spellName, MineCraftSpells plugin){
        return plugin.getConfig().getString("spells." + spellName + ".class") != null;
    }


}
