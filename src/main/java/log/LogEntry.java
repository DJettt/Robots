package log;

/**
 * Лог, содержащий информацию и тип.
 */
public class LogEntry {
    /**
     * Вариант лога.
     */
    private LogLevel m_logLevel;
    /**
     * Информация о приложении.
     */
    private String m_strMessage;

    /**
     * Конструктор лога.
     * @param logLevel Вариация лога.
     * @param strMessage Информация о работе приложения.
     */
    public LogEntry(LogLevel logLevel, String strMessage) {
        m_strMessage = strMessage;
        m_logLevel = logLevel;
    }

    /**
     * Геттер поля информации.
     * @return String - информация о работе приложения.
     */
    public String getMessage() {
        return m_strMessage;
    }

    /**
     * Геттер поля вариации лога.
     * @return LogLevel - Вариация лога.
     */
    public LogLevel getLevel() {
        return m_logLevel;
    }
}

