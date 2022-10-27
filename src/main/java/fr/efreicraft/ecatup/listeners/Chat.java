package fr.efreicraft.ecatup.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import fr.efreicraft.ecatup.Main;
import fr.efreicraft.ecatup.utils.DiscordWebhook;
import fr.efreicraft.ecatup.PreferenceCache;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

@SuppressWarnings("ALL")
public class Chat implements Listener, PluginMessageListener {


    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (!subchannel.equals("ecatup:globalchat")) return;

        in.readShort();

        String msg = "§e" + in.readUTF();
        Bukkit.getLogger().severe(msg);

        StringBuilder msgCopie = new StringBuilder(msg);
        while (msgCopie.indexOf("§") != -1) msgCopie.delete(msgCopie.indexOf("§"), msgCopie.indexOf("§") + 2);

        final String WHAT_TIME_IS_IT = " @ {" + LocalDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "UTC}";

        Component component = Component.text(colorize(msg))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")))
                .clickEvent(ClickEvent.copyToClipboard(msgCopie + WHAT_TIME_IS_IT));

        Bukkit.broadcast(component);
    }

    LuckPerms LP = Main.LP;

    @EventHandler
    public void onChat(AsyncChatEvent event) throws IOException {
        event.setCancelled(true);

        boolean coloriseText = LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join().getCachedData().getPermissionData().checkPermission("ecatup.chat.color").asBoolean();

        PreferenceCache.ChatChannel channelActuel = PreferenceCache.getChannel(event.getPlayer().getUniqueId());

        Component channelPrefix = Component.text(channelActuel == PreferenceCache.ChatChannel.SERVER ? "" : ("["+channelActuel+"] ")).color(NamedTextColor.YELLOW);
        Component msg = event.getPlayer().displayName()
                .append(Component.text(ChatColor.GRAY + ": "))
                .append(coloriseText ? Component.text(colorize((TextComponent) event.message())) : event.message())
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")));

        final String WHAT_TIME_IS_IT = " @ {" + LocalDateTime.now(Clock.systemUTC()).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "UTC}";

        // LORE: msgCopie c'est ce que tu obtiens avec le clic gauche. msgGlobal c'est msgCopie mais AVEC les couleurs
        // de sorte que je puisse le transmettre facilement sur d'autres serveurs.
        StringBuilder msgCopie = new StringBuilder(new String("["+channelActuel.toString()+"] ")).append(((TextComponent) msg).content());
        StringBuilder msgGlobal = new StringBuilder(new String("["+channelActuel.toString()+"] ")).append(((TextComponent) msg).content());

        msgGlobal = msgGlobal.append("§7: §r").append(coloriseText ? colorize(((TextComponent) event.message()).content()) : ((TextComponent) event.message()).content());

        // Les messages Minecraft font au max 255 caractères, c'est pas très grave de les parcourir plusieurs fois.
        while (msgCopie.indexOf("§") != -1) msgCopie.delete(msgCopie.indexOf("§"), msgCopie.indexOf("§") + 2);

        Bukkit.broadcast(msg
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")))
                .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent)event.message()).content() + WHAT_TIME_IS_IT)));

        // Send log to Discord
        DiscordWebhook webhook = new DiscordWebhook(Main.config.getString("webhook"));
        String playerName = event.getPlayer().getName();
        String message = ((TextComponent)event.message()).content();
        webhook.setContent("");
        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Message")
                .setDescription("**" + playerName + "** : " + message)
                .setColor(Color.decode("#3498db"))
                .setFooter("Efrei Craft", "https://efreicraft.fr/img/favicon.png")
        );
        webhook.execute();
        switch (channelActuel) {
            case GLOBAL -> {
                Main.sendGlobalChat(msgGlobal.toString(), event.getPlayer());
                Bukkit.broadcast(Component.join(JoinConfiguration.noSeparators(),channelPrefix,msg
                        .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent)event.message()).content() + WHAT_TIME_IS_IT))));
            }
            case SERVER -> Bukkit.broadcast(Component.join(JoinConfiguration.noSeparators(), channelPrefix, msg
                    .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent)event.message()).content() + WHAT_TIME_IS_IT))));
            case TEAM -> {
                if (event.getPlayer().getScoreboardTags().isEmpty()) {
                    Bukkit.broadcast(Component.join(JoinConfiguration.noSeparators(),channelPrefix,msg
                            .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent)event.message()).content() + WHAT_TIME_IS_IT))));
                } else {
                    Team team = Bukkit.getScoreboardManager().getMainScoreboard().getPlayerTeam(event.getPlayer());

                    if (team == null) {
                        event.getPlayer().sendMessage(msg
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")))
                                .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent) event.message()).content() + WHAT_TIME_IS_IT)));
                        return;
                    }

                    for (String playerName : team.getEntries()) {
                        if (Bukkit.getPlayer(playerName) != null) Bukkit.getPlayer(playerName).sendMessage(Component.join(JoinConfiguration.noSeparators(),channelPrefix, msg
                                .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent) event.message()).content() + WHAT_TIME_IS_IT))));
                    }
                }
            }
        }

    }
}
