package gui.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
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
    private final static String prefix = "log";

    private static final String WIDTH = "width";
    private final static int DEF_WIDTH = 300;
    private static final String HEIGHT = "height";
    private final static int DEF_HEIGHT = 800;
    private static final String LOCATE_X = "locate.x";
    private final static int DEF_LOCATE_X = 10;
    private static final String LOCATE_Y = "locate.y";
    private final static int DEF_LOCATE_Y = 10;
    private static final String IS_ICON = "isIcon";
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
        setDefaultParameters();
        Logger.debug("Протокол работает");
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
        try {
            this.setSize(Integer.parseInt(params.get(WIDTH)), Integer.parseInt(params.get(HEIGHT)));
            this.setLocation(Integer.parseInt(params.get(LOCATE_X)), Integer.parseInt(params.get(LOCATE_Y)));
            this.setIcon(Objects.equals(params.get(IS_ICON), "1"));
        }
        catch (PropertyVetoException e) {
            e.printStackTrace();
        }
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
    public Map<String, String> getParameters() {
        Map<String, String> currentParams = new HashMap<>();
        currentParams.put(WIDTH, String.valueOf(getWidth()));
        currentParams.put(HEIGHT, String.valueOf(getHeight()));
        currentParams.put(LOCATE_X, String.valueOf(getLocation().x));
        currentParams.put(LOCATE_Y, String.valueOf(getLocation().y));
        currentParams.put(IS_ICON, isIcon ? "1" : "0");
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
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
