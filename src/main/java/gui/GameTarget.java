package gui;


public class GameTarget {
    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    /**
     * Конструктор цели робота.
     */
    GameTarget() {}

    /**
     * Устанавливает координаты цели.
     */
    public void setCoordinates(int x, int y) {
        m_targetPositionX = x;
        m_targetPositionY = y;
    }

    /**
     * Геттер координаты Х.
     */
    public int getX() {
        return m_targetPositionX;
    }

    /**
     * Геттер координаты Y.
     */
    public int getY() {
        return m_targetPositionY;
    }
}
