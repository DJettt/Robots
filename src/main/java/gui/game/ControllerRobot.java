package gui.game;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Контроллер игровой логики (модели).
 */
public class ControllerRobot {
    private final GameModel model;

    /**
     * Конструктор
     * @param model модель для подсчета изменений координат
     */
    public ControllerRobot(GameModel model){
        this.model = model;
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.onModelUpdateEvent();
            }
        }, 0, 10);
    }
    /**
     * Инициализирует таймер для переодической перерисовки
     * @return таймер
     */
    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    /**
     * Передает модели информацию от пользователя
     * @param x Координата цели Х
     * @param y Координата цели У
     */
    public void setChangesModel(int x, int y){
        model.setTarget(x, y);
    }

}
