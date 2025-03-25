package gui.windows;

import gui.GameVisualizer;
import java.awt.BorderLayout;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Визуализирует внутреннее поле с игрой.
 */
public class GameWindow extends JInternalFrame implements SavableWindows {
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LOCATE_X = "locate.x";
    private static final String LOCATE_Y = "locate.y";
    private static final String IS_ICON = "isIcon";

    private final static String prefix = "game";

    private final GameVisualizer m_visualizer;
    private final WindowCache cache = new WindowCache(prefix);

    /**
     * Конструктор визуализатора игры.
     */
    public GameWindow() {
        super("Игровое поле",   // title - Название игрового поля
                true,               // resizable - Можно изменять размер окна
                true,               // closable - Можно закрыть
                true,               //  maximizable - Можно сделать на весь экран
                true);              // iconifiable - Может быть свернуто

        m_visualizer = new GameVisualizer();            //визуализатор игры

        setVisible(false);
        JPanel panel = new JPanel(new BorderLayout());      // Тип окна
        panel.add(m_visualizer, BorderLayout.CENTER);   // Добавление данных в панель
        getContentPane().add(panel);                        // Добавление данных в окно

        pack();

        defaultParameters();
        this.setParams(cache.getParameters());
    }

    /**
     * Устанавливает в кэш дефолтные параметры окна.
     */
    private void defaultParameters() {
        cache.put(WIDTH, "400");
        cache.put(HEIGHT, "400");
        cache.put(LOCATE_X, "0");
        cache.put(LOCATE_Y, "0");
        cache.put(IS_ICON, "0");
    }

    @Override
    public void saveParameters() {
        cache.put(WIDTH, String.valueOf(getWidth()));
        cache.put(HEIGHT, String.valueOf(getHeight()));
        cache.put(LOCATE_X, String.valueOf(getLocation().x));
        cache.put(LOCATE_Y, String.valueOf(getLocation().y));
        cache.put(IS_ICON, isIcon ? "1" : "0");

        cache.saveParameters();
    }

    /**
     * Устанавливает параметры окна.
     * @param params параметры
     */
    private void setParams(HashMap<String, String> params) {
            this.setSize(Integer.parseInt(params.get(WIDTH)), Integer.parseInt(params.get(HEIGHT)));
            this.setLocation(Integer.parseInt(params.get(LOCATE_X)), Integer.parseInt(params.get(LOCATE_Y)));
        try {
            this.setIcon(Objects.equals(params.get(IS_ICON), "1"));
        }
        catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadParameters() {
        setParams(cache.getParameters());
    }
}
