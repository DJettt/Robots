package gui.windows;


import gui.game.GameModel;
import gui.game.GameVisualizer;
import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Визуализирует внутреннее поле с игрой.
 */
public class GameWindow extends JInternalFrame implements SavableWindows {
    private final static String prefix = "game";

    private static final String WIDTH = "width";
    private final static int DEF_WIDTH = 400;
    private static final String HEIGHT = "height";
    private final static int DEF_HEIGHT = 400;
    private static final String LOCATE_X = "locate.x";
    private final static int DEF_LOCATE_X = 0;
    private static final String LOCATE_Y = "locate.y";
    private final static int DEF_LOCATE_Y = 0;
    private static final String IS_ICON = "isIcon";
    private final GameVisualizer m_visualizer;

    /**
     * Конструктор визуализатора игры.
     */
    public GameWindow(GameModel model) {
        super("Игровое поле",   // title - Название игрового поля
                true,               // resizable - Можно изменять размер окна
                true,               // closable - Можно закрыть
                true,               //  maximizable - Можно сделать на весь экран
                true);              // iconifiable - Может быть свернуто

        m_visualizer = new GameVisualizer(model);            //визуализатор игры

        setVisible(false);
        JPanel panel = new JPanel(new BorderLayout());      // Тип окна
        panel.add(m_visualizer, BorderLayout.CENTER);   // Добавление данных в панель
        getContentPane().add(panel);                        // Добавление данных в окно

        pack();
        setDefaultParameters();
    }

    /**
     * Устанавливает в кэш дефолтные параметры окна.
     */
    private void setDefaultParameters() {
        this.setSize(DEF_WIDTH, DEF_HEIGHT);
        this.setLocation(DEF_LOCATE_X, DEF_LOCATE_Y);
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> currentParams = new HashMap<>();
        currentParams.put(WIDTH, String.valueOf(getWidth()));
        currentParams.put(HEIGHT, String.valueOf(getHeight()));
        currentParams.put(LOCATE_X, String.valueOf(getLocation().x));
        currentParams.put(LOCATE_Y, String.valueOf(getLocation().y));
        currentParams.put(IS_ICON, isIcon ? "1" : "0");
        return currentParams;
    }

    /**
     * Устанавливает параметры окна.
     * @param params параметры
     */
    private void setParams(Map<String, String> params) {
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
    public void loadParameters(Map<String, String> params) {
        setParams(params);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
