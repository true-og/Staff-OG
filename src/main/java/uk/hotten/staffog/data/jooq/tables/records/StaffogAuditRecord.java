package uk.hotten.staffog.data.jooq.tables.records;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;
import uk.hotten.staffog.data.jooq.tables.StaffogAudit;

/**
 * This class was generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class StaffogAuditRecord extends UpdatableRecordImpl<StaffogAuditRecord>
        implements Record4<Integer, String, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>staffog.staffog_audit.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>staffog.staffog_audit.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>staffog.staffog_audit.type</code>.
     */
    public void setType(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>staffog.staffog_audit.type</code>.
     */
    public String getType() {
        return (String) get(1);
    }

    /**
     * Setter for <code>staffog.staffog_audit.data</code>.
     */
    public void setData(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>staffog.staffog_audit.data</code>.
     */
    public String getData() {
        return (String) get(2);
    }

    /**
     * Setter for <code>staffog.staffog_audit.time</code>.
     */
    public void setTime(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>staffog.staffog_audit.time</code>.
     */
    public Long getTime() {
        return (Long) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, String, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Integer, String, String, Long> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return StaffogAudit.STAFFOG_AUDIT.ID;
    }

    @Override
    public Field<String> field2() {
        return StaffogAudit.STAFFOG_AUDIT.TYPE;
    }

    @Override
    public Field<String> field3() {
        return StaffogAudit.STAFFOG_AUDIT.DATA;
    }

    @Override
    public Field<Long> field4() {
        return StaffogAudit.STAFFOG_AUDIT.TIME;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getType();
    }

    @Override
    public String component3() {
        return getData();
    }

    @Override
    public Long component4() {
        return getTime();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getType();
    }

    @Override
    public String value3() {
        return getData();
    }

    @Override
    public Long value4() {
        return getTime();
    }

    @Override
    public StaffogAuditRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public StaffogAuditRecord value2(String value) {
        setType(value);
        return this;
    }

    @Override
    public StaffogAuditRecord value3(String value) {
        setData(value);
        return this;
    }

    @Override
    public StaffogAuditRecord value4(Long value) {
        setTime(value);
        return this;
    }

    @Override
    public StaffogAuditRecord values(Integer value1, String value2, String value3, Long value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached StaffogAuditRecord
     */
    public StaffogAuditRecord() {
        super(StaffogAudit.STAFFOG_AUDIT);
    }

    /**
     * Create a detached, initialized StaffogAuditRecord
     */
    public StaffogAuditRecord(Integer id, String type, String data, Long time) {
        super(StaffogAudit.STAFFOG_AUDIT);

        setId(id);
        setType(type);
        setData(data);
        setTime(time);
        resetChangedOnNotNull();
    }
}
