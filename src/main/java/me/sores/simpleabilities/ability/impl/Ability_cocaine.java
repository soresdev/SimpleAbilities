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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sores on 4/1/2020.
 */
public class Ability_cocaine extends Ability {

    public Ability_cocaine() {
        register();
    }

    public int getTime() {
        return AbilitiesConfig.COCAINE_SPEED_TIME;
    }

    public int getSpeedLevel() {
        return AbilitiesConfig.COCAINE_SPEED_LEVEL;
    }

    @Override
    public String getName() {
        return "COCAINE";
    }

    @Override
    public String getDisplay() {
        return "Cocaine";
    }

    @Override
    public ItemStack getIcon() {
        List<String> lore = new ArrayList<>();
        lore.add("&8&m------------------------------------------------");
        lore.add("&6- Default Values -");
        lore.add("&e- Enabled: &r" + isEnabled());
        lore.add("&e- Cooldown: &r" + getCooldown());
        lore.add(" ");
        lore.add("&6- Ability Attributes -");
        lore.add("&e- Speed Level: &r" + getSpeedLevel());
        lore.add("&e- Speed Time: &r" + getTime());
        lore.add("&8&m------------------------------------------------");
        StringUtil.color(lore);

        return new ItemBuilder(Material.SUGAR).setName(getDisplay()).setLore(StringUtil.color(lore)).build();
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.SUGAR).setName("&bCocaine Ability").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.COCAINE_ENABLED;
    }

    @Override
    public int getCooldown() {
        return AbilitiesConfig.COCAINE_COOLDOWN;
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
                StringUtil.color("&eSpeed Time: &f " + getTime()),
                StringUtil.color("&eSpeed Level: &f" + getSpeedLevel()),
                StringUtil.color("&eDisplay: &r" + getDisplay()),
                StringUtil.color("&8&m------------------------------------------------"),
        };
    }

    private String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - toggle &f- Enable/Disable this ability."),
            StringUtil.color("&e - setCooldown <int> &f- Set the cooldown."),
            StringUtil.color("&e - setSpeedLevel <int> &f- Set the speed potion level."),
            StringUtil.color("&e - setSpeedTime <int> &f- Set the duration of speed effect."),
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

            case "setspeedlvl":
            case "setspeedlevel":{
                    if(args.length < 1){
                        sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                        return false;
                    }

                    try{
                        int number = Integer.parseInt(args[1]);
                        AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".speed_level", number);

                        sender.sendMessage(StringUtil.color("&aYou have changed the speed level to " + number));
                    }catch (Exception ex){
                        sender.sendMessage(StringUtil.color("&cYou must input a proper number."));
                    }

                break;
            }

            case "setspeedtime":{
                if(args.length < 1){
                    sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                    return false;
                }

                try{
                    int number = Integer.parseInt(args[1]);
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".speed_time", number);

                    sender.sendMessage(StringUtil.color("&aYou have changed the speed time to " + number + " seconds."));
                }catch (Exception ex){
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
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null && item.isSimilar(getItem())){
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

                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, getTime() * 20, getSpeedLevel()));
                player.sendMessage(StringUtil.color("&aYou have used your Cocaine Ability."));
                perform(player, this);
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }

}
