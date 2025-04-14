package log;

/**
 * Отвечает за логи работы приложения.
 */
public final class Logger {
    /**
     * Источник обработки логов.
     */
    private static final LogWindowSource defaultLogSource;
    static {
        defaultLogSource = new LogWindowSource(100);
    }

    /**
     * Конструктор логгера.
     */
    private Logger() {}

    public static void debug(String strMessage) {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    
    public static void error(String strMessage) {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    /**
     * Геттер поля обработки логов.
     * @return LogWindowSource - Источник обработки логов.
     */
    public static LogWindowSource getDefaultLogSource() {
        return defaultLogSource;
    }
}
