package gui.save_window_params;

import java.util.Map;

/**
 * Интерфейс сохраняемых окон.
 */
public interface Savable {
    /**
     * Сохраняет все параметры в файл.
     */
    Map<String, String> getParameters();

    /**
     * Устанавливает параметры окна из файла.
     */
    void loadParameters(Map<String, String> params);

    /**
     * Возвращает префикс сохраняемого окна.
     * @return String - префикс.
     */
    String getPrefix();
}
