package main.java.com.empresa.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class LogUtil {

    private static final String LOG_FOLDER = "log";
    private static final String LOGFILE = LOG_FOLDER + "/app.log";

    private static void writeLog(String level, String mensaje) {
        try {
            java.io.File dir = new java.io.File(LOG_FOLDER);
            if (!dir.exists())
                dir.mkdirs();

            FileWriter fw = new FileWriter(LOGFILE, true);
            PrintWriter pw = new PrintWriter(fw);
            String logEntry = String.format("%s [%s] %s", java.time.LocalDateTime.now(), level, mensaje);
            pw.println(logEntry);
            pw.close();
        } catch (IOException e) {
            System.err.println("ERROR-LOG: No se pudo escribir en el archivo -> " + e.getMessage());
        }
    }

    public static void info(String mensaje) {
        writeLog("INFO", mensaje);
    }

    public static void error(String mensaje) {
        writeLog("ERROR", mensaje);
    }

    public static void warn(String mensaje) {
        writeLog("WARN", mensaje);
    }
}
