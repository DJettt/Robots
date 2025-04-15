package gui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Optional;

/**
 * Отвечает за робота в игре.
 */
public class GameRobot {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Конструктор.
     */
    GameRobot() {}

    /**
     * Устанавливает координаты робота.
     */
    public void setCoordinates(double x, double y) {
        m_robotPositionX = x;
        m_robotPositionY = y;
        pcs.firePropertyChange("changeCoordinate", Optional.empty(), Optional.empty());
    }

    /**
     * Геттер координаты Х.
     */
    public double getX() {
        return m_robotPositionX;
    }

    /**
     * Геттер координаты Y.
     */
    public double getY() {
        return m_robotPositionY;
    }

    /**
     * Устанавливает направление робота.
     */
    public void setDirection(double direction) {
        pcs.firePropertyChange("direction", m_robotDirection, direction);
        m_robotDirection = direction;
    }

    /**
     * Геттер направления робота.
     */
    public double getDirection() {
        return m_robotDirection;
    }

    /**
     * Добавляет слушателя изменения значений полей класса.
     * @param listener Новый слушатель.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Убирает слушателя изменения значений полей класса.
     * @param listener Слушатель, которого нужно убрать.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
