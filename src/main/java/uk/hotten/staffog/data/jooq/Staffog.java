package uk.hotten.staffog.data.jooq;

import java.util.Arrays;
import java.util.List;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;
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
 * This class was generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Staffog extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>staffog</code>
     */
    public static final Staffog STAFFOG = new Staffog();

    /**
     * The table <code>staffog.staffog_appeal</code>.
     */
    public final StaffogAppeal STAFFOG_APPEAL = StaffogAppeal.STAFFOG_APPEAL;

    /**
     * The table <code>staffog.staffog_audit</code>.
     */
    public final StaffogAudit STAFFOG_AUDIT = StaffogAudit.STAFFOG_AUDIT;

    /**
     * The table <code>staffog.staffog_ban</code>.
     */
    public final StaffogBan STAFFOG_BAN = StaffogBan.STAFFOG_BAN;

    /**
     * The table <code>staffog.staffog_chatreport</code>.
     */
    public final StaffogChatreport STAFFOG_CHATREPORT = StaffogChatreport.STAFFOG_CHATREPORT;

    /**
     * The table <code>staffog.staffog_history</code>.
     */
    public final StaffogHistory STAFFOG_HISTORY = StaffogHistory.STAFFOG_HISTORY;

    /**
     * The table <code>staffog.staffog_kick</code>.
     */
    public final StaffogKick STAFFOG_KICK = StaffogKick.STAFFOG_KICK;

    /**
     * The table <code>staffog.staffog_linkcode</code>.
     */
    public final StaffogLinkcode STAFFOG_LINKCODE = StaffogLinkcode.STAFFOG_LINKCODE;

    /**
     * The table <code>staffog.staffog_mute</code>.
     */
    public final StaffogMute STAFFOG_MUTE = StaffogMute.STAFFOG_MUTE;

    /**
     * The table <code>staffog.staffog_report</code>.
     */
    public final StaffogReport STAFFOG_REPORT = StaffogReport.STAFFOG_REPORT;

    /**
     * The table <code>staffog.staffog_staffip</code>.
     */
    public final StaffogStaffip STAFFOG_STAFFIP = StaffogStaffip.STAFFOG_STAFFIP;

    /**
     * The table <code>staffog.staffog_stat</code>.
     */
    public final StaffogStat STAFFOG_STAT = StaffogStat.STAFFOG_STAT;

    /**
     * The table <code>staffog.staffog_task</code>.
     */
    public final StaffogTask STAFFOG_TASK = StaffogTask.STAFFOG_TASK;

    /**
     * The table <code>staffog.staffog_web</code>.
     */
    public final StaffogWeb STAFFOG_WEB = StaffogWeb.STAFFOG_WEB;

    /**
     * No further instances allowed
     */
    private Staffog() {
        super("staffog", null);
    }

    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
                StaffogAppeal.STAFFOG_APPEAL,
                StaffogAudit.STAFFOG_AUDIT,
                StaffogBan.STAFFOG_BAN,
                StaffogChatreport.STAFFOG_CHATREPORT,
                StaffogHistory.STAFFOG_HISTORY,
                StaffogKick.STAFFOG_KICK,
                StaffogLinkcode.STAFFOG_LINKCODE,
                StaffogMute.STAFFOG_MUTE,
                StaffogReport.STAFFOG_REPORT,
                StaffogStaffip.STAFFOG_STAFFIP,
                StaffogStat.STAFFOG_STAT,
                StaffogTask.STAFFOG_TASK,
                StaffogWeb.STAFFOG_WEB);
    }
}
