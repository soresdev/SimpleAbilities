package me.sores.simpleabilities.ability.impl;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.simpleabilities.util.MessageUtil;
import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.StringUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sores on 4/3/2020.
 */
public class Ability_recall extends Ability {

    private HashMap<UUID, Location> locationMap;

    public Ability_recall() {
        register();
        locationMap = new HashMap<>();
    }

    public HashMap<UUID, Location> getLocationMap() {
        return locationMap;
    }

    public boolean isPotionEffect(){
        return AbilitiesConfig.RECALL_POTION_EFFECT;
    }

    public int getRange(){
        return AbilitiesConfig.RECALL_RANGE;
    }

    @Override
    public String getName() {
        return "RECALL";
    }

    @Override
    public String getDisplay() {
        return "Recall";
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
        lore.add("&e- Range: &r" + getRange());
        lore.add("&e- Potion Effect: &r" + isPotionEffect());
        lore.add("&8&m------------------------------------------------");
        StringUtil.color(lore);

        return new ItemBuilder(Material.REDSTONE).setName(getDisplay()).setLore(StringUtil.color(lore)).build();
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("&bRecall Ability").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.RECALL_ENABLED;
    }

    @Override
    public int getCooldown() {
        return AbilitiesConfig.RECALL_COOLDOWN;
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
                StringUtil.color("&eRange: &f" + getRange()),
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
            StringUtil.color("&e - setRange <int> &f- Set the range of teleportation."),
            StringUtil.color("&e - toggleEffect &f- Toggle whether player receives negative effect after teleport."),
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

            case "setrange":{
                if(args.length < 1){
                    sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                    return false;
                }

                try{
                    int number = Integer.parseInt(args[1]);
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".range", number);

                    sender.sendMessage(StringUtil.color("&aYou have changed the range to " + number + " seconds."));
                }catch (NumberFormatException ex){
                    sender.sendMessage(StringUtil.color("&cYou must input a proper number."));
                }

                break;
            }

            case "toggleeffect":{
                if(isPotionEffect()){
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".potion_effect", false);
                    sender.sendMessage(StringUtil.color("&7You have &cdisabled &7the potion effects for this ability."));
                }else{
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".potion_effect", true);
                    sender.sendMessage(StringUtil.color("&7You have &aenabled &7the potion effects for this ability."));
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
    public void onRecallPlace(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null && item.isSimilar(getItem())){
                if(!isEnabled()){
                    player.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_DISABLED));
                    event.setCancelled(true);
                    return;
                }

                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                if(locationMap.containsKey(player.getUniqueId())){
                    player.sendMessage(StringUtil.color("&cYou already have a recall point."));
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                Location location = event.getClickedBlock().getLocation().add(new Vector(0, 1 ,0));
                locationMap.put(player.getUniqueId(), location);
                player.sendMessage(StringUtil.color("&aYou have set your recall point at " + location.getBlockX()
                        + ", " + location.getBlockY() + ", " + location.getBlockZ() + "."));
                event.setCancelled(true);
                player.updateInventory();

                location.getBlock().setType(Material.REDSTONE_TORCH_ON);
            }
        }
    }

    @EventHandler
    public void onRecall(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(item != null && item.isSimilar(getItem()) && player.isSneaking()){
                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                if(!locationMap.containsKey(player.getUniqueId())){
                    player.sendMessage(StringUtil.color("&cYou don't have a recall point set."));
                    return;
                }

                Location location = player.getLocation();
                Location check = locationMap.get(player.getUniqueId());

                if(location.getBlockX() > check.getBlockX() + getRange() ||
                        location.getBlockZ() > check.getBlockZ() + getRange() ||
                        location.getBlockY() > check.getBlockY() + getRange() ||
                        location.getBlockX() < check.getBlockX() - getRange() ||
                        location.getBlockZ() < check.getBlockZ() - getRange() ||
                        location.getBlockY() < check.getBlockY() - getRange()){
                    player.sendMessage(StringUtil.color("&cYou must be within " + getRange() + " blocks of your recall point."));
                    return;
                }

                player.getWorld().playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2, 2);

                player.teleport(check);
                player.sendMessage(StringUtil.color("&aYou have teleported to your recall point."));
                perform(player, this);

                if(item.getAmount() > 1){
                    item.setAmount(item.getAmount() - 1);
                }else{
                    player.getInventory().remove(item);
                }

                check.getBlock().setType(Material.AIR);
                locationMap.remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();

        if(locationMap.containsKey(player.getUniqueId())){
            locationMap.get(player.getUniqueId()).getBlock().setType(Material.AIR);
            locationMap.remove(player.getUniqueId());
        }
    }
}
