package gui;

import gui.windows.CoordinateWindow;
import gui.windows.GameWindow;
import gui.windows.JMenuItemBuilder;
import gui.windows.LogWindow;
import gui.windows.SavableWindows;
import gui.windows.WindowCache;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;

import log.Logger;

/**
 * Создает содержимое окна приложения, а именно окно игры и окно логов.
 */
public class MainApplicationFrame extends JFrame implements SavableWindows
{
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LOCATE_X = "locate.x";
    private static final String LOCATE_Y = "locate.y";
    private static final String IS_ICON = "isIcon";
    private static final String IS_WINDOWED = "isWindowed";

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final static String prefix = "main";
    private final WindowCache cache = new WindowCache();
    private final ArrayList<SavableWindows> savableWindows = new ArrayList<>();

    /**
     * Конструктор.
     */
    public MainApplicationFrame() {
        setDefaultParameters();
        GameRobot robot = new GameRobot();

        setContentPane(desktopPane);
        addSavableWindow(new LogWindow());
        addSavableWindow(new GameWindow(robot));
        addSavableWindow(new CoordinateWindow(this, robot));

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeProgramConfirm();
            }
        });
        pack();
        setDefaultParameters();
        setParams(cache.loadParameters(this.getPrefix(), this.getParameters()));
        setVisible(true);
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
            for (SavableWindows frame : savableWindows) {
                cache.saveParameters(frame.getParameters(), frame.getPrefix());
            }
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            cache.saveParameters(getParameters(), getPrefix());
            desktopPane.setVisible(false);
            dispose();
            System.exit(0);
        }
    }

    /**
     * Добавляет окно в приложение.
     * @param frame Информация о содержимом окна.
     */
    protected void addSavableWindow(SavableWindows frame) {
        if (frame instanceof JInternalFrame) {
            desktopPane.add((JInternalFrame) frame);
            ((JInternalFrame) frame).setVisible(true);
        } else if (frame instanceof JDialog) {
            ((JDialog) frame).setVisible(true);
        }
        frame.loadParameters(cache.loadParameters(frame.getPrefix(), frame.getParameters()));
        savableWindows.add(frame);
    }

    /**
     * Устанавливает задний фон и тему приложения.
     * @param className название фона
     */
    private void setLookAndFeel(String className) {
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

    /**
     * Устанавливает первоначальные значения параметров окна.
     */
    private void setDefaultParameters() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = screenSize.width - inset * 2;
        int windowHeight = screenSize.height - inset * 2;
        setBounds(inset, inset, windowWidth, windowHeight);
        setExtendedState(Frame.NORMAL);
        this.setLocation(inset, inset);
        this.setSize(windowWidth, windowHeight);
    }

    /**
     * Устанавливает параметры, полученные из кэша.
     * @param params Параметры окна.
     */
    private void setParams(Map<String, String> params) {
        this.setSize(Integer.parseInt(params.get(WIDTH)), Integer.parseInt(params.get(HEIGHT)));
        this.setLocation(Integer.parseInt(params.get(LOCATE_X)), Integer.parseInt(params.get(LOCATE_Y)));
        int state = Frame.NORMAL;

        if (Objects.equals(params.get(IS_ICON), "1")) {
            state |= Frame.ICONIFIED;  // Добавляем флаг свернутости
        }

        if (Objects.equals(params.get(IS_WINDOWED), "0")) {
            state |= Frame.MAXIMIZED_BOTH;  // Добавляем флаг развернутости
        }
        this.setExtendedState(state);
    }

    @Override
    public Map<String, String> getParameters() {
        Map<String, String> currentParams = new HashMap<>();
        currentParams.put(WIDTH, String.valueOf(getWidth()));
        currentParams.put(HEIGHT, String.valueOf(getHeight()));
        currentParams.put(LOCATE_X, String.valueOf(getLocation().x));
        currentParams.put(LOCATE_Y, String.valueOf(getLocation().y));
        int state = this.getExtendedState();
        currentParams.put(IS_ICON, (state & Frame.ICONIFIED) != 0 ? "1" : "0");
        currentParams.put(IS_WINDOWED, (state & Frame.MAXIMIZED_BOTH) != 0 ? "0" : "1");
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
