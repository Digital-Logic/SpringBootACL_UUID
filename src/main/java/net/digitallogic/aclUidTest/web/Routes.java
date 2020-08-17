package net.digitallogic.aclUidTest.web;

public class Routes {

    public static final String API = "/api";
    public static final String USERS = "/users";
    public static final String ROLES = "/roles";
    public static final String AUTHORITIES = "/authorities";

    public static final String USERS_ROUTE = API+USERS;
    public static final String ROLES_ROUTE = API + ROLES;
    public static final String AUTHORITIES_ROUTE = API + AUTHORITIES;

    public static final String SIGN_IN = "/sign-in";
    public static final String SIGN_UP = "/sign-up";
    public static final String SIGN_OUT = "/sign-out";

    public static final String SIGN_IN_ROUTE = USERS_ROUTE + SIGN_IN;
    public static final String SIGN_UP_ROUTE = USERS_ROUTE + SIGN_UP;
    public static final String SIGN_OUT_ROUTE = USERS_ROUTE + SIGN_OUT;
}
