package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.ecatup.Main;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Join implements Listener {
    LuckPerms LP = Main.LP;
    @EventHandler
    public void onJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        if (!event.getPlayer().hasPermission("server." + Main.config.getString("server_name"))) {
            event.getPlayer().kick(Component.text("§cVous n'avez pas la permission d'accéder ce serveur !"));
            event.joinMessage(null);
            return;
        }
        String prefix = LP.getUserManager().getUser(event.getPlayer().getUniqueId()).getCachedData().getMetaData().getPrefix().replaceAll("&", "§");
        event.getPlayer().displayName(Component.text(prefix + event.getPlayer().getName()));
        event.joinMessage(event.getPlayer().displayName().append(Component.text("§7 a rejoint le serveur !")));
        event.getPlayer().playerListName(event.getPlayer().displayName());
    }
}
