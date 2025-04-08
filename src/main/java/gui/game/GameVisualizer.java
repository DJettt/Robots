package gui.game;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

/**
 * Отвечает за визуализацию игры.
 */
public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    private final GameLogic gameLogic = new GameLogic();
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    public GameVisualizer() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameLogic.onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter() {
            /**
             * Обработка клика пользователя.
             * @param e клик мышки пользователя
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    /**
     * Устанавливает позицию цели по полученному клику.
     * @param p клик от пользователя.
     */
    protected void setTargetPosition(Point p) {
        gameLogic.setTarget(p.x, p.y);
    }

    /**
     * Перерисовывает компонент интерфейса.
     */
    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        VisualGameData visualGameData = gameLogic.getVisualData();
        drawRobot(g2d, visualGameData.getRobotX(),
                visualGameData.getRobotY(),
                visualGameData.getRobotDirection());
        drawTarget(g2d, visualGameData.getTargetX(),
                visualGameData.getTargetY());
    }

    /**
     * Отрисовывает закрашенный овал.
     * @param g графический контекст
     * @param centerX центр овала по координате x
     * @param centerY центр овала по координате y
     * @param diam1 1й диаметр овала
     * @param diam2 2й диаметр овала
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Отрисовывает контур овала.
     * @param g графический контекст
     * @param centerX центр овала по координате x
     * @param centerY центр овала по координате y
     * @param diam1 1й диаметр овала
     * @param diam2 2й диаметр овала
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Отрисовывает робота.
     * @param g графический контекст
     * @param robotCenterX Координата робота Х.
     * @param robotCenterY Координата робота У.
     * @param direction Направление робота.
     */
    private void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    /**
     * Отрисовывает цель робота.
     * @param g графический контекст
     * @param x Координата цели Х.
     * @param y Координата цели Y.
     */
    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
