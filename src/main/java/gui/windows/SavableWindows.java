package gui.windows;

import java.awt.*;
import java.util.HashMap;

/**
 * Интерфейс сохраняемых окон.
 */
public interface SavableWindows {

    /**
     * Получает размер окна.
     */
    Dimension getSize();

    /**
     * Получает начальную точку окна.
     * @return Point - координата точки
     */
    Point getLocation();

    /**
     * Возвращает информацию о том, видимо ли окно или нет.
     * @return boolean да / нет
     */
    boolean isVisible();

    /**
     * Возвращает хеш-мап с параметрами окна.
     * @return хеш-мап с параметрами
     */
    default HashMap<String, Integer> getParams() {
        HashMap<String, Integer> params = new HashMap<String, Integer>();
        params.put("width", this.getSize().width);
        params.put("height", this.getSize().height);
        params.put("locate.x", this.getLocation().x);
        params.put("locate.y", this.getLocation().y);
        params.put("visible", this.isVisible() ? 1 : 0);
        return params;
    }

    /**
     * Возвращает поле префикса окна.
     * @return префикс
     */
    String getPrefix();

}
