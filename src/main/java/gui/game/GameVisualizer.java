package gui.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 * Отвечает за визуализацию игры.
 */
public class GameVisualizer extends JPanel implements PropertyChangeListener
{
    private final ControllerRobot controller;
    private final GameModel model;

    public GameVisualizer(gui.game.GameModel model) {
        this.model = model;
        this.controller = new ControllerRobot(model);
        model.addListener(this);
        addMouseListener(new MouseAdapter() {
            /**
             * Обработка клика пользователя.
             * @param e клик мышки пользователя
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setChangesModel(e.getPoint().x, e.getPoint().y);
            }
        });
        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

        drawRobot(g2d, round(model.getRobotX()),
                round(model.getRobotY()),
                model.getRobotDirection());

        drawTarget(g2d, round(model.getTargetX()), round(model.getTargetY()));
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

    /**
     * Округляет значение.
     * @param value Значение, которое нужно изменить.
     * @return Округленное значение.
     */
    private static int round(double value) {
        return (int)(value + 0.5);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
}
