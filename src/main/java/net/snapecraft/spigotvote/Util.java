package net.snapecraft.spigotvote;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {
    public static void buyItem(String material, int price, Player p) {
        VoteObject vote = SpigotVote.getInstance().getVotes(p);
        int newmoney = vote.getCoins() - price;
        SpigotVote.getInstance().setVoteCoins(p, newmoney);
        p.getInventory().addItem(new ItemStack(Material.getMaterial(material)));
    }
}
