package log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Содержит в себе все логи.
 */
public class LogWindowSource
{
    /**
     * Размер очереди с логами.
     */
    private final int m_iQueueLength;
    /**
     * Список логов.
     */
    private volatile List<LogEntry> m_messages;
    /**
     * Список слушателей логов приложения.
     */
    private final CopyOnWriteArrayList<WeakReference<LogChangeListener>> m_listeners;
    /**
     * Блокировка потоков чтения/записи.
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Конструктор источника обработки логов.
     * @param iQueueLength int - Длина очереди логов.
     */
    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = Collections.emptyList();
        m_listeners = new CopyOnWriteArrayList<>();
    }

    /**
     * Регистрирует слушателей источника обработки логов.
     * @param listener LogChangeListener - Слушатель, которого нужно добавить.
     */
    public void registerListener(LogChangeListener listener) {
        m_listeners.add(new WeakReference<>(listener));
    }

    /**
     * Убирает слушателя обработки логов.
     * @param listener LogChangeListener - слушатель, которого нужно убрать.
     */
    public void unregisterListener(LogChangeListener listener) {
        m_listeners.remove(new WeakReference<>(listener));
    }

    /**
     * Добавляет лог в список логов.
     * @param logLevel Тип лога.
     * @param strMessage Информация о логе.
     */
    public void append(LogLevel logLevel, String strMessage) {
        lock.writeLock().lock();
        try {
            List<LogEntry> newLogList = new ArrayList<>(m_messages);
            LogEntry entry = new LogEntry(logLevel, strMessage);
            newLogList.add(entry);
            if (newLogList.size() > m_iQueueLength) {
                newLogList = newLogList.subList(newLogList.size() - m_iQueueLength, newLogList.size());
            }
            m_messages = Collections.unmodifiableList(newLogList);

            for (WeakReference<LogChangeListener> weakListener : m_listeners) {
                LogChangeListener listener = weakListener.get();
                assert listener != null;
                listener.onLogChanged();
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Возвращает размер списка логов.
     * @return int - размер.
     */
    public int size() {
        lock.readLock().lock();
        try {
            return m_messages.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает часть списка логов.
     * @param startFrom начало части списка логов.
     * @param count количество элементов.
     * @return Iterable - Часть списка логов.
     */
    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_messages.size()) {
            return Collections.emptyList();
        }
        lock.readLock().lock();
        try {
            int indexTo = Math.min(startFrom + count, m_messages.size());
            return m_messages.subList(startFrom, indexTo);
        }
        finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Возвращает все логи.
     * @return Iterable - Список всех логов.
     */
    public Iterable<LogEntry> all() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(m_messages);
        } finally {
            lock.readLock().unlock();
        }
    }
}
