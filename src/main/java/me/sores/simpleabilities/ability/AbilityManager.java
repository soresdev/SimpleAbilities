package me.sores.simpleabilities.ability;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by sores on 3/30/2020.
 */
public class AbilityManager {

    private static AbilityManager instance;
    private List<Ability> abilities;

    private HashMap<UUID, Integer> abilityHandler;
    private HashMap<UUID, Integer> abilityScheduler;

    public AbilityManager() {
        instance = this;
        abilities = new ArrayList<>();

        abilityHandler = new HashMap<>();
        abilityScheduler = new HashMap<>();
    }

    public void startTimer(Player player, Ability ability){
        abilityHandler.put(player.getUniqueId(), ability.getCooldown());
        abilityScheduler.put(player.getUniqueId(), Bukkit.getScheduler().scheduleAsyncRepeatingTask(SimpleAbilities.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(player.isDead()){
                    cancelTask(player);
                    return;
                }

                if(isOnCooldown(player) && getRemainingTime(player) <= 1){
                    remove(player);
                    MessageUtil.sendNoLongerOnCooldownMessage(player, ability);
                }else{
                    abilityHandler.replace(player.getUniqueId(), getRemainingTime(player), getRemainingTime(player) - 1);
                }
            }
        }, 0L, 20L));
    }

    public boolean isOnCooldown(Player player){
        return abilityHandler.containsKey(player.getUniqueId());
    }

    public int getRemainingTime(Player player){
        if(!isOnCooldown(player))return -1;

        return abilityHandler.get(player.getUniqueId());
    }

    public void remove(Player player){
        abilityHandler.remove(player.getUniqueId());

        if(abilityScheduler.containsKey(player.getUniqueId())) cancelTask(player);
    }

    private void cancelTask(Player player){
        if(abilityScheduler.containsKey(player.getUniqueId())){
            Bukkit.getScheduler().cancelTask(abilityScheduler.get(player.getUniqueId()));
        }

        abilityScheduler.remove(player.getUniqueId());
        abilityHandler.remove(player.getUniqueId());
    }

    public void registerAbility(Ability ability){
        abilities.add(ability);
    }

    public void enableAbility(Ability ability){
        makeConfigChange("abilities." + ability.getName().toLowerCase() + ".enabled", true);
    }

    public void disableAbility(Ability ability){
        makeConfigChange("abilities." + ability.getName().toLowerCase() + ".enabled", false);
    }

    public void makeConfigChange(String path, Object obj){
        SimpleAbilities.getInstance().getConfig().set(path, obj);
        SimpleAbilities.getInstance().saveConfig();

        AbilitiesConfig.reload();
    }

    public void unload(){
        abilities.clear();
    }

    public Ability valueOf(String name){
        for(Ability ability : abilities){
            if(ability.getName().equalsIgnoreCase(name)) return ability;
        }

        return null;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public HashMap<UUID, Integer> getAbilityHandler() {
        return abilityHandler;
    }

    public static AbilityManager getInstance() {
        return instance;
    }
}
