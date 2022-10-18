package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Chat implements Listener {

    LuckPerms LP = Main.LP;

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        boolean coloriseText = LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join().getCachedData().getPermissionData().checkPermission("ecatup.chat.speakcolor").asBoolean();
        Component msg = event.getPlayer().displayName()
                .append(Component.text(ChatColor.GRAY + ": "))
                .append(coloriseText ? Component.text(colorize((TextComponent) event.message())) : event.message());

        Bukkit.broadcast(msg
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")))
                .clickEvent(ClickEvent.copyToClipboard(((TextComponent) msg).content())));
    }
}
