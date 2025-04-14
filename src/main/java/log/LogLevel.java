package log;

/**
 * Вариации логов приложения.
 */
public enum LogLevel {
    Trace(0),
    Debug(1),
    Info(2),
    Warning(3),
    Error(4),
    Fatal(5);
    
    private int m_iLevel;

    /**
     * Конструктор.
     * @param iLevel вариант лога.
     */
    LogLevel(int iLevel) {
        m_iLevel = iLevel;
    }

    /**
     * Геттер поля m_iLevel.
     * @return int - iLevel
     */
    public int level()
    {
        return m_iLevel;
    }
}

