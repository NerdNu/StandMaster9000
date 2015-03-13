package com.michaelelin.StandMaster.command;

import java.util.Collection;
import java.util.Deque;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.michaelelin.StandMaster.CommandTree;
import com.michaelelin.StandMaster.StandMasterException;
import com.michaelelin.StandMaster.StandMasterPlugin;
import com.michaelelin.StandMaster.data.DataModifier;
import com.michaelelin.StandMaster.data.StandMasterData;

/**
 * A command to load, add, or remove a modifier preset.
 */
public final class PresetCommand extends ParentCommand {

    /**
     * Constructs a {@code PresetCommand} from the given name and
     * description.
     *
     * @param name the command's name
     * @param description the command's description
     */
    public PresetCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public void printHelp(CommandSender sender, Collection<String> context) {
        String fullCommand = CommandTree.getFullCommand(context, getName());
        sender.sendMessage(ChatColor.GOLD
                + fullCommand + ChatColor.WHITE + " - " + getDescription());
        sender.sendMessage("Usage: " + CommandTree.getFullCommand(context, getName())
                + " <preset>");
        if (!this.getAllowedSubcommands(sender).isEmpty()) {
            sender.sendMessage("========");
            printSubcommands(sender);
        }
        sender.sendMessage("========");
        sender.sendMessage(ChatColor.AQUA + "Armor stand presets:");
        for (String preset : StandMasterPlugin.getInstance().getPresetManager().listPresets()) {
            sender.sendMessage(preset);
        }
    }

    @Override
    public void execute(CommandSender sender, Deque<String> context, Deque<String> args) {
        if (!(sender instanceof Player)) {
            throw new StandMasterException("That command can't be run from console.");
        }

        Player player = (Player) sender;

        String arg = args.poll();

        StandMasterCommand command = getSubcommand(arg);

        if (command == null) {
            List<DataModifier<? extends Entity, ? extends StandMasterData>.Executable> mods =
                    StandMasterPlugin.getInstance().getPresetManager().get(arg);
            if (mods == null || !args.isEmpty()) {
                printHelp(player, context);
            } else {
                for (DataModifier<? extends Entity, ? extends StandMasterData>.Executable mod : mods) {
                    System.out.println(mod.getIdentifier() + ": " + mod.getValue());
                }
                StandMasterPlugin.getInstance().getModifierList(player).addAll(mods);
                player.sendMessage(ChatColor.AQUA + "Preset loaded.");
            }
        } else {
            context.add(getName());
            command.execute(sender, context, args);
        }
    }

}