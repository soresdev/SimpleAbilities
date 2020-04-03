package me.sores.simpleabilities.commands;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.simpleabilities.util.MessageUtil;
import me.sores.spark.util.StringUtil;
import me.sores.spark.util.command.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 4/2/2020.
 */
public class Command_abilityreload implements ICommand {

    private SimpleAbilities simpleAbilities;

    public Command_abilityreload(SimpleAbilities simpleAbilities) {
        this.simpleAbilities = simpleAbilities;

        register();
    }

    private String usage = StringUtil.color("&cUsage: /abilityreload <config:lang>");

    @Override
    public void execute(CommandSender sender, String... args) {
        if(!sender.hasPermission("abilities.reload")){
            MessageUtil.sendPermissionMessage(sender);
            return;
        }

        if(args.length == 0){
            sender.sendMessage(usage);
            return;
        }

        String input = args[0].toLowerCase();

        switch (input){
            case "lang":{
                new AbilitiesLang();
                sender.sendMessage(StringUtil.color("&aYou have successfully reloaded the SimpleAbilities Lang file."));
                break;
            }

            case "config":{
                AbilitiesConfig.reload();
                sender.sendMessage(StringUtil.color("&aYou have successfully reloaded the SimpleAbilities Config file."));
                break;
            }

            default:{
                sender.sendMessage(usage);
            }
        }
    }

    @Override
    public void register() {
        simpleAbilities.getCommand("abilityreload").setExecutor(this);
    }

    @Override
    public void sendPermissionMessage(Player player) {

    }

    @Override
    public boolean hasPermisson(Player player, String s) {
        return false;
    }
}
