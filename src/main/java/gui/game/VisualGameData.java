package gui.game;

/**
 * Записывает информацию об объектах, которые нужно нарисовать.
 * @param robotX Координата робота Х.
 * @param robotY Координата робота У.
 * @param robotDirection Направление робота.
 * @param targetX Координата цели Х.
 * @param targetY Координата цели У.
 */
public record VisualGameData (double robotX,
                              double robotY,
                              double robotDirection,
                              int targetX,
                              int targetY){

    /**
     * Округляет значение.
     * @param value Значение, которое нужно изменить.
     * @return Округленное значение.
     */
    private static int round(double value) {
        return (int)(value + 0.5);
    }

    /**
     * Геттер поля X у робота.
     * @return Координата робота X.
     */
    public int getRobotX() {
        return round(robotX);
    }

    /**
     * Геттер поля Y у робота.
     * @return Координата робота У.
     */
    public int getRobotY() {
        return round(robotY);
    }

    /**
     * Геттер поля Direction у робота.
     * @return Направление робота.
     */
    public double getRobotDirection() {
        return this.robotDirection;
    }

    /**
     * Геттер поля X у цели.
     * @return Координата цели У.
     */
    public int getTargetX() {
        return targetX;
    }

    /**
     * Геттер поля Y у цели.
     * @return Координата цели У.
     */
    public int getTargetY() {
        return targetY;
    }
}
