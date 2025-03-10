package gui.windows;

import gui.GameVisualizer;
import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Визуализирует внутреннее поле с игрой.
 */
public class GameWindow extends JInternalFrame implements SavableWindows {
    private final static String prefix = "game";
    private final GameVisualizer m_visualizer;

    /**
     * Конструктор визуализатора игры.
     */
    public GameWindow() {
        super("Игровое поле",   // title - Название игрового поля
                true,               // resizable - Можно изменять размер окна
                true,               // closable - Можно закрыть
                true,               //  maximizable - Можно сделать на весь экран
                true);              // iconifiable - Может быть иконформировано
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
