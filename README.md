# MineCraftSpells

A spellcasting plugin for MineCraft servers using Bukkit

MineCraftSpells is my first plugin. Please help me make it better!

## Player Guide


If you're a player on a server using MineCraft Plugins, here's what you need to know:

### How do I cast spells?


Once you obtained a spellbook, simply put it in your hand and right click! Depending on the spell you can't click too close to yourself because you might hurt yourself.

### How do I get a spellbook?

Great question! First off, you need a book. Then you need to collect items from the MineCraft world. Once you have those items, you put the book in your hand and use the /charge command to charge your spellbook.

### How does the charge command work?

Simply type /charge spell_name [number_of_charges]

**Example: /charge fire_blast 10**

"spell_name" is the name of the of the spell you'd like to place on the book. "number_of_charge" is an optional parameter that says how many charges you'd like to attempt to place on the book.

If you have the right reagents for the spell, it'll remove them from your inventory and place charges on your book. Rad!

### What spells are there? and what materials are required?

Both are logical next questions. Here's the list of spells and their reagents so far:

* explosion: 2 gunpowder - shoots a fireball that explodes with a large radius.
* fire_blast: 2 flint and steel - shoots a fireball that explodes with a small radius and sets stuff on fire.
* lightning_bolt: 1 gold ingot - causes a lightning bolt to strike where you're pointing
* teleport: 3 redstone dust - teleports you to the location you click.
* stone_wall: 8 stone blocks - creates a temporary wall out of bedrock
* heal_self: 1 diamond - heals yourself for 30% of your max health

If you have any questions or ideas for new spells, let me know!

## Server Admin Guide

Don't like the default settings for spells? No worries! I made them all configurable.

If you look at plugins/MineCraftSpells-1.0/config.yml, you'll be able to tweak the spell settings and reagents!

Happy spellcasting!
