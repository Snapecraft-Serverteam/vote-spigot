package net.snapecraft.spigotvote;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Gui implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("shopinventory")
            .provider(new Gui())
            .size(SpigotVote.getInstance().getConfig().getInt("Shopconfig.size") + 1, 9)
            .title(ChatColor.YELLOW + "Votecoin shop")
            .closeable(true)
            .build();

    public static void openshop(Player p) {
        INVENTORY.open(p);
    }

    @Override
    public void init(Player player, InventoryContents inv) {

        VoteObject vote = SpigotVote.getInstance().getVotes(player);
        if(vote == null) {
            inv.setProperty("coins", 0);
        } else {
            inv.setProperty("coins", vote.getCoins());
        }



        // Top Row
        ItemStack glasspane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        inv.fillRow(0, ClickableItem.empty(glasspane));

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierdata = barrier.getItemMeta();
        barrierdata.setDisplayName("§cClose");
        barrier.setItemMeta(barrierdata);
        inv.set(0, 6, ClickableItem.of(barrier, e -> inv.inventory().close(player)));


        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemMeta golddata = gold.getItemMeta();
        if(vote != null) {
            golddata.setDisplayName("§aDu hast §6" + vote.getCoins() + " §aVotecoins");
        } else {
            golddata.setDisplayName("§aDu hast §60§aVotecoins");
        }

        gold.setItemMeta(golddata);
        inv.set(0, 2, ClickableItem.empty(gold));



        // Selling Row

        ConfigurationSection shop = SpigotVote.getInstance().getConfig().getConfigurationSection("Shop");
        Set<String> keys = shop.getKeys(false);
        for (String key : keys) {
            ConfigurationSection shopitem = shop.getConfigurationSection(key);
            String name = shopitem.getString("name");
            String material = shopitem.getString("material");
            int price = shopitem.getInt("price");

            ItemStack item = new ItemStack(Material.getMaterial(material));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§a" + name);
            List<String> lore = new ArrayList<>();
            lore.add("§3Preis§7: §6" + price + " §aVotecoins");
            meta.setLore(lore);
            item.setItemMeta(meta);

            inv.add(ClickableItem.of(item, e -> {
                if((int)inv.property("coins") > price) {
                    Util.buyItem(material, price, player);
                    inv.setProperty("coins", ((int)inv.property("coins") - price));
                } else {
                    player.sendMessage("§cDu hast nicht genug Votecoins!");
                }
            }));

        }
    }

    @Override
    public void update(Player player, InventoryContents inv) {


        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemMeta golddata = gold.getItemMeta();
        golddata.setDisplayName("§aDu hast §6" + inv.property("coins") + " §aVotecoins");
        gold.setItemMeta(golddata);
        inv.set(0, 4, ClickableItem.empty(gold));

    }
}
