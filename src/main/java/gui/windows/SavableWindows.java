package gui.windows;

/**
 * Интерфейс сохраняемых окон.
 */
public interface SavableWindows {
    /**
     * Сохраняет все параметры в файл.
     */
    void saveParameters();

    /**
     * Устанавливает параметры окна из файла.
     */
    void loadParameters();
}
