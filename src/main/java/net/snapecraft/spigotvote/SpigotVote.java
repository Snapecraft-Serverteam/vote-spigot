package net.snapecraft.spigotvote;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

public final class SpigotVote extends JavaPlugin implements PluginMessageListener {

    @Override
    public void onEnable() {
        getServer().getMessenger().registerIncomingPluginChannel(this, "vote:votechannel", this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(channel.equalsIgnoreCase("vote:votechannel")) {
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String subChannel = in.readUTF();
            if(subChannel.equalsIgnoreCase("VoteAlert")) {
                String name = in.readUTF();
                System.out.println(name + " hat gevoted");
                Player target = Bukkit.getPlayer(name);
                sucessVote(target);
            }
        }
    }

    private void sucessVote(Player target) {
    }
}
