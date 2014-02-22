package com.gmail.spraetz.spells;

import com.gmail.spraetz.plugin.Engine;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by spraetz on 2/17/14.
 */
public class Spellbook {

    public static Integer MAX_SPELL_CHARGES = 64;

    public static String chargeString(Integer numberOfCharges){
        return "charges: " + numberOfCharges;
    }

    public static Integer getCharges(ItemStack spellbook){
        ArrayList<String> loreList = (ArrayList<String>)spellbook.getItemMeta().getLore();
        if(loreList == null || loreList.size() < 1){
            return 0;
        }
        return Integer.parseInt(loreList.get(0).split(" ")[1]);
    }

    public static void setCharges(ItemStack spellbook, Integer numberOfCharges){
        ItemMeta itemMeta = spellbook.getItemMeta();
        ArrayList<String> loreList = (ArrayList<String>)itemMeta.getLore();
        if(loreList == null || loreList.size() == 0){
            loreList = new ArrayList<String>();
            loreList.add(chargeString(numberOfCharges));
        }
        else{
            loreList.set(0, chargeString(numberOfCharges));
        }
        itemMeta.setLore(loreList);
        spellbook.setItemMeta(itemMeta);
    }

    public static Integer addCharges(ItemStack spellbook, Integer numberToAdd){
        Integer currentCharges = getCharges(spellbook);
        if(currentCharges + numberToAdd > MAX_SPELL_CHARGES){
            numberToAdd = MAX_SPELL_CHARGES - currentCharges;
        }
        setCharges(spellbook, currentCharges + numberToAdd);

        return numberToAdd;
    }

    public static void setSpell(ItemStack spellbook, String spellName){
        ItemMeta itemMeta = spellbook.getItemMeta();
        itemMeta.setDisplayName(spellName);
        spellbook.setItemMeta(itemMeta);
    }

    public static String getSpell(ItemStack spellbook){
        return spellbook.getItemMeta().getDisplayName();
    }

    public static boolean isSpellbook(ItemStack book, Engine plugin){
        ItemMeta itemMeta = book.getItemMeta();
        return Spell.spellExists(itemMeta.getDisplayName(), plugin);
    }
}
