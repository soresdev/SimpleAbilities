package me.sores.simpleabilities.config;

import me.sores.simpleabilities.SimpleAbilities;
import me.sores.spark.util.configuration.ConfigFile;

/**
 * Created by sores on 3/30/2020.
 */
public class AbilitiesLang {

    public static String NO_PERMISSION = "";
    public static String NO_LONGER_ON_COOLDOWN = "";
    public static String COOLDOWN_MESSAGE = "";
    public static String NO_PLAYER_FOUND = "";
    public static String ABILITY_NOT_FOUND = "";
    public static String ABILITY_DISABLED = "";
    public static String COOLDOWN_REMOVED = "";
    public static String NOT_ON_COOLDOWN = "";
    public static String ABILITY_GIVEN = "";
    public static String ABILITY_GIVEN_ALL = "";

    public AbilitiesLang() {
        ConfigFile lang = new ConfigFile("lang.yml", SimpleAbilities.getInstance());

        NO_PERMISSION = lang.getString("NO_PERMISSION");
        NO_LONGER_ON_COOLDOWN = lang.getString("NO_LONGER_ON_COOLDOWN");
        COOLDOWN_MESSAGE = lang.getString("COOLDOWN_MESSAGE");
        NO_PLAYER_FOUND = lang.getString("NO_PLAYER_FOUND");
        ABILITY_NOT_FOUND = lang.getString("ABILITY_NOT_FOUND");
        ABILITY_DISABLED = lang.getString("ABILITY_DISABLED");
        COOLDOWN_REMOVED = lang.getString("COOLDOWN_REMOVED");
        NOT_ON_COOLDOWN = lang.getString("NOT_ON_COOLDOWN");
        ABILITY_GIVEN = lang.getString("ABILITY_GIVEN");
        ABILITY_GIVEN_ALL = lang.getString("ABILITY_GIVEN_ALL");
    }
}
