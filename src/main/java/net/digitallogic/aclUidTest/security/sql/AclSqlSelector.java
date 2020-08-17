package net.digitallogic.aclUidTest.security.sql;

public interface AclSqlSelector {
    String getObjectIdentityPrimaryKeyQuery();
    String getInsertObjectIdentitySql();
    String getClassIdentityQuery();
    String getSidIdentityQuery();
    String getFindChildQuery();
    String getSelectClauseColumn();
    String getLookupObjectIdentitiesWhereClause();
}
