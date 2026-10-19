
package budget;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger {

    private static final Logger LOGGER =
            Logger.getLogger(AppLogger.class.getName());

    static {
        LOGGER.setLevel(Level.FINE);
        LOGGER.setUseParentHandlers(false);

        java.util.logging.ConsoleHandler consoleHandler =
                new java.util.logging.ConsoleHandler();

        consoleHandler.setLevel(Level.FINE);
        consoleHandler.setFormatter(new java.util.logging.SimpleFormatter());

        LOGGER.addHandler(consoleHandler);
    }

    private AppLogger() {
    }

    public static void debug(String message) {
        LOGGER.fine("DEBUG: " + message);
    }

    public static void info(String message) {
        LOGGER.info("INFO: " + message);
    }

    public static void warning(String message) {
        LOGGER.warning("WARNING: " + message);
    }

    public static void error(String message, Exception exception) {
        LOGGER.log(Level.SEVERE, "ERROR: " + message, exception);
    }
}
