package gui.windows;

import gui.game.GameModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Отображает текущие координаты робота.
 */
public class CoordinateWindow extends JDialog implements PropertyChangeListener, SavableWindows, Cleanable {
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
    private final GameModel model;

    /**
     * Конструктор окна.
     * @param parent Отец данного окна.
     * @param model Игровая модель.
     */
    public CoordinateWindow(JFrame parent, GameModel model) {
        super(parent, "Координаты Робота", false);
        this.model = model;
        model.addListener(this);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        coordinatesLabel = new JLabel("\tX: ?\n Y: ?\n Direction: ?");
        getContentPane().add(coordinatesLabel);

        setDefaultParameters();
        setLocationRelativeTo(parent);
    }

    /**
     * Обновляет информацию при изменении параметров робота.
     */
    private void updateCoordinatesLabel(double robotX, double robotY, double robotDirection) {
        SwingUtilities.invokeLater(() -> coordinatesLabel.setText(String.format("X: %.2f,\n Y: %.2f,\n Direction: %.2f",
                    robotX, robotY, robotDirection)));
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("robotChanged")) {
            GameModel model = (GameModel) evt.getNewValue();
            updateCoordinatesLabel(model.getRobotX(), model.getRobotY(), model.getRobotDirection());
        }
    }

    @Override
    public void cleanup() {
        model.removeListener(this);
    }
}
