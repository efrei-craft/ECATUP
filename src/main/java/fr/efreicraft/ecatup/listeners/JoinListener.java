package fr.efreicraft.ecatup.listeners;

import fr.efreicraft.animus.endpoints.PlayerService;
import fr.efreicraft.animus.invoker.ApiException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static fr.efreicraft.ecatup.ECATUP.INSTANCE;

public class JoinListener implements Listener {

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        try {
            PlayerService.justConnected(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
        }
        catch (ApiException e) {
            if (e.getCode() == 400) {
                event.getPlayer().kick(Component.text("Vous êtes banni d'Efrei Craft!").color(NamedTextColor.RED));
                //TODO get le bannissement de la BDD pour afficher les détails
                return;
            }
            INSTANCE.getLogger().severe("Couldn't get " + event.getPlayer().getName() + "'s info...");
            makeTraceFile(e, event.getPlayer());

            event.getPlayer().kick(Component.text("Oups ! Je n'arrive pas à accéder à vos infos joueurs.").color(NamedTextColor.RED));
        }
    }

    public static void makeTraceFile(ApiException e, org.bukkit.entity.Player player) {
        INSTANCE.getLogger().severe("Attempting to write a trace file.");
        String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Date.from(Instant.now(Clock.systemUTC())));

        try {
            File errFolder = new File(INSTANCE.getDataFolder().getAbsolutePath() + "\\errorLogs\\");
            if (!errFolder.exists())
                if (!errFolder.mkdirs())
                    throw new IOException();

            File traceFile = new File(errFolder, player.getName() + date + ".log");
            if (!traceFile.createNewFile()) throw new IOException("Failed to create new trace file");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(traceFile, StandardCharsets.UTF_8))){
                writer.append(player.getName()).append(" failed to get his info.");
                writer.append("\n");
                writer.append(e.getMessage() != null ? ("Exception message:\n\t" + e.getMessage()) : "No exception message given.");
                writer.append("\n");
                writer.append("Response code: ").append(String.valueOf(e.getCode())).append("\t\t(if 0, ignore it, it wasn't a HTTP error)");
                writer.append("\n");
                if (e.getResponseHeaders() != null) {
                    writer.append("Response headers: \n");
                    for (Map.Entry<String, List<String>> entry : e.getResponseHeaders().entrySet()) {
                        writer.append("\t");
                        writer.append(entry.getKey()).append(": ").append(String.join(";", entry.getValue()));
                        writer.append("\n");
                    }
                } else {
                    writer.append("No response headers. Maybe no request was made at all?");
                    writer.append("\n");
                }
                writer.append(e.getResponseBody() != null ? ("Response body:\n\t" + e.getMessage()) : "No body in the response.");
            }
        } catch (IOException ex) {
            INSTANCE.getLogger().severe("Damn it! Couldn't write a trace file...");
            throw new RuntimeException(ex);
        }
    }
}
