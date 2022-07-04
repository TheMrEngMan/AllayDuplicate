package com.MrEngMan.AllayDuplicate;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;

    NamespacedKey allayCooldownKey;
    long cooldownTimeMillis = 6000 * 50; // 5 minutes = 6000 ticks * 50 milliseconds per tick
    int jukeboxAllayRange = 9; // Blocks

    // When plugin is first enabled
    @SuppressWarnings("static-access")
    @Override
    public void onEnable() {
        this.plugin = this;

        // Register stuff
        new InteractionListener(this);
        allayCooldownKey = new NamespacedKey(plugin, "DuplicationCooldown");

    }

}