package uk.hotten.staffog.data.jooq.tables;

import java.util.function.Function;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function7;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row7;
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
import uk.hotten.staffog.data.jooq.tables.records.StaffogStaffipRecord;

/**
 * This class was generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class StaffogStaffip extends TableImpl<StaffogStaffipRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>staffog.staffog_staffip</code>
     */
    public static final StaffogStaffip STAFFOG_STAFFIP = new StaffogStaffip();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StaffogStaffipRecord> getRecordType() {
        return StaffogStaffipRecord.class;
    }

    /**
     * The column <code>staffog.staffog_staffip.id</code>.
     */
    public final TableField<StaffogStaffipRecord, Integer> ID =
            createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>staffog.staffog_staffip.ip</code>.
     */
    public final TableField<StaffogStaffipRecord, String> IP =
            createField(DSL.name("ip"), SQLDataType.VARCHAR(15).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_staffip.uuid</code>.
     */
    public final TableField<StaffogStaffipRecord, String> UUID =
            createField(DSL.name("uuid"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_staffip.initial</code>.
     */
    public final TableField<StaffogStaffipRecord, Boolean> INITIAL =
            createField(DSL.name("initial"), SQLDataType.BIT.nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_staffip.panel_acknowledged</code>.
     */
    public final TableField<StaffogStaffipRecord, Boolean> PANEL_ACKNOWLEDGED =
            createField(DSL.name("panel_acknowledged"), SQLDataType.BIT.nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_staffip.panel_verified</code>.
     */
    public final TableField<StaffogStaffipRecord, Boolean> PANEL_VERIFIED =
            createField(DSL.name("panel_verified"), SQLDataType.BIT.nullable(false), this, "");

    /**
     * The column <code>staffog.staffog_staffip.game_verified</code>.
     */
    public final TableField<StaffogStaffipRecord, Boolean> GAME_VERIFIED =
            createField(DSL.name("game_verified"), SQLDataType.BIT.nullable(false), this, "");

    private StaffogStaffip(Name alias, Table<StaffogStaffipRecord> aliased) {
        this(alias, aliased, null);
    }

    private StaffogStaffip(Name alias, Table<StaffogStaffipRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>staffog.staffog_staffip</code> table reference
     */
    public StaffogStaffip(String alias) {
        this(DSL.name(alias), STAFFOG_STAFFIP);
    }

    /**
     * Create an aliased <code>staffog.staffog_staffip</code> table reference
     */
    public StaffogStaffip(Name alias) {
        this(alias, STAFFOG_STAFFIP);
    }

    /**
     * Create a <code>staffog.staffog_staffip</code> table reference
     */
    public StaffogStaffip() {
        this(DSL.name("staffog_staffip"), null);
    }

    public <O extends Record> StaffogStaffip(Table<O> child, ForeignKey<O, StaffogStaffipRecord> key) {
        super(child, key, STAFFOG_STAFFIP);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Staffog.STAFFOG;
    }

    @Override
    public Identity<StaffogStaffipRecord, Integer> getIdentity() {
        return (Identity<StaffogStaffipRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<StaffogStaffipRecord> getPrimaryKey() {
        return Keys.KEY_STAFFOG_STAFFIP_PRIMARY;
    }

    @Override
    public StaffogStaffip as(String alias) {
        return new StaffogStaffip(DSL.name(alias), this);
    }

    @Override
    public StaffogStaffip as(Name alias) {
        return new StaffogStaffip(alias, this);
    }

    @Override
    public StaffogStaffip as(Table<?> alias) {
        return new StaffogStaffip(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogStaffip rename(String name) {
        return new StaffogStaffip(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogStaffip rename(Name name) {
        return new StaffogStaffip(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public StaffogStaffip rename(Table<?> name) {
        return new StaffogStaffip(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<Integer, String, String, Boolean, Boolean, Boolean, Boolean> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(
            Function7<
                            ? super Integer,
                            ? super String,
                            ? super String,
                            ? super Boolean,
                            ? super Boolean,
                            ? super Boolean,
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
            Function7<
                            ? super Integer,
                            ? super String,
                            ? super String,
                            ? super Boolean,
                            ? super Boolean,
                            ? super Boolean,
                            ? super Boolean,
                            ? extends U>
                    from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
