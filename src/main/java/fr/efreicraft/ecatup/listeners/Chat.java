package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

@SuppressWarnings("ALL")
public class Chat implements Listener {

    LuckPerms LP = Main.LP;

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        boolean coloriseText = LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join().getCachedData().getPermissionData().checkPermission("ecatup.chat.color").asBoolean();
        PreferenceCache.ChatChannel channelActuel = PreferenceCache.getChannel(event.getPlayer().getUniqueId());

        Component channelPrefix = Component.text(channelActuel == PreferenceCache.ChatChannel.SERVER ? "" : ("["+channelActuel+"] ")).color(NamedTextColor.YELLOW);
        Component msg = event.getPlayer().displayName()
                .append(Component.text(ChatColor.GRAY + ": "))
                .append(coloriseText ? Component.text(colorize((TextComponent) event.message())) : event.message())
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")));

        final String WHAT_TIME_IS_IT = " @ {" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "}";

        StringBuilder msgCopie = new StringBuilder(new String("["+channelActuel.toString()+"] ")).append(((TextComponent) msg).content());

        // Les messages Minecraft font au max 255 caractères, c'est pas très grave de les parcourir plusieurs fois.
        while (msgCopie.indexOf("§") != -1) msgCopie.delete(msgCopie.indexOf("§"), msgCopie.indexOf("§") + 2);

        switch (channelActuel) {
            case GLOBAL -> {
                Main.sendGlobalChat(msgCopie.toString(), channelPrefix, msg);
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
