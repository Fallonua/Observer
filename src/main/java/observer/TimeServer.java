package observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Сервер локального времени — реализация Subject.
 * Ведёт отсчёт секунд с момента запуска и уведомляет всех подписанных наблюдателей при каждом тике.
 */
public class TimeServer implements Subject {

    private int timeState; // Текущее значение счётчика (количество секунд с момента старта)
    private final List<IObserver> observers = new ArrayList<>(); // Список подписанных наблюдателей

    private Timer timer; // Таймер для периодического вызова tick()
    private TimerTask task; // Задача, выполняемая таймером каждую секунду

    private volatile boolean active; // Флаг: идёт ли в данный момент отсчёт времени
    private static final long DELAY_MS = 0; // Задержка перед первым тиком (мс), 0 — сразу
    private static final long PERIOD_MS = 1000; // Период между тиками (мс), 1000 = 1 секунда

    // Конструктор
    public TimeServer() {
        this.timeState = 0;
        this.active = false;
    }

    /**
     * Запустить отсчёт времени.
     * Создаётся таймер, который каждую секунду вызывает tick() и уведомляет наблюдателей.
     */
    public void start() {
        if (active) return;
        active = true;
        timer = new Timer(true);
        task = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
        timer.schedule(task, DELAY_MS, PERIOD_MS);
    }

    /**
     * Остановить отсчёт времени.
     * Таймер и задача отменяются, наблюдатели больше не вызываются.
     */
    public void stop() {
        if (!active) return;
        active = false;
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /** @return true, если отсчёт активен */
    public boolean isActive() {
        return active;
    }

    /**
     * Один тик: увеличить счётчик на 1 и уведомить всех наблюдателей.
     * Вызывается из фонового потока таймера.
     */
    private void tick() {
        timeState++;
        notifyAllObserver();
    }

    @Override
    public int getState() {
        return timeState;
    }

    @Override
    public void setState(int time) {
        this.timeState = time;
    }

    @Override
    // Подписка на обновления
    public void attach(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    // Уведомление всех наблюдателей
    public void notifyAllObserver() {
        for (IObserver observer : observers) {
            observer.update(this);
        }
    }
}
