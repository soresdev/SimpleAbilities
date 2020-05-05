package me.sores.simpleabilities.commands;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.simpleabilities.menu.AbilityMenu;
import me.sores.simpleabilities.util.MessageUtil;
import me.sores.spark.util.InventoryUtil;
import me.sores.spark.util.PlayerUtil;
import me.sores.spark.util.StringUtil;
import me.sores.spark.util.command.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 3/30/2020.
 */
public class Command_ability implements ICommand {

    private SimpleAbilities simpleAbilities;

    public Command_ability(SimpleAbilities simpleAbilities) {
        this.simpleAbilities = simpleAbilities;

        register();
    }

    private String[] usage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6&lAbility Usage: "),
            StringUtil.color("&e/ability give <player> <ability> &f- Give a player an ability."),
            StringUtil.color("&e/ability clear <player> &f- Clear a player's ability cooldown."),
            StringUtil.color("&e/ability edit <ability> &f- Edit an ability's individual attributes."),
            StringUtil.color("&e/ability info <ability> &f - View info of an ability."),
            StringUtil.color("&e/ability list &f- List all abilities."),
            StringUtil.color("&e/ability menu &f- View the ability menu."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public void execute(CommandSender sender, String... args) {
        if(!sender.hasPermission("abilities.ability")){
            MessageUtil.sendPermissionMessage(sender);
            return;
        }

        if(args.length == 0){
            sender.sendMessage(usage);
            return;
        }

        switch (args[0].toLowerCase()){
            case "give":{
                if(args.length < 4){
                    sender.sendMessage(StringUtil.color("&cUsage: /ability give <player:all> <ability> <amount>"));
                    return;
                }


                String name = args[1];
                Ability ability = AbilityManager.getInstance().valueOf(args[2]);
                Player target = Bukkit.getPlayer(name);

                if(name.equalsIgnoreCase("all")){
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        try{
                            int amount = Integer.parseInt(args[3]);

                            for(int i = 0; i < amount; i++){
                                InventoryUtil.giveItemSafely(online, ability.getItem());
                            }

                            sender.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_GIVEN_ALL).replace("%amount%", String.valueOf(amount))
                                    .replace("%ability%", ability.getDisplay()));
                        }catch (NumberFormatException ex){
                            sender.sendMessage(StringUtil.color("cPlease input a proper number."));
                        }
                    });
                    return;
                }

                if(!PlayerUtil.doesExist(target)){
                    sender.sendMessage(StringUtil.color(AbilitiesLang.NO_PLAYER_FOUND).replace("%name%", args[1]));
                    return;
                }

                if(!AbilityManager.getInstance().getAbilities().contains(ability)){
                    sender.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_NOT_FOUND).replace("%ability%", args[2]));
                    return;
                }

                try{
                    int amount = Integer.parseInt(args[3]);

                    for (int i = 0; i < amount; i++){
                        InventoryUtil.giveItemSafely(target, ability.getItem());
                    }

                    sender.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_GIVEN).replace("%name%", target.getName()).replace("%amount%", String.valueOf(amount))
                    .replace("%ability%", ability.getDisplay()));
                }catch (NumberFormatException ex){
                    sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                }

                break;
            }

            case "reset":
            case "clear":{
                if(args.length < 2){
                    sender.sendMessage(StringUtil.color("&cUsage: /ability clear <player>"));
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if(!PlayerUtil.doesExist(target)){
                    sender.sendMessage(StringUtil.color(AbilitiesLang.NO_PLAYER_FOUND).replace("%name%", args[1]));
                    return;
                }

                if(!AbilityManager.getInstance().isOnCooldown(target)){
                    sender.sendMessage(StringUtil.color(AbilitiesLang.NOT_ON_COOLDOWN).replace("%name%", target.getName()));
                    return;
                }

                AbilityManager.getInstance().remove(target);
                sender.sendMessage(StringUtil.color(AbilitiesLang.COOLDOWN_REMOVED).replace("%name%", target.getName()));
                break;
            }

            case "edit":{
                if(args.length < 2){
                    sender.sendMessage(StringUtil.color("&cUsage: /ability edit <ability>"));
                    return;
                }

                Ability ability = AbilityManager.getInstance().valueOf(args[1]);

                if(!AbilityManager.getInstance().getAbilities().contains(ability)){
                    sender.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_NOT_FOUND).replace("%ability%", args[1]));
                    return;
                }

                ability.makeChange(sender, StringUtil.trimList(args, 2));
                break;
            }

            case "info":{
                if(args.length < 2){
                    sender.sendMessage(StringUtil.color("&cUsage: /ability info <ability>"));
                    return;
                }

                Ability ability = AbilityManager.getInstance().valueOf(args[1]);

                if(!AbilityManager.getInstance().getAbilities().contains(ability)){
                    sender.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_NOT_FOUND).replace("%ability%", args[1]));
                    return;
                }

                sender.sendMessage(ability.getInfo());
                break;
            }

            case "list": {
                MessageUtil.sendAbilityList(sender);
                break;
            }

            case "menu":{
                if(!(sender instanceof Player)){
                    sender.sendMessage(StringUtil.color("&cOnly players can open the ability menu."));
                    return;
                }

                new AbilityMenu((Player) sender).open((Player) sender);
                break;
            }

            default:{
                sender.sendMessage(usage);
            }
        }
    }

    @Override
    public void register() {
        simpleAbilities.getCommand("ability").setExecutor(this);
    }

    @Override
    public void sendPermissionMessage(Player player) {
        MessageUtil.sendPermissionMessage(player);
    }

    @Override
    public boolean hasPermisson(Player player, String perm) {
        return player.hasPermission(perm);
    }
}
