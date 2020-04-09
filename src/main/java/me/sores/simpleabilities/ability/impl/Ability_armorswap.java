package me.sores.simpleabilities.ability.impl;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.ability.runnable.ArmorSwapRunner;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.simpleabilities.util.MessageUtil;
import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sores on 4/2/2020.
 */
public class Ability_armorswap extends Ability {

    public Ability_armorswap() {
        register();
    }

    public boolean isPotionEffect(){
        return AbilitiesConfig.ARMOR_SWAP_POTION_ENABLED;
    }

    public int getTime() {
        return AbilitiesConfig.ARMOR_SWAP_TIME;
    }

    @Override
    public String getName() {
        return "ARMOR_SWAP";
    }

    @Override
    public String getDisplay() {
        return "Armor Swap";
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
        lore.add("&e- Time: &r" + getTime());
        lore.add("&e- Potion Effect: &r" + isPotionEffect());
        lore.add("&8&m------------------------------------------------");
        StringUtil.color(lore);

        return new ItemBuilder(Material.GOLD_AXE).setName(getDisplay()).setLore(StringUtil.color(lore)).build();
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.GOLD_AXE).setLore(Arrays.asList(StringUtil.color("&b&l&r"))).setName("&bArmor Swap Axe").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.ARMOR_SWAP_ENABLED;
    }

    @Override
    public int getCooldown() {
        return AbilitiesConfig.ARMOR_SWAP_COOLDOWN;
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
                StringUtil.color("&eTime: &f" + getTime()),
                StringUtil.color("&ePotion Effect: &f" + isPotionEffect()),
                StringUtil.color("&eDisplay: &r" + getDisplay()),
                StringUtil.color("&8&m------------------------------------------------"),
        };
    }

    private String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - toggle &f- Enable/Disable this ability."),
            StringUtil.color("&e - setCooldown <int> &f- Set the cooldown."),
            StringUtil.color("&e - settime <int> &f - Set the amount of time the player's helmet will be gone."),
            StringUtil.color("&e - togglePotion &f - Toggle the weakness potion effect on/off."),
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

            case "settime":{
                if(args.length < 1){
                    sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                    return false;
                }

                try{
                    int number = Integer.parseInt(args[1]);
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".time", number);

                    sender.sendMessage(StringUtil.color("&aYou have changed the time to " + number + " seconds."));
                }catch (Exception ex){
                    sender.sendMessage(StringUtil.color("&cYou must input a proper number."));
                }

                break;
            }

            case "togglepotion":{
                if(isPotionEffect()){
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".potion_effect", false);
                    sender.sendMessage(StringUtil.color("&7You have &cdisabled &7the Weakness potion effect for this ability."));
                }else{
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".potion_effect", true);
                    sender.sendMessage(StringUtil.color("&7You have &aenabled &7the Weakness potion effect for this ability."));
                    return false;
                }
            }

            default:{
                sender.sendMessage(abilityUsage);
                break;
            }
        }

        return false;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player player = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            ItemStack item = player.getInventory().getItemInHand();

            if(item != null && item.getItemMeta() != null && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(StringUtil.color("&b&l&r"))){
                if(!isEnabled()){
                    player.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_DISABLED));
                    return;
                }

                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    return;
                }

                ItemStack helmet = damaged.getInventory().getHelmet();
                new ArmorSwapRunner(damaged, this, helmet).runTaskLaterAsynchronously(SimpleAbilities.getInstance(), getTime() * 20);
                damaged.getInventory().setHelmet(new ItemStack(Material.AIR));
                damaged.sendMessage(StringUtil.color("&cYou've been struck by an Armor Swap axe, you will lose your helmet for " + getTime() + " seconds."));
                if(isPotionEffect()) damaged.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, getTime() * 20, 0));

                perform(player, this);
            }
        }
    }

}
