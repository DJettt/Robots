package gui.game;

/**
 * Отвечает за робота в игре.
 */
public class GameRobot {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

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
        m_robotDirection = direction;
    }

    /**
     * Геттер направления робота.
     */
    public double getDirection() {
        return m_robotDirection;
    }
}
