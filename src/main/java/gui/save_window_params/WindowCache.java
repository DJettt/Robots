package gui.save_window_params;

import java.util.HashMap;
import java.util.Map;

/**
 * Кэш параметров окна.
 */
public class WindowCache {
    private final PropertiesManager manager = new PropertiesManager();

    /**
     * Конструктор хеша.
     */
    public WindowCache() {
    }

    /**
     * Создает ключ с префиксом.
     * @param key ключ
     * @return ключ для поиска в общем файле
     */
    private String createKey(String prefix, String key) {
        return prefix + '.' + key;
    }

    /**
     * Сохраняет параметры в файл.
     */
    public void saveParameters(Map<String, String> params, String prefix) {
        manager.loadProperties();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            manager.setProperty(createKey(prefix, key), value);
        }
        manager.saveProperties();
    }

    /**
     * Получает параметры из файла.
     * @return HashMap со всеми параметрами.
     */
    public HashMap<String, String> loadParameters(String prefix, Map<String, String> defaultParams) {
        manager.loadProperties();
        HashMap<String, String> data = new HashMap<>();
        for (String key : defaultParams.keySet()) {
            data.put(key, manager.getProperty(createKey(prefix, key), defaultParams.get(key)));
        }
        return data;
    }
}
