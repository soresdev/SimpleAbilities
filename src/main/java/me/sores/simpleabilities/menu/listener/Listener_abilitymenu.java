package me.sores.simpleabilities.menu.listener;

import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.spark.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/8/2020.
 */
public class Listener_abilitymenu implements Listener {

    @EventHandler
    public void onAbilityClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if(item == null || !item.hasItemMeta()) return;

        if(event.getInventory().getName().equalsIgnoreCase(StringUtil.color(AbilitiesConfig.MENU_TITLE))){
            if(item.getItemMeta().getDisplayName().equals(" ") || item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtil.color("&6Menu Info"))){
                event.setCancelled(true);
                return;
            }

            Ability ability = AbilityManager.getInstance().valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()));

            if(ability == null){
                event.setCancelled(true);
                StringUtil.log("&cInvalid ability in AbilityMenu.");
                return;
            }

            if(ability.isEnabled()){
                AbilityManager.getInstance().disableAbility(ability);
                player.sendMessage(StringUtil.color("&7You have &cdisabled &7the &6" + ability.getName() + " &7ability."));

                event.setCancelled(true);
                player.closeInventory();
            }else{
                AbilityManager.getInstance().enableAbility(ability);
                player.sendMessage(StringUtil.color("&7You have &aenabled &7the &6" + ability.getName() + " &7ability."));

                event.setCancelled(true);
                player.closeInventory();
            }
        }
    }

}
