package uk.hotten.staffog.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.punish.PunishManager;

public class ChatReportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage("ERROR: Only players can run this command!");
            return true;

        }

        if (args == null || args.length < 2) {

            UtilitiesOG.trueogMessage(player, "&6Correct Usage: &e/chatreport <player> <reason>");
            return true;

        }

        final PunishManager pm = PunishManager.getInstance();
        if (!pm.isIncludedInCurrentChatReport(args[0])) {

            UtilitiesOG.trueogMessage(player, ("&c" + args[0] + " has not used chat in the past two minutes."));
            return true;

        }

        final UUID reported = pm.getUUIDFromChatReport(args[0]);

        final ArrayList<String> preReason = new ArrayList<>(Arrays.asList(args));
        preReason.remove(0);
        final String reason = String.join(" ", preReason);

        final String chatReportId = pm.exportChatReport(player.getUniqueId(), reported);
        if (chatReportId == null) {

            UtilitiesOG.trueogMessage(player, "&c Failed to generate chat report. Please try again.");
            return true;

        }

        final String staffReportId = pm.createReportFromCR(player.getUniqueId(), reported,
                Integer.parseInt(chatReportId), reason);
        if (staffReportId == null) {

            UtilitiesOG.trueogMessage(player,
                    ("&cFailed to create a report to staff, however a chat log was successfully exported with ID "
                            + "&f" + "#" + chatReportId + "&c"
                            + ". Please create a manual report quoting this Chat Report ID on " + "&f"
                            + StaffOGPlugin.getReportWebAddress()));

        } else {

            UtilitiesOG.trueogMessage(player,
                    ("&a Thanks for your report! Your report reference is " + "&f" + "#" + staffReportId + "&a"
                            + ". Your chat report ID is " + "&f" + "#" + chatReportId + "&a"
                            + " and has been included in your report."));

            UtilitiesOG.trueogMessage(player, ("&2" + "You can check the status of your report at " + "&f"
                    + StaffOGPlugin.getReportWebAddress()));

            UtilitiesOG.trueogMessage(player, StaffOGPlugin.formatNotification("REPORT", "New Mutable Offences report #"
                    + staffReportId + " by " + player.getName() + " against " + args[0]));

        }

        return true;

    }

}