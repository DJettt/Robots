package gui.windows;

import gui.game.GameModel;
import gui.save_window_params.Savable;
import gui.save_window_params.WindowCache;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import javax.swing.*;

import localization.LocalizationContext;
import localization.LocalizationListener;
import log.Logger;

/**
 * Создает содержимое окна приложения, а именно окно игры и окно логов.
 */
public class MainApplicationFrame extends JFrame implements Savable, LocalizationListener
{
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LOCATE_X = "locate.x";
    private static final String LOCATE_Y = "locate.y";
    private static final String IS_ICON = "isIcon";
    private static final String IS_WINDOWED = "isWindowed";
    private static final String LOCALE = "locale";

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final static String prefix = "main";
    private final WindowCache cache = new WindowCache();
    private final ArrayList<Savable> savableWindows = new ArrayList<>();

    private final Locale englishLocale = Locale.forLanguageTag("en");
    private final Locale russianLocale = Locale.forLanguageTag("ru");
    private final LocalizationContext localizationContext = new LocalizationContext();


    /**
     * Конструктор.
     */
    public MainApplicationFrame() {
        GameModel model = new GameModel();
        loadLocale();
        setContentPane(desktopPane);
        LogWindow log = new LogWindow(localizationContext);
        GameWindow game = new GameWindow(model, localizationContext);
        CoordinateWindow coordinate = new CoordinateWindow(this, model, localizationContext);

        addSavableWindow(log);
        addSavableWindow(game);
        addSavableWindow(coordinate);

        localizationContext.addListener(this);
        localizationContext.addListener(log);
        localizationContext.addListener(game);
        localizationContext.addListener(coordinate);

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
        menuBar.add(createLocalizationMenu());
        return menuBar;
    }

    /**
     * Создает меню заднего фона.
     * @return меню заднего фона
     */
    protected JMenu createBackgroundMenu() {
        JMenu backgroundMenu = new JMenu(
                localizationContext.getString("main.menu.background.title"));
        backgroundMenu.setMnemonic(KeyEvent.VK_V);
        backgroundMenu.getAccessibleContext().setAccessibleDescription(
                localizationContext.getString("main.menu.background.title.description"));

        backgroundMenu.add(new JMenuItemBuilder()
                .title(localizationContext.getString("main.menu.background.button_system"))
                .mnemonic(KeyEvent.VK_S)
                .listener((event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();})
                .build());

        backgroundMenu.add(new JMenuItemBuilder()
                .title(localizationContext.getString("main.menu.background.button_universal"))
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
        JMenu testMenu = new JMenu(
                localizationContext.getString("main.menu.tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                localizationContext.getString("main.menu.test_command"));

        testMenu.add(new JMenuItemBuilder()
                .title(localizationContext.getString("main.log.test.debug"))
                .mnemonic(KeyEvent.VK_S)
                .listener((event) -> Logger.debug(
                        localizationContext.getString("main.log.test.debug.command")))
                .build());

        testMenu.add(new JMenuItemBuilder()
                .title(localizationContext.getString("main.log.test.error"))
                .mnemonic(KeyEvent.VK_E)
                .listener((event) -> Logger.debug(
                        localizationContext.getString("main.log.test.error.command")))
                .build());
        return testMenu;
    }

    /**
     * Создает меню Локализации
     * @return Меню "Локализация" с кнопками
     */
    protected JMenu createLocalizationMenu() {
        JMenu testMenu = new JMenu(localizationContext.getString("main.window.language.title"));
        testMenu.setMnemonic(KeyEvent.VK_L);
        testMenu.getAccessibleContext().setAccessibleDescription(
                localizationContext.getString("main.window.language.description"));

        testMenu.add(new JMenuItemBuilder()
                .title("Русский")
                .listener((event) -> localizationContext.setLocale(russianLocale))
                .build());

        testMenu.add(new JMenuItemBuilder()
                .title("English")
                .listener((event) -> localizationContext.setLocale(englishLocale))
                .build());

        return testMenu;
    }

    /**
     * Создает кнопку "Меню" в меню.
     * @return Кнопка с разделами.
     */
    protected JMenu createMenuBar() {
        JMenu menu = new JMenu(localizationContext.getString("main.menu.menu.title"));
        menu.setMnemonic(KeyEvent.VK_D);

        menu.add(new JMenuItemBuilder()
                .title(localizationContext.getString("main.menu.menu.exit"))
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
        UIManager.put(
                "OptionPane.yesButtonText",
                localizationContext.getString("main.exit.yes_button")
        );
        UIManager.put(
                "OptionPane.noButtonText",
                localizationContext.getString("main.exit.no_button")
        );
        int confirm = JOptionPane.showConfirmDialog(
                MainApplicationFrame.this,
                localizationContext.getString("main.exit.question"),
                localizationContext.getString("main.exit.window_title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            for (Savable frame : savableWindows) {
                cache.saveParameters(frame.getParameters(), frame.getPrefix());
            }
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            cache.saveParameters(
                    localizationContext.getParameters(),
                    localizationContext.getPrefix()
            );
            cache.saveParameters(getParameters(), getPrefix());
            desktopPane.setVisible(false);
            dispose();
            System.exit(0);
        }
    }

    private void loadLocale() {
        localizationContext.setParams(
                cache.loadParameters(localizationContext.getPrefix(),
                        localizationContext.getParameters())
        );
    }

    /**
     * Добавляет окно в приложение.
     * @param frame Информация о содержимом окна.
     */
    protected void addSavableWindow(Savable frame) {
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

    @Override
    public void onLanguageChanged() {
        setJMenuBar(generateMenuBar());
    }
}
