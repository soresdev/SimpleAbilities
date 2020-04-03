package me.sores.simpleabilities.ability.runnable;

import me.sores.simpleabilities.ability.Ability;
import me.sores.spark.util.InventoryUtil;
import me.sores.spark.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/2/2020.
 */
public class ArmorSwapRunner extends BukkitRunnable {

    private Player player;
    private Ability ability;
    private ItemStack item;

    public ArmorSwapRunner(Player player, Ability ability, ItemStack item) {
        this.player = player;
        this.ability = ability;
        this.item = item;
    }

    @Override
    public void run() {
        cancel();

        if(player.isDead() || player == null){
            super.cancel();
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            return;
        }

        if(player.getInventory().getHelmet() == null || player.getInventory().getHelmet().getType() == Material.AIR){
            player.getInventory().setHelmet(item);
        }else{
            InventoryUtil.giveItemSafely(player, item);
            player.sendMessage(StringUtil.color("&aYour helmet was returned to your inventory."));
            return;
        }

        player.sendMessage(StringUtil.color("&aYour helmet has been returned."));
    }
}
