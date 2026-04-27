package uk.hotten.staffog;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import net.luckperms.api.query.QueryOptions;
import net.trueog.utilitiesog.UtilitiesOG;
import uk.hotten.staffog.commands.ChatReportCommand;
import uk.hotten.staffog.commands.KickCommand;
import uk.hotten.staffog.commands.LinkPanelCommand;
import uk.hotten.staffog.commands.PermPunishCommand;
import uk.hotten.staffog.commands.TempPunishCommand;
import uk.hotten.staffog.commands.UnpunishCommand;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.security.SecurityManager;
import uk.hotten.staffog.tasks.TaskManager;
import uk.hotten.staffog.utils.Console;

public class StaffOGPlugin extends JavaPlugin {

    // Declare variable to hold class for passing.
    private static StaffOGPlugin plugin;

    @Getter
    private static LuckPerms luckPerms;

    private static PlayerAdapter<Player> luckPermsPlayerAdapter;

    @Getter
    private static String reportWebAddress;

    @Getter
    private static String appealWebAddress;

    @Override
    public void onEnable() {

        Console.info("Setting up Staff-OG...");

        // Assign the plugin variable to the main class instance.
        plugin = this;

        // Tell JOOQ to STFU
        System.setProperty("org.jooq.no-tips", "true");

        this.saveDefaultConfig();

        if (!setupLuckPerms()) {

            Console.error("LuckPerms not found. Plugin will be disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;

        }

        reportWebAddress = this.getConfig().getString("reportWebAddress");
        appealWebAddress = this.getConfig().getString("appealWebAddress");

        DatabaseManager databaseManager = new DatabaseManager(this);
        new PunishManager(this);
        new SecurityManager(this);
        new TaskManager(this);

        getCommand("linkpanel").setExecutor(new LinkPanelCommand());
        getCommand("chatreport").setExecutor(new ChatReportCommand());

        getCommand("permban").setExecutor(new PermPunishCommand());
        getCommand("tempban").setExecutor(new TempPunishCommand());
        getCommand("unban").setExecutor(new UnpunishCommand());
        getCommand("permmute").setExecutor(new PermPunishCommand());
        getCommand("tempmute").setExecutor(new TempPunishCommand());
        getCommand("unmute").setExecutor(new UnpunishCommand());
        getCommand("kick").setExecutor(new KickCommand());

        databaseManager.setStatEntry("server_status", "online");

        Console.info("Staff-OG is ready!");

    }

    @Override
    public void onDisable() {

        if (DatabaseManager.getInstance() == null) {

            return;

        }

        DatabaseManager.getInstance().setStatEntry("server_status", "offline");
        DatabaseManager.getInstance().setStatEntry("player_count", "0");
        DatabaseManager.getInstance().setStatEntry("staff_count", "0");

    }

    private boolean setupLuckPerms() {

        RegisteredServiceProvider<LuckPerms> rsp = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (rsp == null) {

            return false;

        }

        luckPerms = rsp.getProvider();
        if (luckPerms == null) {

            return false;

        }

        luckPermsPlayerAdapter = luckPerms.getPlayerAdapter(Player.class);

        return true;

    }

    public static boolean hasPermission(Player player, String permission) {

        return luckPermsPlayerAdapter.getPermissionData(player).checkPermission(permission).asBoolean();

    }

    public static boolean hasPermission(UUID uuid, String username, String worldName, String permission) {

        if (Bukkit.isPrimaryThread()) {

            Console.error("Offline LuckPerms permission checks must not run on the main server thread.");
            return false;

        }

        boolean wasLoaded = luckPerms.getUserManager().isLoaded(uuid);
        User user;
        try {

            user = luckPerms.getUserManager().loadUser(uuid, username).get();

        } catch (InterruptedException error) {

            Thread.currentThread().interrupt();
            Console.error("Interrupted while loading LuckPerms user for permission check.");
            return false;

        } catch (ExecutionException error) {

            Console.error("Failed to load LuckPerms user for permission check.");
            error.printStackTrace();
            return false;

        }

        try {

            QueryOptions queryOptions = withWorldContext(luckPerms.getContextManager().getStaticQueryOptions(),
                    worldName);
            return user.getCachedData().getPermissionData(queryOptions).checkPermission(permission).asBoolean();

        } finally {

            if (!wasLoaded) {

                luckPerms.getUserManager().cleanupUser(user);

            }

        }

    }

    private static QueryOptions withWorldContext(QueryOptions queryOptions, String worldName) {

        MutableContextSet contexts = queryOptions.context().mutableCopy();
        contexts.removeAll("world");
        contexts.add("world", worldName);

        return queryOptions.toBuilder().context(contexts).build();

    }

    // Class constructor.
    public static StaffOGPlugin getPlugin() {

        // Pass instance of main to other classes.
        return plugin;

    }

    public static String prependPrefix(String message) {

        return "" + "&7" + "[" + "&e" + "Staff-OG" + "&7" + "] " + "&r" + message;

    }

    public static String formatNotification(String prefix, String message) {

        return "" + "&c" + StringUtils.upperCase(prefix) + " | " + "&B" + "Notification " + "&8" + "> " + "&r"
                + message;

    }

    public static void staffBroadcast(String message) {

        Bukkit.getServer().getOnlinePlayers().stream().filter(p -> p.hasPermission("staffog.seebroadcast"))
                .forEach(p -> UtilitiesOG.trueogMessage(p, message));

        UtilitiesOG.logToConsole(prependPrefix(""), message);

    }

}
