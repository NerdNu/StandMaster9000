package com.michaelelin.standmaster.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.michaelelin.standmaster.CommandTree;

/**
 * An abstract {@code StandMasterCommand} implementation.
 */
public abstract class AbstractCommand implements StandMasterCommand {

    private final String name;
    private final String description;

    private final List<String> aliases;

    private String permission;

    protected AbstractCommand(String name, String description) {
        this.name = name;
        this.description = description;
        aliases = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public StandMasterCommand addAlias(String alias) {
        aliases.add(alias);
        return this;
    }

    @Override
    public boolean hasAlias(String name) {
        for (String alias : aliases) {
            if (name.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StandMasterCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return permission == null || sender.hasPermission(permission);
    }

    @Override
    public void printHelp(CommandSender sender, Collection<String> context) {
        sender.sendMessage(ChatColor.GOLD
                + CommandTree.getFullCommand(context, getName()) + ChatColor.WHITE + " - "
                + getDescription());
    }

}
