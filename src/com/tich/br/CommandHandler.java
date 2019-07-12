package com.tich.br;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Evan Tichenor (evan.tichenor@gmail.com)
 * @version 1.0, 7/11/2019
 */
public class CommandHandler implements CommandExecutor {

    private Main br;

    public CommandHandler(Main br) {
        this.br = br;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            switch(args[0]) {
                case "remove":
                    // remove the block the sender is looking at's metadata
                    if(sender instanceof Player) {
                        br.removeMetadata(((Player) sender).getTargetBlock(null, 8));
                        sender.sendMessage("Removed the block's 'remorse' metadata.");
                    } else {
                        sender.sendMessage("You must be a player looking at a block to use this command!");
                    }
                    break;
                case "enable":
                    if(br.enabled) {
                        sender.sendMessage("Builder's Remorse is already enabled.");
                    } else {
                        br.enabled = true;
                        sender.sendMessage("Builder's Remorse has been re-enabled. Use /br disable to disable.");
                    }
                    break;
                case "disable":
                    if(br.enabled) {
                        br.enabled = false;
                        sender.sendMessage("Builder's Remorse has been disabled. Use /br enable to re-enable.");
                    } else {
                        sender.sendMessage("Builder's Remorse is already disabled.");
                    }
                    break;
                case "version":
                    sender.sendMessage(br.getDescription().getVersion());
                    break;
                case "help":
                case "usage":
                case "?":
                case "h":
                    showHelp(sender);
                    break;
            }
        } else {
            showHelp(sender);
        }

        return true;
    }

    private void showHelp(CommandSender sender) {
        sender.sendMessage(br.getCommand("buildersremorse").getUsage());
    }
}
