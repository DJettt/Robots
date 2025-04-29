package gui.game;

import gui.GameModelListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Отвечает за игровую логику.
 */
public class GameModel {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final List<WeakReference<GameModelListener>> listeners = new ArrayList<>();
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private double robotX = 100;
    private double robotY = 100;
    private double robotDirection = 0;
    private int targetX = 150;
    private int targetY = 100;

    /**
     * Конструктор игровой логики.
     */
    public GameModel() {}

    /**
     * Отвечает за движения робота
     * @param velocity скорость робота по прямой
     * @param angularVelocity скорость поворота робота
     * @param duration направление робота
     */
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robotX + velocity / angularVelocity *
                (Math.sin(robotDirection  + angularVelocity * duration) -
                        Math.sin(robotDirection));
        if (!Double.isFinite(newX)) {
            newX = robotX + velocity * duration * Math.cos(robotDirection);
        }

        double newY = robotY - velocity / angularVelocity *
                (Math.cos(robotDirection + angularVelocity * duration) -
                        Math.cos(robotDirection));

        if (!Double.isFinite(newY)) {
            newY = robotY + velocity * duration * Math.sin(robotDirection);
        }
        robotX = newX;
        robotY = newY;
        robotDirection = asNormalizedRadians(robotDirection + angularVelocity * duration);
    }

    /**
     * Считает дистанцию от робота до цели.
     * @param x1 координата робота х
     * @param y1 координата робота у
     * @param x2 координата цели х
     * @param y2 координата цели у
     * @return расстояние от робота до цели
     */
    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Вычисляет новое положение робота.
     */
    public void onModelUpdateEvent() {
        double distance = distance(targetX, targetY,
                robotX, robotY);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(robotX, robotY, targetX, targetY);
        double angularVelocity = 0;
        if (angleToTarget > robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }
        moveRobot(maxVelocity, angularVelocity, 10);
        notifyAllListeners();
    }

    /**
     * Рассчитывает угол от Робота до цели.
     * @param fromX координата робота x
     * @param fromY координата робота у
     * @param toX координата цели х
     * @param toY координата цели у
     * @return Нормализованный угол от робота до цели.
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Выставляет ограничение на какую-либо величину.
     * @param value значение, которое нужно ограничить
     * @param min минимальное значение величины
     * @param max максимальное значение величины
     * @return ограниченное значение
     */
    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        return Math.min(value, max);
    }


    /**
     * Уменьшает угол в радианах до 0 <= angle <= 2*PI
     * @param angle угол
     * @return угол 0 <= angle <= 2*PI
     */
    private static double asNormalizedRadians(double angle) {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    /**
     * Устанавливает координаты цели.
     * @param x Координата цели Х.
     * @param y Координата цели Y.
     */
    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }

    /**
     * Возвращает координату робота Х.
     * @return double координата робота Х.
     */
    public double getRobotX() {
        return robotX;
    }

    /**
     * Возвращает координату робота У.
     * @return double координата робота У.
     */
    public double getRobotY() {
        return robotY;
    }

    /**
     * Возвращает направление робота.
     * @return double направление робота.
     */
    public double getRobotDirection() {
        return robotDirection;
    }

    /**
     * Возвращает координату цели Х.
     * @return double координата цели Х.
     */
    public double getTargetX() {
        return targetX;
    }

    /**
     * Возвращает координату цели Y.
     * @return double координата цели Y.
     */
    public double getTargetY() {
        return targetY;
    }

    /**
     * Добавляет слушателя изменения значений полей класса.
     * @param listener Новый слушатель.
     */
    public void addListener(GameModelListener listener) {
        this.listeners.add(new WeakReference<>(listener));
    }

    /**
     * Убирает слушателя изменения значений полей класса.
     * @param listener Слушатель, которого нужно убрать.
     */
    public void removeListener(GameModelListener listener) {
        this.listeners.remove(new WeakReference<>(listener));
    }

    /**
     * Оповещает всех слушателей об изменении значений полей.
     */
    public void notifyAllListeners() {
        for (WeakReference<GameModelListener> weakRefToListener : listeners) {
            GameModelListener listener = weakRefToListener.get();
            if (listener != null) {
                executor.submit(listener::onEvent);
            }
        }
    }
}
