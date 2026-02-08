package util;

/**
 * Klasa przechowująca dane zalogowanego użytkownika w pamięci RAM.
 */
public class UserSession {
    private static int userId;
    private static String userNick;

    /**
     * Inicjalizuje sesję. Wywołaj to w LoginController zaraz po poprawnym loginie.
     */
    public static void init(int id, String nick) {
        userId = id;
        userNick = nick;
        // Dodajemy printa, żebyś widział w konsoli IntelliJ, że ID 4 (szypki) faktycznie "wskoczyło"
        System.out.println("[SESSION] Zalogowano użytkownika: " + nick + " (ID: " + id + ")");
    }

    public static int getUserId() {
        return userId;
    }

    public static String getUserNick() {
        return userNick;
    }

    /**
     * Sprawdza, czy ktokolwiek jest zalogowany.
     */
    public static boolean isLoggedIn() {
        return userId != 0;
    }

    /**
     * Czyści dane przy wylogowaniu.
     */
    public static void clean() {
        System.out.println("[SESSION] Wylogowano: " + userNick);
        userId = 0;
        userNick = null;
    }
}