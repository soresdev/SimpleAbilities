package me.sores.simpleabilities.ability.impl;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.ability.runnable.AntiBuildRunner;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.simpleabilities.util.MessageUtil;
import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by sores on 4/1/2020.
 */
public class Ability_antibuild extends Ability {

    private HashMap<UUID, Integer> buildMap;

    public Ability_antibuild() {
        register();
        buildMap = new HashMap<>();
    }

    public int getTime() {
        return AbilitiesConfig.ANTI_BUILD_TIME;
    }

    public HashMap<UUID, Integer> getBuildMap() {
        return buildMap;
    }

    @Override
    public String getName() {
        return "ANTI_BUILD";
    }

    @Override
    public String getDisplay() {
        return "Anti-Build";
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.EGG).setName("&bAnti-Build Ability").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.ANTI_BUILD_ENABLED;
    }

    @Override
    public int getCooldown() {
        return AbilitiesConfig.ANTI_BUILD_COOLDOWN;
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
                StringUtil.color("&eAnti-Build Time: &f " + getTime()),
                StringUtil.color("&eDisplay: &r" + getDisplay()),
                StringUtil.color("&8&m------------------------------------------------"),
        };
    }

    private String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - toggle &f- Enable/Disable this ability."),
            StringUtil.color("&e - setCooldown <int> &f- Set the cooldown."),
            StringUtil.color("&e - setTime <int> &f- Set the duration of the anti-build effect."),
            StringUtil.color("&8&m------------------------------------------------"),
    };

    @Override
    public boolean makeChange(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(abilityUsage);
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "toggle": {
                if (isEnabled()) {
                    AbilityManager.getInstance().disableAbility(this);
                    sender.sendMessage(StringUtil.color("&7You have &cdisabled &7the &6" + getName() + " &7ability."));
                } else {
                    AbilityManager.getInstance().enableAbility(this);
                    sender.sendMessage(StringUtil.color("&7You have &aenabled &7the &6" + getName() + " &7ability."));
                    return false;
                }

                break;
            }

            case "setcooldown": {
                if (args.length < 1) {
                    sender.sendMessage(StringUtil.color("&cPlease input a proper number."));
                    return false;
                }

                try {
                    int number = Integer.parseInt(args[1]);
                    AbilityManager.getInstance().makeConfigChange("abilities." + getName().toLowerCase() + ".cooldown", number);

                    sender.sendMessage(StringUtil.color("&aYou have changed the cooldown to " + number + " seconds."));
                } catch (NumberFormatException ex) {
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

                    sender.sendMessage(StringUtil.color("&aYou have changed the anti-build time to " + number + " seconds."));
                }catch (Exception ex){
                    sender.sendMessage(StringUtil.color("&cYou must input a proper number."));
                }

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
            if(!isEnabled()){
                player.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_DISABLED));
                return;
            }

            if(item != null && item.isSimilar(getItem())){
                if(!canPerform(player)){
                    MessageUtil.sendCooldownMessage(player, this);
                    event.setCancelled(true);
                    player.updateInventory();
                    return;
                }

                event.setCancelled(true);
                Egg egg = player.launchProjectile(Egg.class);
                egg.setMetadata("launched_egg", new FixedMetadataValue(SimpleAbilities.getInstance(), "launched_egg"));

                if(player.getItemInHand().getAmount() > 1){
                    player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
                }else{
                    player.getInventory().remove(player.getItemInHand());
                }

                perform(player, this);
            }
        }
    }

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent event){
        event.setHatching(false);
    }

    @EventHandler
    public void onEggHitPlayer(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Egg && event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            Egg egg = (Egg) event.getDamager();

            if(egg.hasMetadata("launched_egg")){
                if(player == egg.getShooter()) return;
                buildMap.put(player.getUniqueId(), getTime());
                player.sendMessage(StringUtil.color("&cYou've been struck by an anti-build egg."));

                new AntiBuildRunner(player, this).runTaskLaterAsynchronously(SimpleAbilities.getInstance(), getTime() * 20);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();

        if(buildMap.containsKey(player.getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();

        if(buildMap.containsKey(player.getUniqueId())){
            event.setCancelled(true);
        }
    }
}
