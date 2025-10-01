package com.lgcns.svcp.prod.context;

/**
 * ThreadLocal utility to store and retrieve the current user's information.
 */
public class UserContext {
    private static final ThreadLocal<String> currentUser   = new ThreadLocal<>();

    // Set the current user
    public static void setCurrentUser(String user) {
        currentUser.set(user);
    }

    // Get the current user
    public static String getCurrentUser() {
        return currentUser.get();
    }

    // Clear the current user after request completion
    public static void clear() {
        currentUser.remove();
    }
}