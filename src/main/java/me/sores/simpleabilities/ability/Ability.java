package me.sores.simpleabilities.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Created by sores on 3/30/2020.
 */
public abstract class Ability implements Listener, IAbility {

    public abstract String getName();
    public abstract String getDisplay();
    public abstract ItemStack getIcon();
    public abstract ItemStack getItem();

    public abstract boolean isEnabled();
    public abstract int getCooldown();
    public abstract void register();

    public void perform(Player player, Ability ability){
        AbilityManager.getInstance().startTimer(player, ability);
    }

    public boolean canPerform(Player player){
        return !AbilityManager.getInstance().isOnCooldown(player) || !AbilityManager.getInstance().getAbilityHandler().containsKey(player.getUniqueId());
    }
}
