package com.dilshan.realestate.util;

import com.dilshan.realestate.model.User;
import com.dilshan.realestate.model.enums.Role;
import jakarta.servlet.http.HttpSession;

public class SessionHelper {

    public static final String SESSION_USER = "LOGGED_IN_USER";

    public static void setLoggedInUser(HttpSession session, User user) {
        session.setAttribute(SESSION_USER, user);
    }

    public static User getLoggedInUser(HttpSession session) {
        if (session == null) return null;
        return (User) session.getAttribute(SESSION_USER);
    }

    public static boolean isLoggedIn(HttpSession session) {
        return getLoggedInUser(session) != null;
    }

    public static boolean hasRole(HttpSession session, Role role) {
        User user = getLoggedInUser(session);
        return user != null && user.getRole() == role;
    }

    public static void logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute(SESSION_USER);
            session.invalidate();
        }
    }
}
