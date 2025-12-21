package uk.hotten.staffog.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.security.data.StaffIPInfo;

public class LinkPanelCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player player)) {

            sender.sendMessage("ERROR: Only players can run this command!");
            return true;

        }

        final SecurityManager sm = SecurityManager.getInstance();
        if (!sm.getStaffIPInfos().containsKey(player.getUniqueId())) {

            UtilitiesOG.trueogMessage(player, "&cRequired data is missing, please re-log.");
            return true;

        }

        final StaffIPInfo ipInfo = sm.getStaffIPInfos().get(player.getUniqueId());
        if (ipInfo.isPanelVerified()) {

            UtilitiesOG.trueogMessage(player, ("&6Your IP has already been verified on the panel."));
            return true;

        }

        final String doesHaveCode = sm.doesPlayerHaveLinkCode(player.getUniqueId());
        if (doesHaveCode != null) {

            UtilitiesOG.trueogMessage(player, ("&6Your activation code is &e" + doesHaveCode));
            return true;

        }

        final String code = sm.createLinkCode(player.getUniqueId(),
                StaffOGPlugin.getVaultPerms().has(player, "staffog.paneladmin"));
        if (code == null) {

            UtilitiesOG.trueogMessage(player,
                    ("&cERROR: Failed to create activation code. Please contact an administrator."));
            return true;

        }

        UtilitiesOG.trueogMessage(player, ("&6Your activation code is: &e" + code));
        return true;

    }

}