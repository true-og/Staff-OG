package uk.hotten.staffog.punish;

import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_BAN;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_CHATREPORT;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_HISTORY;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_KICK;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_MUTE;
import static uk.hotten.staffog.data.jooq.Tables.STAFFOG_REPORT;

import com.google.gson.Gson;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.UUID;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.data.jooq.tables.records.StaffogBanRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogChatreportRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogMuteRecord;
import uk.hotten.staffog.data.jooq.tables.records.StaffogReportRecord;
import uk.hotten.staffog.punish.data.ChatReportEntry;
import uk.hotten.staffog.punish.data.KickPunishEntry;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.utils.Console;
import uk.hotten.staffog.utils.Message;
import uk.hotten.staffog.utils.TimeUtils;

public class PunishManager {

    @Getter
    private JavaPlugin plugin;

    @Getter
    private static PunishManager instance;

    @Getter
    private ArrayList<ChatReportEntry> chatReportEntries;

    public PunishManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
        chatReportEntries = new ArrayList<>();

        plugin.getServer().getPluginManager().registerEvents(new PunishEventListener(), plugin);
    }

    public void checkNameToUuid(String name, UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection);

            boolean exists = dsl.fetchExists(dsl.select(STAFFOG_HISTORY.ID)
                    .from(STAFFOG_HISTORY)
                    .where(STAFFOG_HISTORY.UUID.eq(uuid.toString()))
                    .and(STAFFOG_HISTORY.NAME.eq(name)));

            if (exists) {
                return;
            }

            dsl.insertInto(STAFFOG_HISTORY)
                    .set(STAFFOG_HISTORY.UUID, uuid.toString())
                    .set(STAFFOG_HISTORY.NAME, name)
                    .execute();

        } catch (Exception error) {

            Console.error("Failed to check username to uuid.");
            error.printStackTrace();
        }
    }

    public String getNameFromUUID(UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection);

            return dsl.select(STAFFOG_HISTORY.NAME)
                    .from(STAFFOG_HISTORY)
                    .where(STAFFOG_HISTORY.UUID.eq(uuid.toString()))
                    .orderBy(STAFFOG_HISTORY.ID.desc())
                    .limit(1)
                    .fetchOne(STAFFOG_HISTORY.NAME);

        } catch (Exception error) {
            Console.error("Failed to get name from uuid. " + uuid.toString());
            error.printStackTrace();
            return null;
        }
    }

    public UUID getUUIDFromName(String name) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection);
            String uuidString = dsl.select(STAFFOG_HISTORY.UUID)
                    .from(STAFFOG_HISTORY)
                    .where(STAFFOG_HISTORY.NAME.eq(name))
                    .orderBy(STAFFOG_HISTORY.ID.desc())
                    .limit(1)
                    .fetchOne(STAFFOG_HISTORY.UUID);

            if (uuidString == null) return null;

            return UUID.fromString(uuidString);

        } catch (Exception error) {
            Console.error("Failed to get name from name. " + name);
            error.printStackTrace();
            return null;
        }
    }

    public void newPunishment(PunishEntry entry) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection);
            if (entry.getType() == PunishType.BAN) {
                dsl.insertInto(STAFFOG_BAN)
                        .set(STAFFOG_BAN.UUID, entry.getUuid().toString())
                        .set(STAFFOG_BAN.REASON, entry.getReason())
                        .set(STAFFOG_BAN.BY_UUID, entry.getByUuid())
                        .set(STAFFOG_BAN.BY_NAME, entry.getByName())
                        .set(STAFFOG_BAN.TIME, entry.getTime())
                        .set(STAFFOG_BAN.UNTIL, entry.getUntil())
                        .set(STAFFOG_BAN.ACTIVE, entry.isActive())
                        .execute();
            } else {
                dsl.insertInto(STAFFOG_MUTE)
                        .set(STAFFOG_MUTE.UUID, entry.getUuid().toString())
                        .set(STAFFOG_MUTE.REASON, entry.getReason())
                        .set(STAFFOG_MUTE.BY_UUID, entry.getByUuid())
                        .set(STAFFOG_MUTE.BY_NAME, entry.getByName())
                        .set(STAFFOG_MUTE.TIME, entry.getTime())
                        .set(STAFFOG_MUTE.UNTIL, entry.getUntil())
                        .set(STAFFOG_MUTE.ACTIVE, entry.isActive())
                        .execute();
            }

            String duration = TimeUtils.formatMillisecondTime(entry.calculateDuration());
            if (entry.getPlayer() != null && entry.getPlayer().isOnline()) {
                switch (entry.getType()) {
                    case BAN -> {
                        String messageString = ("&c" + "You have been banned for:\n" + "&f"
                                + entry.getReason() + "\n\n" + "&c"
                                + "Your ban "
                                + (entry.getUntil() == -1
                                        ? "does not expire."
                                        : "will expire in: \n" + "&f" + duration));

                        // Create a MiniMessage instance.
                        MiniMessage miniMessage = MiniMessage.miniMessage();
                        // Deserialize the string into a TextComponent.å
                        Component kickMessage = miniMessage.deserialize(messageString);

                        entry.getPlayer().kick(kickMessage);
                    }
                    case MUTE -> {
                        entry.getPlayer()
                                .sendMessage(Message.format("&cYou have been muted for &f" + entry.getReason()));
                        entry.getPlayer()
                                .sendMessage(Message.format("&cYour mute "
                                        + (entry.getUntil() == -1
                                                ? "does not expire."
                                                : "will expire in: \n" + "&f" + duration)));
                    }
                }
            }

            Message.staffBroadcast(Message.formatNotification(
                    entry.getType().toString(),
                    "New " + entry.getType().toString().toLowerCase()
                            + " on " + entry.getName() + " for "
                            + duration + " by " + entry.getByName()));

            SecurityManager.getInstance().checkAndDeactivateStaffAccount(entry.getUuid());

        } catch (Exception error) {
            Console.error("Failed to create punishment.");
            error.printStackTrace();
        }
    }

    public PunishEntry getPunishment(PunishType type, int id) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PunishEntry entry = new PunishEntry(type);
            DSLContext dsl = DSL.using(connection);
            if (type == PunishType.BAN) {
                StaffogBanRecord record = dsl.select(STAFFOG_BAN.asterisk())
                        .from(STAFFOG_BAN)
                        .where(STAFFOG_BAN.ID.eq((long) id))
                        .fetchOneInto(STAFFOG_BAN);

                if (record == null) {

                    return null;
                }

                entry.setId(record.getId());
                entry.setUuid(UUID.fromString(record.getUuid()));
                entry.setName(getNameFromUUID(entry.getUuid()));
                entry.setReason(record.getReason());
                entry.setByUuid(record.getByUuid());
                entry.setByName(record.getByName());
                entry.setTime(record.getTime());
                entry.setUntil(record.getUntil());
                entry.setActive(record.getActive());
                if (!entry.isActive()) {
                    entry.setRemovedUuid(record.getRemovedUuid());
                    entry.setRemovedName(record.getRemovedName());
                    entry.setRemovedTime(record.getRemovedTime());
                    entry.setRemovedReason(record.getRemovedReason());
                }
            } else {
                StaffogMuteRecord record = dsl.select(STAFFOG_MUTE.asterisk())
                        .from(STAFFOG_MUTE)
                        .where(STAFFOG_MUTE.ID.eq((long) id))
                        .fetchOneInto(STAFFOG_MUTE);

                if (record == null) return null;

                entry.setId(record.getId());
                entry.setUuid(UUID.fromString(record.getUuid()));
                entry.setName(getNameFromUUID(entry.getUuid()));
                entry.setReason(record.getReason());
                entry.setByUuid(record.getByUuid());
                entry.setByName(record.getByName());
                entry.setTime(record.getTime());
                entry.setUntil(record.getUntil());
                entry.setActive(record.getActive());
                if (!entry.isActive()) {
                    entry.setRemovedUuid(record.getRemovedUuid());
                    entry.setRemovedName(record.getRemovedName());
                    entry.setRemovedTime(record.getRemovedTime());
                    entry.setRemovedReason(record.getRemovedReason());
                }
            }

            if (entry.checkDurationOver()) {
                expirePunishment(entry);
                return null;
            }

            return entry;

        } catch (Exception error) {
            Console.error("Failed to get punishment info for id " + id);
            error.printStackTrace();
            return null;
        }
    }

    public PunishEntry checkActivePunishment(PunishType type, UUID uuid) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            PunishEntry entry = new PunishEntry(type);
            DSLContext dsl = DSL.using(connection);
            if (type == PunishType.BAN) {
                StaffogBanRecord record = dsl.select(STAFFOG_BAN.asterisk())
                        .from(STAFFOG_BAN)
                        .where(STAFFOG_BAN.UUID.eq(uuid.toString()))
                        .and(STAFFOG_BAN.ACTIVE.eq(true))
                        .fetchOneInto(STAFFOG_BAN);

                if (record == null) return null;

                entry.setId(record.getId());
                entry.setUuid(UUID.fromString(record.getUuid()));
                entry.setName(getNameFromUUID(entry.getUuid()));
                entry.setReason(record.getReason());
                entry.setByUuid(record.getByUuid());
                entry.setByName(record.getByName());
                entry.setTime(record.getTime());
                entry.setUntil(record.getUntil());
                entry.setActive(record.getActive());
            } else {
                StaffogMuteRecord record = dsl.select(STAFFOG_MUTE.asterisk())
                        .from(STAFFOG_MUTE)
                        .where(STAFFOG_MUTE.UUID.eq(uuid.toString()))
                        .and(STAFFOG_MUTE.ACTIVE.eq(true))
                        .fetchOneInto(STAFFOG_MUTE);

                if (record == null) return null;

                entry.setId(record.getId());
                entry.setUuid(UUID.fromString(record.getUuid()));
                entry.setName(getNameFromUUID(entry.getUuid()));
                entry.setReason(record.getReason());
                entry.setByUuid(record.getByUuid());
                entry.setByName(record.getByName());
                entry.setTime(record.getTime());
                entry.setUntil(record.getUntil());
                entry.setActive(record.getActive());
            }

            if (entry.checkDurationOver()) {
                expirePunishment(entry);
                return null;
            }

            return entry;

        } catch (Exception error) {
            Console.error("Failed to get punishment info for " + uuid);
            error.printStackTrace();
            return null;
        }
    }

    public void expirePunishment(PunishEntry entry) {
        entry.setRemovedUuid("Expired");
        entry.setRemovedName("Expired");
        entry.setRemovedReason("Punishment Expired");
        entry.setActive(false);
        removePunishment(entry);
    }

    public void removePunishment(PunishEntry entry) {
        entry.setActive(false);
        entry.setRemovedTime(System.currentTimeMillis());

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            if (entry.getName() == null) entry.setName(getNameFromUUID(entry.getUuid()));

            DSLContext dsl = DSL.using(connection);
            if (entry.getType() == PunishType.BAN) {
                dsl.update(STAFFOG_BAN)
                        .set(STAFFOG_BAN.REMOVED_UUID, entry.getRemovedUuid())
                        .set(STAFFOG_BAN.REMOVED_NAME, entry.getRemovedName())
                        .set(STAFFOG_BAN.REMOVED_REASON, entry.getRemovedReason())
                        .set(STAFFOG_BAN.REMOVED_TIME, entry.getRemovedTime())
                        .set(STAFFOG_BAN.ACTIVE, false)
                        .where(STAFFOG_BAN.ID.eq(entry.getId()))
                        .execute();
            } else {
                dsl.update(STAFFOG_MUTE)
                        .set(STAFFOG_MUTE.REMOVED_UUID, entry.getRemovedUuid())
                        .set(STAFFOG_MUTE.REMOVED_NAME, entry.getRemovedName())
                        .set(STAFFOG_MUTE.REMOVED_REASON, entry.getRemovedReason())
                        .set(STAFFOG_MUTE.REMOVED_TIME, entry.getRemovedTime())
                        .set(STAFFOG_MUTE.ACTIVE, false)
                        .where(STAFFOG_MUTE.ID.eq(entry.getId()))
                        .execute();
            }

            if (entry.getType() == PunishType.MUTE && Bukkit.getServer().getPlayer(entry.getUuid()) != null) {

                Bukkit.getServer().getPlayer(entry.getUuid()).sendMessage(Message.format("&aYou have been unmuted."));
            }

            Message.staffBroadcast(Message.formatNotification(
                    "UN" + entry.getType(),
                    entry.getName() + " has been un" + entry.getType().getBroadcastMessage() + " by "
                            + entry.getRemovedName()));

        } catch (Exception error) {

            Console.error("Failed to remove punishment.");
            error.printStackTrace();
        }
    }

    public void visualRemovePunishment(PunishEntry entry) {

        if (entry.getType() == PunishType.MUTE && Bukkit.getServer().getPlayer(entry.getUuid()) != null) {

            Bukkit.getServer().getPlayer(entry.getUuid()).sendMessage(Message.format("&aYou have been unmuted."));
        }

        Message.staffBroadcast(Message.formatNotification(
                "UN" + entry.getType(),
                entry.getName() + " has been un" + entry.getType().getBroadcastMessage() + " by "
                        + entry.getRemovedName()));
    }

    public void newKickPunishment(KickPunishEntry entry) {

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {
            DSLContext dsl = DSL.using(connection);
            dsl.insertInto(STAFFOG_KICK)
                    .set(STAFFOG_KICK.UUID, entry.getUuid().toString())
                    .set(STAFFOG_KICK.REASON, entry.getReason())
                    .set(STAFFOG_KICK.BY_UUID, entry.getByUuid())
                    .set(STAFFOG_KICK.BY_NAME, entry.getByName())
                    .set(STAFFOG_KICK.TIME, entry.getTime())
                    .execute();

            if (entry.getPlayer() != null && entry.getPlayer().isOnline()) {

                String messageString = "&cYou have been kicked for:\n" + "&f"
                        + entry.getReason() + "\n\n" + "&c"
                        + "Please read our rules when you rejoin by running /rules";

                // Create a MiniMessage instance.
                MiniMessage miniMessage = MiniMessage.miniMessage();
                // Deserialize the string into a TextComponent.å
                Component kickMessage = miniMessage.deserialize(messageString);

                entry.getPlayer().kick(kickMessage);
            }

            Message.staffBroadcast(
                    Message.formatNotification("KICK", entry.getName() + " has been kicked by " + entry.getByName()));

        } catch (Exception error) {

            Console.error("Failed to create kick punishment.");
            error.printStackTrace();
        }
    }

    public boolean isIncludedInCurrentChatReport(String name) {
        for (ChatReportEntry entry : chatReportEntries) {
            if (name.equalsIgnoreCase(entry.getName())) {
                return true;
            }
        }

        return false;
    }

    public UUID getUUIDFromChatReport(String name) {
        for (ChatReportEntry entry : chatReportEntries) {
            if (name.equalsIgnoreCase(entry.getName())) {
                return entry.getUuid();
            }
        }

        return null;
    }

    public String exportChatReport(UUID by, UUID reported) {
        Gson gson = new Gson();
        String data = gson.toJson(chatReportEntries);

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection);
            StaffogChatreportRecord record = dsl.insertInto(STAFFOG_CHATREPORT)
                    .set(STAFFOG_CHATREPORT.UUID, reported.toString())
                    .set(STAFFOG_CHATREPORT.BY_UUID, by.toString())
                    .set(STAFFOG_CHATREPORT.TIME, System.currentTimeMillis())
                    .set(STAFFOG_CHATREPORT.MESSAGES, data)
                    .returning(STAFFOG_CHATREPORT.ID)
                    .fetchOne();

            return "" + record.getId();

        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    public String createReportFromCR(UUID by, UUID reported, int crid, String reason) {
        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            DSLContext dsl = DSL.using(connection);
            StaffogReportRecord record = dsl.insertInto(STAFFOG_REPORT)
                    .set(STAFFOG_REPORT.UUID, reported.toString())
                    .set(STAFFOG_REPORT.BY_UUID, by.toString())
                    .set(STAFFOG_REPORT.TIME, System.currentTimeMillis())
                    .set(STAFFOG_REPORT.TYPE, "CHAT")
                    .set(STAFFOG_REPORT.REASON, reason)
                    .set(STAFFOG_REPORT.CRID, crid)
                    .returning(STAFFOG_REPORT.ID)
                    .fetchOne();

            return "" + record.getId();

        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }
}
