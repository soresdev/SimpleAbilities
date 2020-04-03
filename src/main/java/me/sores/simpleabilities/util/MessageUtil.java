package me.sores.simpleabilities.util;

import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.spark.util.StringUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 3/31/2020.
 */
public class MessageUtil {

    public static void sendPermissionMessage(Player player){
        player.sendMessage(StringUtil.color(AbilitiesLang.NO_PERMISSION));
    }

    public static void sendPermissionMessage(CommandSender sender){
        sender.sendMessage(StringUtil.color(AbilitiesLang.NO_PERMISSION));
    }

    public static void sendCooldownMessage(Player player, Ability ability){
        player.sendMessage(StringUtil.color(AbilitiesLang.COOLDOWN_MESSAGE).replace("%ability%", ability.getDisplay())
                .replace("%time%", String.valueOf(AbilityManager.getInstance().getRemainingTime(player))));
    }

    public static void sendNoLongerOnCooldownMessage(Player player, Ability ability){
        player.sendMessage(StringUtil.color(AbilitiesLang.NO_LONGER_ON_COOLDOWN).replace("%ability%", ability.getDisplay()));
    }

    public static void sendAbilityList(CommandSender sender){
        String list = "";

        for(int i= 0; i < AbilityManager.getInstance().getAbilities().size(); i++){
            Ability ability = AbilityManager.getInstance().getAbilities().get(i);
            if(i == AbilityManager.getInstance().getAbilities().size() - 1){
                list += StringUtil.color(ability.isEnabled() ? "&a" + ability.getName() : "&c" + ability.getName());
            }else{
                list += StringUtil.color(ability.isEnabled() ? "&a" + ability.getName() + "&6, " : "&c" + ability.getName() + "&6, ");
            }
        }

        String[] send = {
                StringUtil.color("&8&m------------------------------------------------"),
                StringUtil.color("&6&lAbilities: " + list),
                StringUtil.color("&8&m------------------------------------------------"),
        };

        sender.sendMessage(send);
    }

}
