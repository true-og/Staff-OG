package uk.hotten.staffog.tasks;

import java.sql.Connection;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import com.google.gson.Gson;

import lombok.Getter;
import uk.hotten.staffog.StaffOGPlugin;
import uk.hotten.staffog.data.DatabaseManager;
import uk.hotten.staffog.data.jooq.Tables;
import uk.hotten.staffog.data.jooq.tables.records.StaffogTaskRecord;
import uk.hotten.staffog.punish.PunishManager;
import uk.hotten.staffog.punish.data.PunishEntry;
import uk.hotten.staffog.tasks.data.NewAppealTask;
import uk.hotten.staffog.tasks.data.NewReportTask;
import uk.hotten.staffog.tasks.data.TaskEntry;
import uk.hotten.staffog.tasks.data.UnpunishTask;

public class TaskManager {

    @Getter
    private final JavaPlugin plugin;

    public TaskManager(JavaPlugin plugin) {

        this.plugin = plugin;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {

            for (TaskEntry entry : checkForTasks()) {

                switch (entry.getTask()) {

                    case "unpunish": {

                        processUnpunish(entry);
                        deleteTask(entry.getId());
                        break;

                    }
                    case "newreport": {

                        processNewReport(entry);
                        deleteTask(entry.getId());

                    }
                    case "newappeal": {

                        processNewAppeal(entry);
                        deleteTask(entry.getId());

                    }

                }

            }

        }, 0, 200);

    }

    private ArrayList<TaskEntry> checkForTasks() {

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            final ArrayList<TaskEntry> toReturn = new ArrayList<>();

            final DSLContext dsl = DSL.using(connection);
            final Result<StaffogTaskRecord> result = dsl.select(Tables.STAFFOG_TASK.asterisk())
                    .from(Tables.STAFFOG_TASK).fetchInto(Tables.STAFFOG_TASK);

            result.forEach(record -> {

                final TaskEntry entry = new TaskEntry();
                entry.setId(record.getId());
                entry.setTask(record.getTask());
                entry.setData(record.getData());
                toReturn.add(entry);

            });

            return toReturn;

        } catch (Exception error) {

            error.printStackTrace();
            return new ArrayList<>();

        }

    }

    private void processUnpunish(TaskEntry entry) {

        if (!"unpunish".equals(entry.getTask())) {

            return;

        }

        final Gson gson = new Gson();
        final UnpunishTask task = gson.fromJson(entry.getData(), UnpunishTask.class);
        final PunishEntry punishEntry = PunishManager.getInstance().getPunishment(task.getType(), task.getId());

        PunishManager.getInstance().visualRemovePunishment(punishEntry);

    }

    private void processNewReport(TaskEntry entry) {

        if (!"newreport".equals(entry.getTask())) {

            return;

        }

        final Gson gson = new Gson();
        final NewReportTask task = gson.fromJson(entry.getData(), NewReportTask.class);

        StaffOGPlugin.staffBroadcast(StaffOGPlugin.formatNotification("REPORT", "New " + task.getType() + " report #"
                + task.getId() + " by " + task.getBy() + " against " + task.getOffender()));

    }

    private void processNewAppeal(TaskEntry entry) {

        if (!"newappeal".equals(entry.getTask())) {

            return;

        }

        final Gson gson = new Gson();
        final NewAppealTask task = gson.fromJson(entry.getData(), NewAppealTask.class);

        StaffOGPlugin.staffBroadcast(StaffOGPlugin.formatNotification("APPEAL",
                "New " + StringUtils.lowerCase(task.getType()) + " appeal #" + task.getId() + " by " + task.getBy()));

    }

    private void deleteTask(int id) {

        try (Connection connection = DatabaseManager.getInstance().createConnection()) {

            final DSLContext dsl = DSL.using(connection);
            dsl.delete(Tables.STAFFOG_TASK).where(Tables.STAFFOG_TASK.ID.eq(id)).execute();

        } catch (Exception error) {

            error.printStackTrace();

        }

    }

}