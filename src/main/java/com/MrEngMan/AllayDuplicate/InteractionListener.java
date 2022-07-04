package com.MrEngMan.AllayDuplicate;

import org.bukkit.*;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InteractionListener implements Listener {

    public InteractionListener(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        World world = entity.getWorld();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offhandItem = player.getInventory().getItemInOffHand();

        if(entity.getType() == EntityType.ALLAY) {
            boolean shardInMainHand = mainHandItem.getType() == Material.AMETHYST_SHARD;
            boolean shardInOffHand = offhandItem.getType() == Material.AMETHYST_SHARD;
            if(shardInMainHand || shardInOffHand) {

                // Check if allay is not in cooldown
                PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
                if(dataContainer.has(Main.plugin.allayCooldownKey, PersistentDataType.LONG)) {
                    long lastAllayDuplicationTick = dataContainer.get(Main.plugin.allayCooldownKey, PersistentDataType.LONG);

                    // Not enough time passed
                    if(System.currentTimeMillis() - lastAllayDuplicationTick < Main.plugin.cooldownTimeMillis) {
                        return;
                    }
                }

                Allay allay = ((Allay)entity);
                Location allayLocation = allay.getLocation();

                for(int x = allayLocation.getBlockX() - Main.plugin.jukeboxAllayRange; x < allayLocation.getBlockX() + Main.plugin.jukeboxAllayRange; x++) {
                    for(int y = allayLocation.getBlockY() - Main.plugin.jukeboxAllayRange; y < allayLocation.getBlockY() + Main.plugin.jukeboxAllayRange; y++) {
                        for(int z = allayLocation.getBlockZ() - Main.plugin.jukeboxAllayRange; z < allayLocation.getBlockZ() + Main.plugin.jukeboxAllayRange; z++) {

                            if(world.getBlockAt(x, y, z).getType() == Material.JUKEBOX) {
                                Jukebox jukebox = ((Jukebox)world.getBlockAt(x, y, z).getState());
                                if(jukebox.isPlaying()) {

                                    // If allay is out of range and can't hear the jukebox
                                    double distance = allayLocation.distance(new Location(world, x, y, z));
                                    if(distance > Main.plugin.jukeboxAllayRange) continue;

                                    // Duplicate allay and create heart particles
                                    Entity duplicatedAllay = world.spawnEntity(allayLocation, EntityType.ALLAY);
                                    world.spawnParticle(Particle.HEART, allayLocation, 3, 0.2, 0.2, 0.2);

                                    // Save cooldown for both allays
                                    dataContainer.set(Main.plugin.allayCooldownKey, PersistentDataType.LONG, System.currentTimeMillis());
                                    duplicatedAllay.getPersistentDataContainer().set(Main.plugin.allayCooldownKey, PersistentDataType.LONG, System.currentTimeMillis());

                                    // Take away shard from player
                                    if(player.getGameMode() != GameMode.CREATIVE) {
                                        if (shardInMainHand) {
                                            mainHandItem.setAmount(mainHandItem.getAmount() - 1);
                                        } else {
                                            offhandItem.setAmount(offhandItem.getAmount() - 1);
                                        }
                                    }

                                    // Don't give shard to original allay
                                    event.setCancelled(true);

                                }
                            }

                        }
                    }
                }

            }
        }

    }

}
