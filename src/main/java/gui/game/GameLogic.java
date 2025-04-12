package gui.game;

import gui.GameRobot;

/**
 * Отвечает за игровую логику.
 */
public class GameLogic {
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private final GameRobot robot;
    private final GameTarget target = new GameTarget();

    /**
     * Конструктор игровой логики.
     */
    public GameLogic(GameRobot robot) {
        this.robot = robot;
    }

    /**
     * Отвечает за движения робота
     * @param velocity скорость робота по прямой
     * @param angularVelocity скорость поворота робота
     * @param duration направление робота
     */
    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = robot.getX() + velocity / angularVelocity *
                (Math.sin(robot.getDirection()  + angularVelocity * duration) -
                        Math.sin(robot.getDirection()));
        if (!Double.isFinite(newX)) {
            newX = robot.getX() + velocity * duration * Math.cos(robot.getDirection());
        }

        double newY = robot.getY() - velocity / angularVelocity *
                (Math.cos(robot.getDirection() + angularVelocity * duration) -
                        Math.cos(robot.getDirection()));

        if (!Double.isFinite(newY)) {
            newY = robot.getY() + velocity * duration * Math.sin(robot.getDirection());
        }
        robot.setCoordinates(newX, newY);
        double newDirection = asNormalizedRadians(robot.getDirection() + angularVelocity * duration);
        robot.setDirection(newDirection);
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
        double distance = distance(target.getX(), target.getY(),
                robot.getX(), robot.getY());
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(robot.getX(), robot.getY(), target.getX(), target.getY());
        double angularVelocity = 0;
        if (angleToTarget > robot.getDirection()) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robot.getDirection()) {
            angularVelocity = -maxAngularVelocity;
        }
        moveRobot(maxVelocity, angularVelocity, 10);
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
        target.setCoordinates(x, y);
    }

    /**
     * Возвращает информацию, которую надо отрисовать.
     * @return Объект с информацией.
     */
    public VisualGameData getVisualData() {
        return new VisualGameData(robot.getX(),
                                    robot.getY(),
                                    robot.getDirection(),
                                    target.getX(),
                                    target.getY());
    }

    public GameRobot getRobot() {
        return this.robot;
    }
}
