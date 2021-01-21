package com.gy.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

public class LogFactory {

    private static final String LOG_NAME = "JDK_LOG";

    private static Logger logger;

    static {
        initLogger();
    }

    private static void initLogger() {
        logger = Logger.getLogger(LOG_NAME);
        logger.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();

        consoleHandler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return getCurrentDateTime()
                        + "  " + record.getLevel().getName()
                        + " [" + record.getSourceMethodName() + "]"
                        + " --- " + record.getSourceClassName()
                        + " : " + record.getMessage() + "\n";

            }
        });

        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        logger.setUseParentHandlers(false);
    }

    private static String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static Logger getLogger() {
        return logger;
    }

}
