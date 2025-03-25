package gui.windows;

import java.util.HashMap;

/**
 * Кэш параметров окна.
 */
public class WindowCache {
    private final PropertiesManager manager = new PropertiesManager();
    private final String prefix;
    private final HashMap<String, String> params = new HashMap<>();

    /**
     * Конструктор хеша.
     * @param prefix префикс окна
     */
    WindowCache(String prefix) {
        this.prefix = prefix;
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    /**
     * Создает ключ с префиксом.
     * @param key ключ
     * @return ключ для поиска в общем файле
     */
    private String createKey(String key) {
        return prefix + '.' + key;
    }

    /**
     * Сохраняет параметры в файл.
     */
    public void saveParameters() {
        manager.loadProperties();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            manager.setProperty(createKey(key), value);
        }
        manager.saveProperties();
    }

    /**
     * Получает параметры из файла.
     * @return HashMap со всеми параметрами.
     */
    public HashMap<String, String> getParameters() {
        manager.loadProperties();
        HashMap<String, String> data = new HashMap<>();
        for (String key : params.keySet()) {
            data.put(key, manager.getProperty(createKey(key), params.get(key)));
        }
        return data;
    }
}
