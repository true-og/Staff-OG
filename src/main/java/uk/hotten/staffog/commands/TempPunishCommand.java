package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;
import uk.hotten.staffog.utils.TimeUtils;

public class TempPunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args == null || args.length < 4) {

            UtilitiesOG.trueogMessage((Player) sender,
                    ("&6Correct Usage: &e/" + label + " <player> <unit> <amount> <reason>"));
            return true;

        }

        final int amount;
        try {

            amount = Integer.parseInt(args[2]);

        } catch (Exception error) {

            UtilitiesOG.trueogMessage((Player) sender, ("&cERROR: The amount of units must be a number!"));
            return true;

        }

        final Date currentTime = new Date(System.currentTimeMillis());
        final long untilTime;
        switch (StringUtils.lowerCase(args[1])) {

            case "d" -> untilTime = DateUtils.addDays(currentTime, amount).getTime();
            case "w" -> untilTime = DateUtils.addWeeks(currentTime, amount).getTime();
            case "m" -> untilTime = DateUtils.addMonths(currentTime, amount).getTime();
            case "y" -> untilTime = DateUtils.addYears(currentTime, amount).getTime();
            default -> {

                UtilitiesOG.trueogMessage((Player) sender,
                        ("&cERROR: Invalid time unit. &6Please supply: &e(d)ay, (w)eek, (m)onth or (y)ear&6."));
                return true;

            }

        }

        final PunishType commandType;
        if (StringUtils.contains(StringUtils.lowerCase(label), "tempban")) {

            commandType = PunishType.BAN;

        } else if (StringUtils.contains(StringUtils.lowerCase(label), "tempmute")) {

            commandType = PunishType.MUTE;

        } else {

            UtilitiesOG.trueogMessage((Player) sender, ("&cERROR: Unsupported type for command."));
            return true;

        }

        final UUID uuid = PunishManager.getInstance().getUUIDFromName(args[0]);
        if (uuid == null) {

            UtilitiesOG.trueogMessage((Player) sender, ("&c" + args[0] + " has never joined the server!"));
            return true;

        }

        final PunishEntry isPunished = PunishManager.getInstance().checkActivePunishment(commandType, uuid);
        if (isPunished != null) {

            UtilitiesOG.trueogMessage((Player) sender,
                    ("&c" + args[0] + " is already " + commandType.getBroadcastMessage() + " for "
                            + TimeUtils.formatMillisecondTime(isPunished.calculateRemaining())) + ".");
            return true;

        }

        final OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(uuid);

        final PunishEntry entry = new PunishEntry(commandType);

        entry.setUuid(uuid);
        entry.setName(player.getName());
        if (player.isOnline()) {

            entry.setPlayer(player.getPlayer());

        }

        entry.setByUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "NCP");
        entry.setByName((sender instanceof Player) ? ((Player) sender).getName() : "NCP");
        entry.setTime(currentTime.getTime());
        entry.setUntil(untilTime);
        entry.setActive(true);

        final ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
        preReason.remove(0);
        preReason.remove(0);
        preReason.remove(0);
        final String reason = String.join(" ", preReason);
        entry.setReason(reason);

        PunishManager.getInstance().newPunishment(entry);

        UtilitiesOG.trueogMessage((Player) sender, ("&7" + "You have " + commandType.getBroadcastMessage() + " "
                + entry.getName() + " for " + TimeUtils.formatMillisecondTime(entry.calculateDuration()) + "."));
        return true;

    }

}