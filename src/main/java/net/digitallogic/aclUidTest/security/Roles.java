package net.digitallogic.aclUidTest.security;

public enum Roles {

    USER_ROLE("USER_ROLE"),
    ADMIN_ROLE("ADMIN_ROLE"),
    ;

    public final String name;

    private Roles(String name) {
        this.name = name;
    }
}
