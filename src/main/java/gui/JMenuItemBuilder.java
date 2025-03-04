package gui;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.jetbrains.annotations.NotNull;

/**
 * Строитель класса JMenuItem
 */
public class JMenuItemBuilder {
    private String title;
    private Integer keyEventMnemonic;
    private Integer accelKey;
    private String command;
    private Integer accelMask;
    private ActionListener listener;

    /**
     * Пустой конструктор.
     */
    public JMenuItemBuilder() {}
    /**
     * Конструктор, который инициализирует заголовок
     * @param item кнопка
      */
    JMenuItemBuilder(@NotNull JMenuItem item) {
        this.title = item.getText();
    }

    /**
     * Устанавливает заголовок кнопки.
     * @param title заголовок
     * @return JMenuItemBuilder
     */
    public JMenuItemBuilder title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Устанавливает мнемонику кнопки.
     * @param key ключ
     * @return JMenuItemBuilder
     */
    public JMenuItemBuilder mnemonic(@NotNull Integer key) {
        this.keyEventMnemonic = key;
        return this;
    }

    /**
     * Устанавливает ключ ускоренного вызова кнопки.
     * @param key ключ
     * @return JMenuItemBuilder
     */
    public JMenuItemBuilder accelKey(@NotNull Integer key) {
        this.accelKey = key;
        return this;
    }

    /**
     * Устанавливает команду кнопки.
     * @param command кнопка
     * @return JMenuItemBuilder
     */
    public JMenuItemBuilder command(@NotNull String command) {
        this.command = command;
        return this;
    }

    /**
     * Устанавливает маску ускоренного вызова кнопки.
     * @param mask маска
     * @return JMenuItemBuilder
     */
    public JMenuItemBuilder accelMask(@NotNull Integer mask) {
        this.accelMask = mask;
        return this;
    }

    /**
     * Устанавливает слушателя действия.
     * @param listener слушатель
     * @return JMenuItemBuilder
     */
    public JMenuItemBuilder listener(@NotNull ActionListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Метод для создания JMenuItem
     * @return JMenuItem
     */
    public JMenuItem build() {
        JMenuItem menuItem = new JMenuItem(title);

        if (keyEventMnemonic != null) {
            menuItem.setMnemonic(keyEventMnemonic);
        }

        if (accelKey != null && accelMask != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(accelKey, accelMask));
        }

        if (listener != null) {
            menuItem.addActionListener(listener);
        }

        // Если нужно, можно добавить обработку команды (например, для ActionMap)
        if (command != null) {
            menuItem.setActionCommand(command);
        }

        return menuItem;
    }
}