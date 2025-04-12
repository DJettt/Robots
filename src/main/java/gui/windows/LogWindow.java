package gui.windows;

import gui.WindowCache;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;

/**
 * Визуализирует окно с логами.
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, SavableWindows {
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LOCATE_X = "locate.x";
    private static final String LOCATE_Y = "locate.y";
    private static final String IS_ICON = "isIcon";
    private final static String prefix = "log";
    private final WindowCache cache = new WindowCache(prefix);
    private final LogWindowSource m_logSource;
    private final TextArea m_logContent;

    /**
     * Конструктор лог-окна
     */
    public LogWindow() {
        super("Протокол работы", // title - Название игрового поля
                true,               // resizable - Можно изменять размер окна
                true,               // closable - Можно закрыть
                true,               //  maximizable - Можно сделать на весь экран
                true);              // iconifiable - Может быть иконформировано
        m_logSource = Logger.getDefaultLogSource();
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        setVisible(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);

        pack();
        updateLogContent();
        defaultParameters();
        this.setParams(cache.getParameters());
        Logger.debug("Протокол работает");
    }

    /**
     * Устанавливает в кэш дефолтные параметры окна.
     */
    private void defaultParameters() {
        cache.put(WIDTH, "300");
        cache.put(HEIGHT, "800");
        cache.put(LOCATE_X, "10");
        cache.put(LOCATE_Y, "10");
        cache.put(IS_ICON, "0");
    }

    @Override
    public void saveParameters() {
        cache.put(WIDTH, String.valueOf(getWidth()));
        cache.put(HEIGHT, String.valueOf(getHeight()));
        cache.put(LOCATE_X, String.valueOf(getLocation().x));
        cache.put(LOCATE_Y, String.valueOf(getLocation().y));
        cache.put(IS_ICON, isIcon ? "1" : "0");
        cache.saveParameters();
    }

    /**
     * Устанавливает параметры окна.
     * @param params параметры
     */
    private void setParams(HashMap<String, String> params) {
        try {
            this.setSize(Integer.parseInt(params.get(WIDTH)), Integer.parseInt(params.get(HEIGHT)));
            this.setLocation(Integer.parseInt(params.get(LOCATE_X)), Integer.parseInt(params.get(LOCATE_Y)));
            this.setIcon(Objects.equals(params.get(IS_ICON), "1"));
        }
        catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadParameters() {
        setParams(cache.getParameters());
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    /**
     * Обновление логов.
     */
    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }
}
