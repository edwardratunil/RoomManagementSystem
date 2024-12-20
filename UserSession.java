public class UserSession {
    private static UserSession instance;
    private String loggedInUsername;

    private UserSession() {
        // Private constructor to enforce singleton pattern
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public void setLoggedInUsername(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
    }
}
