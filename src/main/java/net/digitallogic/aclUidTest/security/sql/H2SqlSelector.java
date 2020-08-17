package net.digitallogic.aclUidTest.security.sql;

import net.digitallogic.aclUidTest.config.Profiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({ Profiles.H2 })
public class H2SqlSelector implements AclSqlSelector {

    /**
     * INVALID SQL, If i ever figure out how to get h2 to work while casting/converting between varchar and UUI types,
     * I will update this file, in the mean time it does not work...
     */

    private final static String OBJECT_IDENTITY_PRIMARY_KEY_QUERY = "select acl_object_identity.id from acl_object_identity, acl_class "
            + "where acl_object_identity.object_id_class = acl_class.id and acl_class.class=? "
            + "and acl_object_identity.object_id_identity = ?";
    private final static String CLASS_IDENTITY_QUERY = "CALL identity()";
    private final static String SID_IDENTITY_QUERY = "CALL identity()";

    private final String INSERT_OBJECT_IDENTITY_SQL = "insert into acl_object_identity "
            + "(object_id_class, object_id_identity, owner_sid, entries_inheriting) "
            + "values (?, ?, ?, ?)";

    private final String FIND_CHILD_QUERY = "select obj.object_id_identity as obj_id, "
            + "class.class as class"
            + ", class.class_id_type as class_id_type"
            + " from acl_object_identity obj, acl_object_identity parent, acl_class class "
            + "where obj.parent_object = parent.id and obj.object_id_class = class.id "
            + "and parent.object_id_identity = ? and parent.object_id_class = ("
            + "select id FROM acl_class where acl_class.class = ?)";

    private final String SELECT_CLAUSE_COLUMN = "select CAST(acl_object_identity.object_id_identity AS varchar), "
            + "acl_entry.ace_order,  "
            + "acl_object_identity.id as acl_id, "
            + "acl_object_identity.parent_object, "
            + "acl_object_identity.entries_inheriting, "
            + "acl_entry.id as ace_id, "
            + "acl_entry.mask,  "
            + "acl_entry.granting,  "
            + "acl_entry.audit_success, "
            + "acl_entry.audit_failure,  "
            + "acl_sid.principal as ace_principal, "
            + "acl_sid.sid as ace_sid,  "
            + "acli_sid.principal as acl_principal, "
            + "acli_sid.sid as acl_sid, "
            + "acl_class.class "
            + ", acl_class.class_id_type  "
            + "from acl_object_identity "
            + "left join acl_sid acli_sid on acli_sid.id = acl_object_identity.owner_sid "
            + "left join acl_class on acl_class.id = acl_object_identity.object_id_class   "
            + "left join acl_entry on acl_object_identity.id = acl_entry.acl_object_identity "
            + "left join acl_sid on acl_entry.sid = acl_sid.id  " + "where ( ";

    private final String LOOKUP_OBJECT_IDENTITIES_WHERE_CLAUSE = "(acl_object_identity.object_id_identity = CONVERT(?, UUID) and acl_class.class = ?)";


    @Override
    public String getObjectIdentityPrimaryKeyQuery() {
        return OBJECT_IDENTITY_PRIMARY_KEY_QUERY;
    }

    @Override
    public String getInsertObjectIdentitySql() { return INSERT_OBJECT_IDENTITY_SQL; }

    @Override
    public String getClassIdentityQuery() {
        return CLASS_IDENTITY_QUERY;
    }

    @Override
    public String getSidIdentityQuery() {
        return SID_IDENTITY_QUERY;
    }

    @Override
    public String getFindChildQuery() {
        return FIND_CHILD_QUERY;
    }

    @Override
    public String getSelectClauseColumn() {
        return SELECT_CLAUSE_COLUMN;
    }

    @Override
    public String getLookupObjectIdentitiesWhereClause() {
        return LOOKUP_OBJECT_IDENTITIES_WHERE_CLAUSE;
    }
}
