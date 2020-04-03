package me.sores.simpleabilities;

import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.ability.impl.*;
import me.sores.simpleabilities.commands.Command_ability;
import me.sores.simpleabilities.commands.Command_abilityreload;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.simpleabilities.config.AbilitiesLang;
import me.sores.spark.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by sores on 3/30/2020.
 */
public class SimpleAbilities extends JavaPlugin {

    private static SimpleAbilities instance;
    private AbilityManager abilityManager;

    @Override
    public void onEnable() {
        instance = this;

        if(Bukkit.getPluginManager().getPlugin("Spark") == null){
            Bukkit.getPluginManager().disablePlugin(this);
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[SimpleAbilities] Disabling...");
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "[SimpleAbilities] SimpleAbilities cannot be enabled while missing Spark depend.");
            return;
        }

        initConfigs();
        initClasses();

        loadCommands();
        loadListeners();

        loadAbilities();
        StringUtil.log("&a[SimpleAbilities] SimpleAbilities has successfully loaded.");
    }

    @Override
    public void onDisable() {
        abilityManager.unload();
        instance = null;
    }

    private void loadCommands(){
        new Command_ability(this);
        new Command_abilityreload(this);
    }

    private void loadListeners(){

    }

    private void initClasses(){
        abilityManager = new AbilityManager();
    }

    private void initConfigs(){
        new AbilitiesConfig();
        new AbilitiesLang();
    }

    private void loadAbilities(){
        new Ability_cocaine();
        new Ability_smeltingshovel();
        new Ability_antibuild();
        new Ability_switcher();
        new Ability_armorswap();
        new Ability_lightningrod();
        new Ability_fallprotection();
        new Ability_recall();
    }

    public static SimpleAbilities getInstance() {
        return instance;
    }
}
