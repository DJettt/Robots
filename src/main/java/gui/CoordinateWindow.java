package gui;

import gui.windows.SavableWindows;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class CoordinateWindow extends JDialog implements PropertyChangeListener, SavableWindows {
    private final String prefix = "coordinates";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LOCATE_X = "locate.x";
    private static final String LOCATE_Y = "locate.y";
    private final WindowCache cache = new WindowCache(prefix);
    private final JLabel coordinatesLabel;
    private final GameRobot robot;

    /**
     * Конструктор окна.
     * @param parent Отец данного окна.
     * @param robot Игровой робот, координаты которого отслеживаются в окне.
     */
    public CoordinateWindow(JFrame parent, GameRobot robot) {
        super(parent, "Координаты Робота", false);
        this.robot = robot;
        robot.addPropertyChangeListener(this);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        coordinatesLabel = new JLabel("\tX: ?\n Y: ?\n Direction: ?");
        getContentPane().add(coordinatesLabel);

        updateCoordinatesLabel();

        setSize(300, 100);
        setLocationRelativeTo(parent);
    }

    /**
     * Обновляет информацию при изменении параметров класса GameRobot.
     */
    private void updateCoordinatesLabel() {
        SwingUtilities.invokeLater(() -> {
            coordinatesLabel.setText(String.format("X: %.2f,\n Y: %.2f,\n Direction: %.2f",
                    robot.getX(), robot.getY(), robot.getDirection()));
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateCoordinatesLabel();
    }

    @Override
    public void saveParameters() {

    }

    @Override
    public void loadParameters() {

    }
}
