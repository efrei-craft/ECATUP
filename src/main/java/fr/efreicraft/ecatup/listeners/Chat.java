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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static fr.efreicraft.ecatup.utils.Msg.colorize;

public class Chat implements Listener {

    LuckPerms LP = Main.LP;

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.setCancelled(true);

        boolean coloriseText = LP.getUserManager().loadUser(event.getPlayer().getUniqueId()).join().getCachedData().getPermissionData().checkPermission("ecatup.chat.color").asBoolean();
        boolean global = ((TextComponent) event.message()).content().toCharArray()[0] == '@';

        Component msg = event.getPlayer().displayName()
                .append(Component.text(ChatColor.GRAY + ": "))
                .append(coloriseText ? Component.text(colorize((TextComponent) event.message())) : event.message());
        final String WHAT_TIME_IS_IT = " @ {" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) + "}";

        StringBuilder msgCopie = new StringBuilder( ( (TextComponent) (msg.append(Component.text(ChatColor.GRAY + ": ")).append(event.message())) ).content() );

        // Les messages Minecraft font au max 255 caractères, c'est pas très grave de les parcourir plusieurs fois.
        while (msgCopie.indexOf("§") != -1) msgCopie.delete(msgCopie.indexOf("§"), msgCopie.indexOf("§") + 2);

        Bukkit.broadcast(msg
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Copier")))
                .clickEvent(ClickEvent.copyToClipboard(msgCopie + ": " + ((TextComponent)event.message()).content() + WHAT_TIME_IS_IT)));
    }
}
