package net.digitallogic.aclUidTest.security;

public enum Authorities {

    ADMIN_USERS_AUTHORITY("ADMIN_USERS_AUTHORITY"),
    ADMIN_ROLES_AUTHORITY("ADMIN_ROLES_AUTHORITY"),
    ADMIN_ACL_AUTHORITY("ADMIN_ACL_AUTHORITY")
    ;

    public final String name;
    private Authorities(String name) {
        this.name = name;
    }
}
