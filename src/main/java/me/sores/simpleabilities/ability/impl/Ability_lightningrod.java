package me.sores.simpleabilities.ability.impl;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.simpleabilities.util.MessageUtil;
import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 4/2/2020.
 */
public class Ability_lightningrod extends Ability {

    public Ability_lightningrod() {
        register();
    }

    @Override
    public String getName() {
        return "LIGHTNING_ROD";
    }

    @Override
    public String getDisplay() {
        return "Lightning Rod";
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.BLAZE_ROD).setName("&bLightning Rod Ability").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.LIGHTNING_ROD_ENABLED;
    }

    @Override
    public int getCooldown() {
        return AbilitiesConfig.LIGHTNING_ROD_COOLDOWN;
    }

    @Override
    public void register() {
        AbilityManager.getInstance().registerAbility(this);
        SimpleAbilities.getInstance().getServer().getPluginManager().registerEvents(this, SimpleAbilities.getInstance());
    }

    @Override
    public String[] getInfo() {
        return new String[] {
                StringUtil.color("&6Showing ability info for &l" + getName()),
                StringUtil.color("&8&m------------------------------------------------"),
                StringUtil.color("&eEnabled: &f" + isEnabled()),
                StringUtil.color("&eCooldown: &f" + getCooldown()),
                StringUtil.color("&eDisplay: &r" + getDisplay()),
                StringUtil.color("&8&m------------------------------------------------"),
        };
    }

    private String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - toggle &f- Enable/Disable this ability."),
            StringUtil.color("&e - setCooldown <int> &f- Set the cooldown."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public boolean makeChange(CommandSender sender, String[] args) {
        if(args.length == 0){
            sender.sendMessage(abilityUsage);
            return false;
        }

        switch (args[0].toLowerCase()){
            case "toggle":{
                if(isEnabled()){
                    AbilityManager.getInstance().disableAbility(this);
                    sender.sendMessage(StringUtil.color("&7You have &cdisabled &7the &6" + getName() + " &7ability."));
                }else{
                    AbilityManager.getInstance().enableAbility(this);
                    sender.sendMessage(StringUtil.color("&7You have &aenabled &7the &6" + getName() + " &7ability."));
                    return false;
                }

                break;
            }

            case "setcooldown":{
                if(args.length < 1){
                    sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                    return false;
                }

                try{
                    int number = Integer.parseInt(args[1]);
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".cooldown", number);

                    sender.sendMessage(StringUtil.color("&aYou have changed the cooldown to " + number + " seconds."));
                }catch (NumberFormatException ex){
                    sender.sendMessage(StringUtil.color("&cYou must input a proper number."));
                }

                break;
            }

            default:{
                sender.sendMessage(abilityUsage);
                break;
            }
        }

        return false;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(event.getRightClicked() instanceof Player){
            Player clicked = (Player) event.getRightClicked();

            if(item != null && item.isSimilar(getItem().clone())){
                if(!isEnabled()){
                    player.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_DISABLED));
                    return;
                }

                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                if(player.getItemInHand().getAmount() > 1){
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                }else{
                    player.getInventory().remove(player.getItemInHand());
                }

                clicked.getWorld().strikeLightning(clicked.getLocation());
                event.setCancelled(true);
                player.updateInventory();

                player.sendMessage(StringUtil.color("&aYou have smited " + clicked.getName() + " with your Lighting Rod."));
                clicked.sendMessage(StringUtil.color("&cYou have been smited by " + player.getName() + "'s Lighting Rod."));
                perform(player, this);
            }
        }
    }

}
