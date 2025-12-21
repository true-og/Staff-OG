package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.punish.data.PunishType;

public class UnpunishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args == null || args.length < 2) {

            UtilitiesOG.trueogMessage((Player) sender, ("&6Correct Usage: &e/" + label + " <player> <reason>"));
            return true;

        }

        final PunishType commandType;
        if (StringUtils.contains(StringUtils.lowerCase(label), "unban")) {

            commandType = PunishType.BAN;

        } else if (StringUtils.contains(StringUtils.lowerCase(label), "unmute")) {

            commandType = PunishType.MUTE;

        } else {

            UtilitiesOG.trueogMessage((Player) sender, ("&cERROR: Unsupported type for command."));
            return true;

        }

        final UUID uuid = PunishManager.getInstance().getUUIDFromName(args[0]);
        if (uuid == null) {

            UtilitiesOG.trueogMessage((Player) sender, ("&e" + args[0] + " has never joined the server!"));
            return true;

        }

        final PunishEntry entry = PunishManager.getInstance().checkActivePunishment(commandType, uuid);
        if (entry == null) {

            UtilitiesOG.trueogMessage((Player) sender,
                    ("&c" + args[0] + " is not currently " + commandType.getBroadcastMessage() + "."));
            return true;

        }

        entry.setRemovedUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "Console");
        entry.setRemovedName((sender instanceof Player) ? ((Player) sender).getName() : "Console");

        final ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
        preReason.remove(0);
        final String reason = String.join(" ", preReason);
        entry.setRemovedReason(reason);

        PunishManager.getInstance().removePunishment(entry);
        UtilitiesOG.trueogMessage((Player) sender,
                ("&7You have un" + commandType.getBroadcastMessage() + " " + entry.getName() + "."));

        return true;

    }

}