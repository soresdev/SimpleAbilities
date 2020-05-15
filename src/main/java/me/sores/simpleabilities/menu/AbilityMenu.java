package me.sores.simpleabilities.menu;

import me.sores.simpleabilities.ability.AbilityManager;
import me.sores.simpleabilities.config.AbilitiesConfig;
import me.sores.spark.util.ItemBuilder;
import me.sores.spark.util.StringUtil;
import me.sores.spark.util.menu.BasicMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sores on 4/8/2020.
 */
public class AbilityMenu extends BasicMenu {

    public AbilityMenu(Player holder) {
        super(holder);
        setup();
    }

    @Override
    public int getSize() {
        return 27;
    }

    @Override
    public String getTitle() {
        return StringUtil.color(AbilitiesConfig.MENU_TITLE);
    }

    @Override
    public void setup() {
        ItemStack glass = new ItemBuilder(Material.STAINED_GLASS_PANE).setData((short) 8).setName(" ").build();

        List<String> lore = new ArrayList<>();
        lore.add("&8&m------------------------------------------------");
        lore.add("&7- Click on an ability to enable/disable it.");
        lore.add(" ");
        lore.add("&7- Total Abilities: &e" + AbilityManager.getInstance().getAbilities().size());
        lore.add("&7- Active Cooldowns: &e" + AbilityManager.getInstance().getAbilityHandler().size());
        lore.add("&8&m------------------------------------------------");
        ItemStack info = new ItemBuilder(Material.PAPER).setName(StringUtil.color("&6Menu Info")).setLore(StringUtil.color(lore)).build();

        for(int i = getSize() - 9; i < getSize(); i++){
            toCreate.setItem(i, glass);
        }

        toCreate.setItem(22, info);

        AbilityManager.getInstance().getAbilities().forEach(ability -> {
            ItemStack clone = ability.getIcon().clone();
            ItemMeta meta = ability.getIcon().getItemMeta();
            meta.setDisplayName(ability.isEnabled() ? StringUtil.color("&a" + ability.getDisplay()) : StringUtil.color("&c" + ability.getDisplay()));

            clone.setItemMeta(meta);
            toCreate.addItem(clone);
        });
    }

    @Override
    public void setup(Player player) {

    }

    @Override
    public void open(Player player) {
        player.openInventory(toCreate);
    }
}
