package com.tich.br;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.01, 7/11/2019
 */
public class Main extends JavaPlugin implements Listener {

    private static final String META = "placed";

    boolean enabled;

    // a list of new blocks to add to the config
    private List<Location> playerBlocks;

    @Override
    public void onEnable() {
        // setup default values
        getConfig().options().header(
                "Builder's Remorse configuration file. \n" +
                "Used for storing locations in which blocks were placed by players. \n" +
                "Required for persistence between shutdown and startup."
        );
        getConfig().addDefault("enabled", true);
        getConfig().addDefault("blocks", new ArrayList<String>());
        getConfig().options().copyDefaults(true);
        saveConfig();
        // done with that setup

        getServer().getPluginManager().registerEvents(this, this);
        getCommand("buildersremorse").setExecutor(new CommandHandler(this));

        // load config file and setup other variables
        enabled = getConfig().getBoolean("enabled");
        playerBlocks = new ArrayList<>();

        // set existing metadata for blocks
        List<String> locs = getConfig().getStringList("blocks");
        for(int i = 0; i < locs.size(); i++) {
            String[] l = locs.get(i).split(" ");
            addMetadataNoList(getServer()
                    .getWorld(l[0])
                    .getBlockAt(Integer.parseInt(l[1]), Integer.parseInt(l[2]), Integer.parseInt(l[3]))
            );
        }
    }

    @Override
    public void onDisable() {
        // save all metadata for blocks from list
        List<String> list = getConfig().getStringList("blocks");

        // add all new player blocks
        playerBlocks.stream()
                .filter( l -> !isAir(l.getBlock())) // filter all air blocks out because we can place stuff in air
                .map(Main::briefLocation) // map to strings
                .forEach(list::add); // add to list

        // save em'
        getConfig().set("enabled", enabled);
        getConfig().set("blocks", list); // saves as a nice list of locations that need to be parsed on startup-
        saveConfig();
    }

    // UTILITY

    private static boolean isAir(Block b) {
        // void air too because why not
        return b.getType() == Material.AIR || b.getType() == Material.CAVE_AIR || b.getType() == Material.VOID_AIR;
    }

    private static String briefLocation(Location l) {
        return l.getWorld().getName() + " " + l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ();
    }

    private void addMetadata(Block b) {
        playerBlocks.add(b.getLocation());

        addMetadataNoList(b);
    }

    private void addMetadataNoList(Block b) {
        b.setMetadata(META, new FixedMetadataValue(this, true));
    }

    private boolean isPlaced(Block b) {
        return b.hasMetadata(META) && b.getMetadata(META).get(0).asBoolean();
    }

    public void removeMetadata(Block... blocks) {
        for(Block b : blocks) {
            if(b.hasMetadata(META)) {
                playerBlocks.remove(b.getLocation());
                b.removeMetadata(META, this);
            }
        }
    }

    // END UTILITY

    // EVENT HANDLERS

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        if(!enabled)
            return;

        // block placed by player
        addMetadata(e.getBlockPlaced());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if(!enabled)
            return;

        if(isPlaced(e.getBlock())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("That block was placed by a player!");
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent e) {
        if(!enabled)
            return;

        addMetadata(e.getBlockClicked().getRelative(e.getBlockFace()));
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent e) {
        if(!enabled)
            return;

        if(isPlaced(e.getBlockClicked())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent e) {
        // remove block metadata
        removeMetadata(e.getBlock());
    }

    @EventHandler
    public void onBlockFertilize(BlockFertilizeEvent e) {
        // remove block metadata
        removeMetadata(e.getBlock());
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent e) {
        // remove metadata on blocks where saplings and mushrooms were
        for(BlockState b : e.getBlocks()) {
            removeMetadata(b.getBlock());
        }
    }

    @EventHandler
    public void onBlockMultiplace(BlockMultiPlaceEvent e) {
        if(!enabled)
            return;

        // add metadata to extra block
        for(BlockState b : e.getReplacedBlockStates()) {
            addMetadata(b.getBlock());
        }
    }
}
