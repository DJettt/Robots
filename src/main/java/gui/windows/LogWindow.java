package gui.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
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
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

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

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                cache.put(WIDTH, String.valueOf(getWidth()));
                cache.put(HEIGHT, String.valueOf(getHeight()));
                super.componentResized(e);
            }
            @Override
            public void componentMoved(ComponentEvent e) {
                cache.put(LOCATE_X, String.valueOf(getLocation().x));
                cache.put(LOCATE_Y, String.valueOf(getLocation().y));
                super.componentMoved(e);
            }
            @Override
            public void componentHidden(ComponentEvent e) {
                cache.saveParameters();
                super.componentMoved(e);
            }
        });
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                cache.put(IS_ICON, String.valueOf(isIcon() ? 1 : 0));
                super.internalFrameIconified(e);
            }
            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                cache.put(IS_ICON, String.valueOf(isIcon() ? 1 : 0));
                super.internalFrameIconified(e);
            }
        });
        pack();
        updateLogContent();
        defaultParameters();
        try {
            this.setParams(cache.getParameters());
        }
        catch (PropertyVetoException e){
            e.printStackTrace();
        }
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

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void saveParameters() {
        cache.saveParameters();
    }

    /**
     * Устанавливает параметры окна.
     * @param params параметры
     */
    private void setParams(HashMap<String, String> params) throws PropertyVetoException {
        this.setSize(Integer.parseInt(params.get(WIDTH)), Integer.parseInt(params.get(HEIGHT)));
        this.setLocation(Integer.parseInt(params.get(LOCATE_X)), Integer.parseInt(params.get(LOCATE_Y)));
        this.setIcon(Objects.equals(params.get(IS_ICON), "1"));
    }
}
