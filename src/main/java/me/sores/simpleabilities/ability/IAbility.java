package me.sores.simpleabilities.ability;

import org.bukkit.command.CommandSender;

/**
 * Created by sores on 3/31/2020.
 */
public interface IAbility {

    String[] getInfo();

    boolean makeChange(CommandSender sender, String[] args);

}
