package uk.hotten.staffog.data.jooq.tables;

import java.util.function.Function;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function12;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row12;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import uk.hotten.staffog.data.jooq.Keys;
import uk.hotten.staffog.data.jooq.Staffog;
import uk.hotten.staffog.data.jooq.tables.records.StaffogMuteRecord;

/**
 * This class was generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class StaffogMute extends TableImpl<StaffogMuteRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>staffog.staffog_mute</code>
     */
    public static final StaffogMute STAFFOG_MUTE = new StaffogMute();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StaffogMuteRecord> getRecordType() {
        return StaffogMuteRecord.class;
    }

    /**
     * The column <code>staffog.staffog_mute.id</code>.
     */
    public final TableField<StaffogMuteRecord, Long> ID =
            createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>staffog.staffog_mute.uuid</code>.
     */
    public final TableField<StaffogMuteRecord, String> UUID =
            createField(DSL.name("uuid"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_mute.reason</code>.
     */
    public final TableField<StaffogMuteRecord, String> REASON =
            createField(DSL.name("reason"), SQLDataType.VARCHAR(2048).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_mute.by_uuid</code>.
     */
    public final TableField<StaffogMuteRecord, String> BY_UUID =
            createField(DSL.name("by_uuid"), SQLDataType.VARCHAR(36), this, "");

    /**
     * The column <code>staffog.staffog_mute.by_name</code>.
     */
    public final TableField<StaffogMuteRecord, String> BY_NAME =
            createField(DSL.name("by_name"), SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>staffog.staffog_mute.removed_uuid</code>.
     */
    public final TableField<StaffogMuteRecord, String> REMOVED_UUID =
            createField(DSL.name("removed_uuid"), SQLDataType.VARCHAR(36), this, "");

    /**
     * The column <code>staffog.staffog_mute.removed_name</code>.
     */
    public final TableField<StaffogMuteRecord, String> REMOVED_NAME =
            createField(DSL.name("removed_name"), SQLDataType.VARCHAR(128), this, "");

    /**
     * The column <code>staffog.staffog_mute.removed_reason</code>.
     */
    public final TableField<StaffogMuteRecord, String> REMOVED_REASON =
            createField(DSL.name("removed_reason"), SQLDataType.VARCHAR(2048), this, "");

    /**
     * The column <code>staffog.staffog_mute.removed_time</code>.
     */
    public final TableField<StaffogMuteRecord, Long> REMOVED_TIME =
            createField(DSL.name("removed_time"), SQLDataType.BIGINT, this, "");

    /**
     * The column <code>staffog.staffog_mute.time</code>.
     */
    public final TableField<StaffogMuteRecord, Long> TIME =
            createField(DSL.name("time"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_mute.until</code>.
     */
    public final TableField<StaffogMuteRecord, Long> UNTIL =
            createField(DSL.name("until"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_mute.active</code>.
     */
    public final TableField<StaffogMuteRecord, Boolean> ACTIVE =
            createField(DSL.name("active"), SQLDataType.BIT.nullable(false), this, "");

    private StaffogMute(Name alias, Table<StaffogMuteRecord> aliased) {
        this(alias, aliased, null);
    }

    private StaffogMute(Name alias, Table<StaffogMuteRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>staffog.staffog_mute</code> table reference
     */
    public StaffogMute(String alias) {
        this(DSL.name(alias), STAFFOG_MUTE);
    }

    /**
     * Create an aliased <code>staffog.staffog_mute</code> table reference
     */
    public StaffogMute(Name alias) {
        this(alias, STAFFOG_MUTE);
    }

    /**
     * Create a <code>staffog.staffog_mute</code> table reference
     */
    public StaffogMute() {
        this(DSL.name("staffog_mute"), null);
    }

    public <O extends Record> StaffogMute(Table<O> child, ForeignKey<O, StaffogMuteRecord> key) {
        super(child, key, STAFFOG_MUTE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Staffog.STAFFOG;
    }

    @Override
    public Identity<StaffogMuteRecord, Long> getIdentity() {
        return (Identity<StaffogMuteRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<StaffogMuteRecord> getPrimaryKey() {
        return Keys.KEY_STAFFOG_MUTE_PRIMARY;
    }

    @Override
    public StaffogMute as(String alias) {
        return new StaffogMute(DSL.name(alias), this);
    }

    @Override
    public StaffogMute as(Name alias) {
        return new StaffogMute(alias, this);
    }

    @Override
    public StaffogMute as(Table<?> alias) {
        return new StaffogMute(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogMute rename(String name) {
        return new StaffogMute(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogMute rename(Name name) {
        return new StaffogMute(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogMute rename(Table<?> name) {
        return new StaffogMute(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row12 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row12<Long, String, String, String, String, String, String, String, Long, Long, Long, Boolean> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(
            Function12<
                            ? super Long,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super Long,
                            ? super Long,
                            ? super Long,
                            ? super Boolean,
                            ? extends U>
                    from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(
            Class<U> toType,
            Function12<
                            ? super Long,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super String,
                            ? super Long,
                            ? super Long,
                            ? super Long,
                            ? super Boolean,
                            ? extends U>
                    from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
