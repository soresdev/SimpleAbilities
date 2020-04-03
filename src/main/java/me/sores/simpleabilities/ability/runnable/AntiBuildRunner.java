package me.sores.simpleabilities.ability.runnable;

import me.sores.simpleabilities.ability.Ability;
import me.sores.simpleabilities.ability.impl.Ability_antibuild;
import me.sores.simpleabilities.ability.impl.Ability_cocaine;
import me.sores.spark.util.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/1/2020.
 */
public class AntiBuildRunner extends BukkitRunnable {

    private Player player;
    private Ability ability;


    public AntiBuildRunner(Player player, Ability ability) {
        this.player = player;
        this.ability = ability;
    }

    @Override
    public void run() {
        cancel();
        Ability_antibuild antibuild = (Ability_antibuild) ability;

        if(player.isDead() || player == null){
            super.cancel();
            antibuild.getBuildMap().remove(player.getUniqueId());
            return;
        }

        antibuild.getBuildMap().remove(player.getUniqueId());
        player.sendMessage(StringUtil.color("&aYou can now build again."));
    }
}
