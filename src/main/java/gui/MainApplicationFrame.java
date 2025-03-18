package gui;

import gui.windows.GameWindow;
import gui.windows.LogWindow;
import gui.windows.SavableWindows;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import log.Logger;

/**
 * Создает содержимое окна приложения, а именно окно игры и окно логов.
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    /**
     * Конструктор.
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        //addWindow(createLogWindow());
        addWindow(new LogWindow());
        addWindow(new GameWindow());

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeProgramConfirm();
            }
        });
    }

    /**
     * Генерирует меню (строка сверху).
     * @return строка меню
     */
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenuBar());
        menuBar.add(createBackgroundMenu());
        menuBar.add(createTestMenu());
        return menuBar;
    }

    /**
     * Создает меню заднего фона.
     * @return меню заднего фона
     */
    protected JMenu createBackgroundMenu() {
        JMenu backgroundMenu = new JMenu("Режим отображения");
        backgroundMenu.setMnemonic(KeyEvent.VK_V);
        backgroundMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        backgroundMenu.add(new JMenuItemBuilder()
                .title("Системная схема")
                .mnemonic(KeyEvent.VK_S)
                .listener((event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();})
                .build());

        backgroundMenu.add(new JMenuItemBuilder()
                .title("Универсальная схема")
                .mnemonic(KeyEvent.VK_S)
                .listener((event) -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();})
                .build());
        return backgroundMenu;
    }

    /**
     * Создает меню "Тесты"
     * @return Меню "Тесты" с кнопками
     */
    protected JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        testMenu.add(new JMenuItemBuilder()
                .title("Сообщение в лог")
                .mnemonic(KeyEvent.VK_S)
                .listener((event) -> Logger.debug("Новая строка"))
                .build());
        return testMenu;
    }

    /**
     * Создает кнопку "Меню" в меню.
     * @return Кнопка с разделами.
     */
    protected JMenu createMenuBar() {
        JMenu menu = new JMenu("Меню");
        menu.setMnemonic(KeyEvent.VK_D);

        menu.add(new JMenuItemBuilder()
                .title("Выход")
                .mnemonic(KeyEvent.VK_Q)
                .accelKey(KeyEvent.VK_Q)
                .accelMask(ActionEvent.ALT_MASK)
                .command("quit")
                .listener((event) -> closeProgramConfirm())
                .build());
        return menu;
    }

    /**
     * Создает окно подтверждения при попытке выхода из программы.
     */
    protected void closeProgramConfirm() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        int confirm = JOptionPane.showConfirmDialog(
                MainApplicationFrame.this,
                "Вы точно хотите выйти?",
                "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            for (JInternalFrame frame : desktopPane.getAllFrames()) {
                if (frame instanceof SavableWindows) {
                    ((SavableWindows) frame).saveParameters();
                }
            }
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

            desktopPane.setVisible(false);
            dispose();
            System.exit(0);
        }
    }

    /**
     * Создает окно с логами.
     * @return окно с логами
     */
//    protected LogWindow createLogWindow()
//    {
//        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
//        logWindow.setLocation(10,10);
//        logWindow.setSize(300, 800);
//        setMinimumSize(logWindow.getSize());
//        logWindow.pack();
//        Logger.debug("Протокол работает");
//        return logWindow;
//    }

    protected GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        //gameWindow.setLocation(10,10);
        //gameWindow.setSize(400, 400);
        //gameWindow.setIcon();
        setMinimumSize(gameWindow.getSize());
        gameWindow.pack();
        return gameWindow;
    }

    /**
     * Добавляет окно в приложение.
     * @param frame Информация о содержимом окна.
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * Устанавливает задний фон.
     * @param className название фона
     */
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
