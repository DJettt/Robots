package gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class CoordinateWindow extends JDialog implements PropertyChangeListener {
    private JLabel coordinatesLabel;
    private GameRobot robot;

    public CoordinateWindow(JFrame parent, GameRobot robot) {
        super(parent, "Координаты Робота", false);
        this.robot = robot;
        robot.addPropertyChangeListener(this);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        coordinatesLabel = new JLabel("\tX: ?, Y: ?, Direction: ?");
        getContentPane().add(coordinatesLabel);

        updateCoordinatesLabel(); // Инициализируем координаты при создании окна

        setSize(300, 100);
        setLocationRelativeTo(parent);
    }

    private void updateCoordinatesLabel() {
        SwingUtilities.invokeLater(() -> {
            coordinatesLabel.setText(String.format("X: %.2f, Y: %.2f, Direction: %.2f",
                    robot.getX(), robot.getY(), robot.getDirection()));
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateCoordinatesLabel();
    }
}
