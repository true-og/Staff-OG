package uk.hotten.staffog.punish;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.punish.data.ChatReportEntry;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.TimeUtils;

public class PunishEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        PunishManager.getInstance().checkNameToUuid(event.getPlayer().getName(), event.getPlayer().getUniqueId());

    }

    @EventHandler
    public void onAsyncPreJoin(AsyncPlayerPreLoginEvent event) {

        final PunishEntry entry = PunishManager.getInstance().checkActivePunishment(PunishType.BAN,
                event.getUniqueId());

        if (entry == null) {

            return;

        }

        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);

        final String messageString = "&cYou have been banned for: \n" + "&f" + entry.getReason() + "\n\n" + "&c"
                + "Your ban " + (entry.getUntil() == -1 ? "does not expire."
                        : "will expire in:\n" + "&f" + TimeUtils.formatMillisecondTime(entry.calculateRemaining()));

        // Create a MiniMessage instance.
        final MiniMessage miniMessage = MiniMessage.miniMessage();
        // Deserialize the string into a TextComponent.å
        final Component kickMessage = miniMessage.deserialize(messageString);

        event.kickMessage(kickMessage);

    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {

        final PunishEntry entry = PunishManager.getInstance().checkActivePunishment(PunishType.MUTE,
                event.getPlayer().getUniqueId());

        if (entry != null) {

            event.setCancelled(true);
            UtilitiesOG.trueogMessage(event.getPlayer(), "&c" + "You have been muted for " + "&6" + entry.getReason());
            UtilitiesOG.trueogMessage(event.getPlayer(),
                    "&c" + "Your mute " + (entry.getUntil() == -1 ? "does not expire."
                            : "will expire in: " + "&f" + TimeUtils.formatMillisecondTime(entry.calculateRemaining())));

        } else {

            return;

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChatReport(AsyncChatEvent event) {

        if (event.isCancelled()) {

            return;

        }

        final String eventMessage = MiniMessage.miniMessage().serialize(event.message());

        final PunishManager pm = PunishManager.getInstance();
        final ChatReportEntry entry = new ChatReportEntry(event.getPlayer().getUniqueId(), event.getPlayer().getName(),
                eventMessage, System.currentTimeMillis());

        pm.getChatReportEntries().add(entry);
        Bukkit.getServer().getScheduler().runTaskLater(pm.getPlugin(), () -> pm.getChatReportEntries().remove(entry),
                2400); // remove 2m later

    }

}