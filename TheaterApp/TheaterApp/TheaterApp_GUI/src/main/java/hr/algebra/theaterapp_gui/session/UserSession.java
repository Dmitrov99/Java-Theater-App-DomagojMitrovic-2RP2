package hr.algebra.theaterapp_gui.session;

import hr.algebra.model.User;

public final class UserSession {

    private static User loggedInUser;

    private UserSession() {
    }

    public static void login(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void logout() {
        loggedInUser = null;
    }
}