package me.sores.simpleabilities.ability.impl;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sores on 4/1/2020.
 */
public class Ability_smeltingshovel extends Ability {

    public Ability_smeltingshovel() {
        register();
    }

    @Override
    public String getName() {
        return "SMELTING_SHOVEL";
    }

    @Override
    public String getDisplay() {
        return "Smelting Shovel";
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
        StringUtil.color(lore);

        return new ItemBuilder(Material.DIAMOND_SPADE).setName(getDisplay()).setLore(StringUtil.color(lore)).build();
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(Material.DIAMOND_SPADE).setName("&bSmelting Shovel").build();
    }

    @Override
    public boolean isEnabled() {
        return AbilitiesConfig.SMELTING_SHOVEL_ENABLED;
    }

    @Override
    public int getCooldown() {
        return -1;
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
                StringUtil.color("&eDisplay: &r" + getDisplay()),
                StringUtil.color("&8&m------------------------------------------------"),
        };
    }

    private String[] abilityUsage = {
            StringUtil.color("&8&m------------------------------------------------"),
            StringUtil.color("&6" + getDisplay() + " Ability's settings: "),
            StringUtil.color("&e - toggle &f- Enable/Disable this ability."),
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

            default:{
                sender.sendMessage(abilityUsage);
            }
        }
        return false;
    }

    @EventHandler
    public void onSandBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInHand();

        if(block.getType() == Material.SAND){
            if(item != null && item.isSimilar(getItem().clone())){
                if(!isEnabled()){
                    player.sendMessage(StringUtil.color(AbilitiesLang.ABILITY_DISABLED));
                    event.setCancelled(true);
                    return;
                }

                event.setCancelled(true);
                block.setType(Material.AIR);
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GLASS));
            }
        }
    }

}
