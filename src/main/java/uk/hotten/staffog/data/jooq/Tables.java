package uk.hotten.staffog.data.jooq;

import uk.hotten.staffog.data.jooq.tables.StaffogAppeal;
import uk.hotten.staffog.data.jooq.tables.StaffogAudit;
import uk.hotten.staffog.data.jooq.tables.StaffogBan;
import uk.hotten.staffog.data.jooq.tables.StaffogChatreport;
import uk.hotten.staffog.data.jooq.tables.StaffogHistory;
import uk.hotten.staffog.data.jooq.tables.StaffogKick;
import uk.hotten.staffog.data.jooq.tables.StaffogLinkcode;
import uk.hotten.staffog.data.jooq.tables.StaffogMute;
import uk.hotten.staffog.data.jooq.tables.StaffogReport;
import uk.hotten.staffog.data.jooq.tables.StaffogStaffip;
import uk.hotten.staffog.data.jooq.tables.StaffogStat;
import uk.hotten.staffog.data.jooq.tables.StaffogTask;
import uk.hotten.staffog.data.jooq.tables.StaffogWeb;

/**
 * Convenience access to all tables in staffog.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Tables {

    /**
     * The table <code>staffog.staffog_appeal</code>.
     */
    public static final StaffogAppeal STAFFOG_APPEAL = StaffogAppeal.STAFFOG_APPEAL;

    /**
     * The table <code>staffog.staffog_audit</code>.
     */
    public static final StaffogAudit STAFFOG_AUDIT = StaffogAudit.STAFFOG_AUDIT;

    /**
     * The table <code>staffog.staffog_ban</code>.
     */
    public static final StaffogBan STAFFOG_BAN = StaffogBan.STAFFOG_BAN;

    /**
     * The table <code>staffog.staffog_chatreport</code>.
     */
    public static final StaffogChatreport STAFFOG_CHATREPORT = StaffogChatreport.STAFFOG_CHATREPORT;

    /**
     * The table <code>staffog.staffog_history</code>.
     */
    public static final StaffogHistory STAFFOG_HISTORY = StaffogHistory.STAFFOG_HISTORY;

    /**
     * The table <code>staffog.staffog_kick</code>.
     */
    public static final StaffogKick STAFFOG_KICK = StaffogKick.STAFFOG_KICK;

    /**
     * The table <code>staffog.staffog_linkcode</code>.
     */
    public static final StaffogLinkcode STAFFOG_LINKCODE = StaffogLinkcode.STAFFOG_LINKCODE;

    /**
     * The table <code>staffog.staffog_mute</code>.
     */
    public static final StaffogMute STAFFOG_MUTE = StaffogMute.STAFFOG_MUTE;

    /**
     * The table <code>staffog.staffog_report</code>.
     */
    public static final StaffogReport STAFFOG_REPORT = StaffogReport.STAFFOG_REPORT;

    /**
     * The table <code>staffog.staffog_staffip</code>.
     */
    public static final StaffogStaffip STAFFOG_STAFFIP = StaffogStaffip.STAFFOG_STAFFIP;

    /**
     * The table <code>staffog.staffog_stat</code>.
     */
    public static final StaffogStat STAFFOG_STAT = StaffogStat.STAFFOG_STAT;

    /**
     * The table <code>staffog.staffog_task</code>.
     */
    public static final StaffogTask STAFFOG_TASK = StaffogTask.STAFFOG_TASK;

    /**
     * The table <code>staffog.staffog_web</code>.
     */
    public static final StaffogWeb STAFFOG_WEB = StaffogWeb.STAFFOG_WEB;
}
