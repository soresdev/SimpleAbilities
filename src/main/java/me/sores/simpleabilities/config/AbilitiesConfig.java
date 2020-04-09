package me.sores.simpleabilities.config;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.spark.util.configuration.ConfigFile;

/**
 * Created by sores on 3/30/2020.
 */
public class AbilitiesConfig {

    public static String MENU_TITLE;

    public static int COCAINE_COOLDOWN;
    public static boolean COCAINE_ENABLED;
    public static int COCAINE_SPEED_LEVEL;
    public static int COCAINE_SPEED_TIME;
    public static boolean ANTI_BUILD_ENABLED;
    public static int ANTI_BUILD_COOLDOWN;
    public static int ANTI_BUILD_TIME;
    public static boolean SMELTING_SHOVEL_ENABLED;
    public static boolean SWITCHER_ENABLED;
    public static int SWITCHER_COOLDOWN;
    public static boolean ARMOR_SWAP_ENABLED;
    public static int ARMOR_SWAP_COOLDOWN;
    public static int ARMOR_SWAP_TIME;
    public static boolean ARMOR_SWAP_POTION_ENABLED;
    public static boolean LIGHTNING_ROD_ENABLED;
    public static int LIGHTNING_ROD_COOLDOWN;
    public static boolean FALL_PROTECTION_ENABLED;
    public static int FALL_PROTECTION_COOLDOWN;
    public static boolean RECALL_ENABLED;
    public static int RECALL_COOLDOWN;
    public static int RECALL_RANGE;
    public static boolean RECALL_POTION_EFFECT;

    public AbilitiesConfig() {
        ConfigFile config = new ConfigFile("config.yml", SimpleAbilities.getInstance());
        String path = "abilities.";

        MENU_TITLE = config.getString("menu.title");
        COCAINE_COOLDOWN = config.getInt(path + "cocaine.cooldown");
        COCAINE_ENABLED = config.getBoolean(path + "cocaine.enabled");
        COCAINE_SPEED_LEVEL = config.getInt(path + "cocaine.speed_level");
        COCAINE_SPEED_TIME = config.getInt(path + "cocaine.speed_time");
        ANTI_BUILD_ENABLED = config.getBoolean(path + "anti_build.enabled");
        ANTI_BUILD_COOLDOWN = config.getInt(path + "anti_build.cooldown");
        ANTI_BUILD_TIME = config.getInt(path + "anti_build.time");
        SMELTING_SHOVEL_ENABLED = config.getBoolean(path + "smelting_shovel.enabled");
        SWITCHER_ENABLED = config.getBoolean(path + "switcher.enabled");
        SWITCHER_COOLDOWN = config.getInt(path + "switcher.cooldown");
        ARMOR_SWAP_ENABLED = config.getBoolean(path + "armor_swap.enabled");
        ARMOR_SWAP_COOLDOWN = config.getInt(path + "armor_swap.cooldown");
        ARMOR_SWAP_TIME = config.getInt(path + "armor_swap.time");
        ARMOR_SWAP_POTION_ENABLED = config.getBoolean(path + "armor_swap.potion_effect");
        LIGHTNING_ROD_ENABLED = config.getBoolean(path + "lightning_rod.enabled");
        LIGHTNING_ROD_COOLDOWN = config.getInt(path + "lightning_rod.cooldown");
        FALL_PROTECTION_ENABLED = config.getBoolean(path + "fall_protection.enabled");
        FALL_PROTECTION_COOLDOWN = config.getInt(path + "fall_protection.cooldown");
        RECALL_ENABLED = config.getBoolean(path + "recall.enabled");
        RECALL_COOLDOWN = config.getInt(path + "recall.cooldown");
        RECALL_RANGE = config.getInt(path + "recall.range");
        RECALL_POTION_EFFECT = config.getBoolean(path + "recall.potion_effect");

    }

    public static void reload(){
        destroy();
        new AbilitiesConfig();
    }

    private static void destroy(){
        //destroy things needed here
    }
}
