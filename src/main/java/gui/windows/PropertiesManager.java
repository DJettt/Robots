package gui.windows;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Работает с файлом .properties.
 */
public class PropertiesManager {
    private final Properties properties = new Properties();
    private final String filePath = "C://ProgramData/RobotsIDEA/cfg.properties";

    /**
     * Конструктор.
     */
    public PropertiesManager() {
        loadProperties();
    }

    /**
     * Получает значение из объекта по ключу.
     * @param key ключ
     * @return значение
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Устанавливает значения в properties.
     * @param key ключ
     * @param value значение
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Выгружает объект properties из файла.
     */
    public void loadProperties() {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке свойств: " + e.getMessage());
        }
    }

    /**
     * Метод для сохранения свойств в файл.
     */
    public void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, "Saved windows parameters.");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении свойств: " + e.getMessage());
        }
    }
}