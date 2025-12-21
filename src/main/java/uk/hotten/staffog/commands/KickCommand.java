package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.KickPunishEntry;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args == null || args.length < 1) {

            UtilitiesOG.trueogMessage((Player) sender, "&6Correct Usage: &e/kick <player> [reason]");
            return true;

        }

        final Player toKick = Bukkit.getServer().getPlayer(args[0]);
        if (toKick == null) {

            UtilitiesOG.trueogMessage((Player) sender, ("&c" + args[0] + " is not online!"));
            return true;

        }

        final KickPunishEntry entry = new KickPunishEntry();
        entry.setUuid(toKick.getUniqueId());
        entry.setName(toKick.getName());
        entry.setPlayer(toKick);
        entry.setByUuid((sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "NCP");
        entry.setByName((sender instanceof Player) ? ((Player) sender).getName() : "NCP");
        entry.setTime(System.currentTimeMillis());

        if (args.length >= 2) {

            final ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
            preReason.remove(0);
            final String reason = String.join(" ", preReason);
            entry.setReason(reason);

        } else {

            entry.setReason("No reason specified");

        }

        PunishManager.getInstance().newKickPunishment(entry);
        if (sender instanceof Player player) {

            UtilitiesOG.trueogMessage(player,
                    "&7You have kicked &e" + entry.getName() + " &7for &e" + entry.getReason() + "&7.");

        } else {

            UtilitiesOG.logToConsole(StaffOGPlugin.prependPrefix(" "),
                    "&7You have kicked &e" + entry.getName() + " &7for &e" + entry.getReason() + "&7.");

        }

        return true;

    }

}