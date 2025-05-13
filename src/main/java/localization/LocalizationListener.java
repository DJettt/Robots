package localization;

/**
 * Интерфейс, описывающий слушателя изменения локализации.
 */
public interface LocalizationListener {
    /**
     * Метод, вызывающийся при изменении локализации.
     */
    void onLanguageChanged();
}
