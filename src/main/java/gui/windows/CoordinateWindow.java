package gui.windows;

import gui.GameRobot;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class CoordinateWindow extends JDialog implements PropertyChangeListener, SavableWindows {
    private final static String prefix = "coordinates";

    private static final String WIDTH = "width";
    private final static int DEF_WIDTH = 300;
    private static final String HEIGHT = "height";
    private final static int DEF_HEIGHT = 100;
    private static final String LOCATE_X = "locate.x";
    private final static int DEF_LOCATE_X = 100;
    private static final String LOCATE_Y = "locate.y";
    private final static int DEF_LOCATE_Y = 100;
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

        setDefaultParameters();
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

    /**
     * Устанавливает в кэш дефолтные параметры окна.
     */
    private void setDefaultParameters(){
        this.setSize(DEF_WIDTH, DEF_HEIGHT);
        this.setLocation(DEF_LOCATE_X, DEF_LOCATE_Y);
    }

    /**
     * Устанавливает параметры окна.
     * @param params параметры
     */
    private void setParams(Map<String, String> params) {
        this.setSize(Integer.parseInt(params.get(WIDTH)), Integer.parseInt(params.get(HEIGHT)));
        this.setLocation(Integer.parseInt(params.get(LOCATE_X)), Integer.parseInt(params.get(LOCATE_Y)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateCoordinatesLabel();
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> currentParams = new HashMap<>();
        currentParams.put(WIDTH, String.valueOf(getWidth()));
        currentParams.put(HEIGHT, String.valueOf(getHeight()));
        currentParams.put(LOCATE_X, String.valueOf(getLocation().x));
        currentParams.put(LOCATE_Y, String.valueOf(getLocation().y));
        return currentParams;
    }

    @Override
    public void loadParameters(Map<String, String> params) {
        setParams(params);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
