package log;

/**
 * Интерфейс, описывающий окно с отображением логов приложения.
 */
public interface LogChangeListener {
    /**
     * Метод при изменении логов приложения.
     */
    void onLogChanged();
}
