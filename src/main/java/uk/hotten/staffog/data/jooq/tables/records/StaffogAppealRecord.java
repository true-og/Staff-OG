package uk.hotten.staffog.data.jooq.tables.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;
import uk.hotten.staffog.data.jooq.tables.StaffogAppeal;

/**
 * This class was generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class StaffogAppealRecord extends UpdatableRecordImpl<StaffogAppealRecord>
        implements Record12<
                Integer, String, Long, String, Integer, String, String, Boolean, String, Boolean, Long, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>staffog.staffog_appeal.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.uuid</code>.
     */
    public void setUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.uuid</code>.
     */
    public String getUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.time</code>.
     */
    public void setTime(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.time</code>.
     */
    public Long getTime() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.type</code>.
     */
    public void setType(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.type</code>.
     */
    public String getType() {
        return (String) get(3);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.pid</code>.
     */
    public void setPid(Integer value) {
        set(4, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.pid</code>.
     */
    public Integer getPid() {
        return (Integer) get(4);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.reason</code>.
     */
    public void setReason(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.reason</code>.
     */
    public String getReason() {
        return (String) get(5);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.evidence</code>.
     */
    public void setEvidence(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.evidence</code>.
     */
    public String getEvidence() {
        return (String) get(6);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.open</code>.
     */
    public void setOpen(Boolean value) {
        set(7, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.open</code>.
     */
    public Boolean getOpen() {
        return (Boolean) get(7);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.assigned</code>.
     */
    public void setAssigned(String value) {
        set(8, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.assigned</code>.
     */
    public String getAssigned() {
        return (String) get(8);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.verdict</code>.
     */
    public void setVerdict(Boolean value) {
        set(9, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.verdict</code>.
     */
    public Boolean getVerdict() {
        return (Boolean) get(9);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.verdict_time</code>.
     */
    public void setVerdictTime(Long value) {
        set(10, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.verdict_time</code>.
     */
    public Long getVerdictTime() {
        return (Long) get(10);
    }

    /**
     * Setter for <code>staffog.staffog_appeal.comment</code>.
     */
    public void setComment(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>staffog.staffog_appeal.comment</code>.
     */
    public String getComment() {
        return (String) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row12<Integer, String, Long, String, Integer, String, String, Boolean, String, Boolean, Long, String>
            fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    @Override
    public Row12<Integer, String, Long, String, Integer, String, String, Boolean, String, Boolean, Long, String>
            valuesRow() {
        return (Row12) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return StaffogAppeal.STAFFOG_APPEAL.ID;
    }

    @Override
    public Field<String> field2() {
        return StaffogAppeal.STAFFOG_APPEAL.UUID;
    }

    @Override
    public Field<Long> field3() {
        return StaffogAppeal.STAFFOG_APPEAL.TIME;
    }

    @Override
    public Field<String> field4() {
        return StaffogAppeal.STAFFOG_APPEAL.TYPE;
    }

    @Override
    public Field<Integer> field5() {
        return StaffogAppeal.STAFFOG_APPEAL.PID;
    }

    @Override
    public Field<String> field6() {
        return StaffogAppeal.STAFFOG_APPEAL.REASON;
    }

    @Override
    public Field<String> field7() {
        return StaffogAppeal.STAFFOG_APPEAL.EVIDENCE;
    }

    @Override
    public Field<Boolean> field8() {
        return StaffogAppeal.STAFFOG_APPEAL.OPEN;
    }

    @Override
    public Field<String> field9() {
        return StaffogAppeal.STAFFOG_APPEAL.ASSIGNED;
    }

    @Override
    public Field<Boolean> field10() {
        return StaffogAppeal.STAFFOG_APPEAL.VERDICT;
    }

    @Override
    public Field<Long> field11() {
        return StaffogAppeal.STAFFOG_APPEAL.VERDICT_TIME;
    }

    @Override
    public Field<String> field12() {
        return StaffogAppeal.STAFFOG_APPEAL.COMMENT;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getUuid();
    }

    @Override
    public Long component3() {
        return getTime();
    }

    @Override
    public String component4() {
        return getType();
    }

    @Override
    public Integer component5() {
        return getPid();
    }

    @Override
    public String component6() {
        return getReason();
    }

    @Override
    public String component7() {
        return getEvidence();
    }

    @Override
    public Boolean component8() {
        return getOpen();
    }

    @Override
    public String component9() {
        return getAssigned();
    }

    @Override
    public Boolean component10() {
        return getVerdict();
    }

    @Override
    public Long component11() {
        return getVerdictTime();
    }

    @Override
    public String component12() {
        return getComment();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getUuid();
    }

    @Override
    public Long value3() {
        return getTime();
    }

    @Override
    public String value4() {
        return getType();
    }

    @Override
    public Integer value5() {
        return getPid();
    }

    @Override
    public String value6() {
        return getReason();
    }

    @Override
    public String value7() {
        return getEvidence();
    }

    @Override
    public Boolean value8() {
        return getOpen();
    }

    @Override
    public String value9() {
        return getAssigned();
    }

    @Override
    public Boolean value10() {
        return getVerdict();
    }

    @Override
    public Long value11() {
        return getVerdictTime();
    }

    @Override
    public String value12() {
        return getComment();
    }

    @Override
    public StaffogAppealRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value2(String value) {
        setUuid(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value3(Long value) {
        setTime(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value4(String value) {
        setType(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value5(Integer value) {
        setPid(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value6(String value) {
        setReason(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value7(String value) {
        setEvidence(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value8(Boolean value) {
        setOpen(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value9(String value) {
        setAssigned(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value10(Boolean value) {
        setVerdict(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value11(Long value) {
        setVerdictTime(value);
        return this;
    }

    @Override
    public StaffogAppealRecord value12(String value) {
        setComment(value);
        return this;
    }

    @Override
    public StaffogAppealRecord values(
            Integer value1,
            String value2,
            Long value3,
            String value4,
            Integer value5,
            String value6,
            String value7,
            Boolean value8,
            String value9,
            Boolean value10,
            Long value11,
            String value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached StaffogAppealRecord
     */
    public StaffogAppealRecord() {
        super(StaffogAppeal.STAFFOG_APPEAL);
    }

    /**
     * Create a detached, initialized StaffogAppealRecord
     */
    public StaffogAppealRecord(
            Integer id,
            String uuid,
            Long time,
            String type,
            Integer pid,
            String reason,
            String evidence,
            Boolean open,
            String assigned,
            Boolean verdict,
            Long verdictTime,
            String comment) {

        super(StaffogAppeal.STAFFOG_APPEAL);

        setId(id);
        setUuid(uuid);
        setTime(time);
        setType(type);
        setPid(pid);
        setReason(reason);
        setEvidence(evidence);
        setOpen(open);
        setAssigned(assigned);
        setVerdict(verdict);
        setVerdictTime(verdictTime);
        setComment(comment);
        resetChangedOnNotNull();
    }
}
