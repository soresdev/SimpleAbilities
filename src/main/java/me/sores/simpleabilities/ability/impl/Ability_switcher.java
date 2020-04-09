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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sores on 4/2/2020.
 */
public class Ability_switcher extends Ability {

    public Ability_switcher() {
        register();
    }

    @Override
    public String getName() {
        return "SWITCHER";
    }

    @Override
    public String getDisplay() {
        return "Switcher";
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
        lore.add("&8&m------------------------------------------------");

        return new ItemBuilder(Material.SNOW_BALL).setName(getDisplay()).setLore(StringUtil.color(lore)).build();
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.SNOW_BALL).setName("&bSwitcher Ability").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.SWITCHER_ENABLED;
    }

    @Override
    public int getCooldown() {
        return AbilitiesConfig.SWITCHER_COOLDOWN;
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

            default:{
                sender.sendMessage(abilityUsage);
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

                event.setCancelled(true);
                Snowball snowball = player.launchProjectile(Snowball.class);
                snowball.setMetadata("launched_snowball", new FixedMetadataValue(SimpleAbilities.getInstance(), "launched_snowball"));

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
    public void onEggHitPlayer(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Snowball && event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            Snowball snowball = (Snowball) event.getDamager();

            if(snowball.getShooter() instanceof Player && snowball.hasMetadata("launched_snowball")){
                Player shooter = (Player) snowball.getShooter();

                player.damage(0);
                Location location = player.getLocation();

                player.teleport(shooter.getLocation(), PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                shooter.teleport(location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                player.sendMessage(StringUtil.color("&cYou have been switched by " + shooter.getName() + "."));
            }
        }
    }

}
